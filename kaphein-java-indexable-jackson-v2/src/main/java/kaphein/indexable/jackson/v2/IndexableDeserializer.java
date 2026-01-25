package kaphein.indexable.jackson.v2;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

import kaphein.indexable.Indexable;
import kaphein.indexable.PropertyDescriptor;
import kaphein.indexable.MapBackedObject;
import kaphein.indexable.internal.ThrowableExtensions;

// TOOD: [P1] Replace Xxx.getDescriptorByKey(key) static method call with
// Xxx.PropertyDescriptors.getAllDescriptors().get(key) method call.
public class IndexableDeserializer
  extends StdDeserializer<Indexable>
  implements ContextualDeserializer
{
  private static final long serialVersionUID = -2038821068508596428L;

  private static final String DESC_GETTER_NAME = "getDescriptorByKey";

  private final ConcurrentMap<Class<?>, Method> descGetterByTypeMap;

  private final ConcurrentMap<Class<?>, Constructor<?>> ctorByTypeMap;

  private final JavaType targetJavaType;

  public IndexableDeserializer()
  {
    super(Indexable.class);

    this.descGetterByTypeMap = new ConcurrentHashMap<>();
    this.ctorByTypeMap = new ConcurrentHashMap<>();
    this.targetJavaType = null;
  }

  public IndexableDeserializer(final JavaType targetType)
  {
    super(targetType);

    this.descGetterByTypeMap = new ConcurrentHashMap<>();
    this.ctorByTypeMap = new ConcurrentHashMap<>();
    this.targetJavaType = targetType;
  }

  @Override
  public JsonDeserializer<?> createContextual(
    final DeserializationContext ctxt,
    final BeanProperty property
  )
    throws JsonMappingException
  {
    final JavaType type = ctxt.getContextualType();

    return new IndexableDeserializer(type);
  }

  @Override
  public Indexable deserialize(
    final JsonParser p,
    final DeserializationContext ctxt
  )
    throws IOException,
    JacksonException
  {
    Indexable result = null;

    final JsonToken token = p.currentToken();
    if(!JsonToken.VALUE_NULL.equals(token))
    {
      final JsonNode rootNode = ctxt.readTree(p);
      if(!rootNode.isObject())
      {
        ctxt.reportInputMismatch(handledType(), "null or a JSON object should be provided");
      }

      final TypeFactory typeFactory = ctxt.getTypeFactory();
      final JavaType rootJavaType = (null != targetJavaType
        ? targetJavaType
        : typeFactory.constructType(Indexable.class));
      final List<Map.Entry<String, JsonNode>> jsonPropEntries = rootNode
        .propertyStream()
        .collect(Collectors.toList());
      final List<Map.Entry<String, Object>> propEntries = new ArrayList<>(jsonPropEntries.size());
      for(final Map.Entry<String, JsonNode> jsonPropEntry : jsonPropEntries)
      {
        final String propName = jsonPropEntry.getKey();
        final JsonNode propValueNode = jsonPropEntry.getValue();

        final PropertyDescriptor<?> propDesc = tryGetDescriptorByKey(ctxt, rootJavaType, propName);
        final JavaType valueNodeType = typeFactory
          .constructType((null != propDesc ? propDesc.getType() : Object.class));

        final Object propValue = ctxt.readTreeAsValue(propValueNode, valueNodeType);

        propEntries.add(new AbstractMap.SimpleImmutableEntry<>(propName, propValue));
      }

      result = createFromEntries(ctxt, rootJavaType, propEntries);
    }

    return result;
  }

  private PropertyDescriptor<?> tryGetDescriptorByKey(
    final DeserializationContext ctxt,
    final JavaType objectType,
    final String key
  )
    throws JsonMappingException
  {
    PropertyDescriptor<?> desc = null;

    final Class<?> objectRawType = objectType.getRawClass();
    try
    {
      Method method = descGetterByTypeMap.get(objectRawType);
      if(null == method)
      {
        try
        {
          method = objectRawType.getMethod(DESC_GETTER_NAME, String.class);
          if(null != method)
          {
            // The method must be a non-private static method.
            final int modifiers = method.getModifiers();
            if(0 != (modifiers & Modifier.STATIC) && (0 == (modifiers & Modifier.PRIVATE)))
            {
              descGetterByTypeMap.put(objectRawType, method);
            }
            else
            {
              method = null;
            }
          }
        }
        catch(final NoSuchMethodException nsme)
        {
          // The method does not exist. Ignore the exception.
        }
      }

      if(null != method)
      {
        desc = (PropertyDescriptor<?>)method.invoke(null, key);
      }
    }
    catch(final InvocationTargetException ite)
    {
      throw Optional
        .ofNullable(ite.getCause())
        .map(ThrowableExtensions::coerceToRuntimeException)
        .orElseGet(() -> ThrowableExtensions.coerceToRuntimeException(ite));
    }
    catch(final IllegalAccessException | ClassCastException e)
    {
      ctxt.reportBadDefinition(
        objectType,
        String.format(
          "Type '%s' has %s method, but its method definition does not meet the contract.",
          objectRawType.getName(),
          DESC_GETTER_NAME));
    }

    return desc;
  }

  public Indexable createFromEntries(
    final DeserializationContext ctxt,
    final JavaType objectType,
    final Collection<? extends Map.Entry<? extends String, ? extends Object>> entries
  )
    throws JsonMappingException
  {
    Indexable result = null;

    final Class<?> objectRawType = objectType.getRawClass();
    try
    {
      Constructor<?> ctor = ctorByTypeMap.get(objectRawType);
      if(null == ctor)
      {
        try
        {
          ctor = objectRawType.getConstructor(Collection.class);
          if(null != ctor)
          {
            // The access modifier must not be private.
            final int modifiers = ctor.getModifiers();
            if((0 == (modifiers & Modifier.PRIVATE)))
            {
              ctorByTypeMap.put(objectRawType, ctor);
            }
            else
            {
              ctor = null;
            }
          }
        }
        catch(final NoSuchMethodException nsme)
        {
          // The constructor does not exist. Ignore the exception.
        }
      }

      if(null != ctor)
      {
        result = (Indexable)ctor.newInstance(entries);
      }
    }
    catch(final SecurityException
      | InstantiationException
      | IllegalAccessException
      | IllegalArgumentException
      | InvocationTargetException e)
    {
      ctxt.reportBadCoercion(
        this,
        objectRawType,
        e,
        "Failed to instantiate '%s'.",
        objectRawType.getName());
    }

    if(null == result)
    {
      result = new MapBackedObject(entries);
    }

    return result;
  }
}
