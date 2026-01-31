package kaphein.indexable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections4.MapUtils;

public final class PropertyDescriptorFactories
{
  private PropertyDescriptorFactories()
  {
    // Empty.
  }

  public static PropertyDescriptor<Boolean> createBoolean(
    final String mapKey
  )
  {
    return createBoolean(
      mapKey,
      mapKey);
  }

  public static PropertyDescriptor<Boolean> createBoolean(
    final String mapKey,
    final String propName
  )
  {
    return new PlainPropertyDescriptor<>(
      mapKey,
      propName,
      Boolean.class,
      Boolean.class::cast,
      (desc, m) -> MapUtils.getBoolean(m, mapKey),
      (desc, m, v) -> m.put(
        mapKey,
        (null == v ? null : ConvertUtils.convert(v, Boolean.class))));
  }

  public static PropertyDescriptor<Integer> createInteger(
    final String mapKey
  )
  {
    return createInteger(
      mapKey,
      mapKey);
  }

  public static PropertyDescriptor<Integer> createInteger(
    final String mapKey,
    final String propName
  )
  {
    return new PlainPropertyDescriptor<>(
      mapKey,
      propName,
      Integer.class,
      Integer.class::cast,
      (desc, m) -> MapUtils.getInteger(m, mapKey),
      (desc, m, v) -> m.put(
        mapKey,
        (null == v ? null : ConvertUtils.convert(v, Integer.class))));
  }

  public static PropertyDescriptor<Long> createLong(
    final String mapKey
  )
  {
    return createLong(
      mapKey,
      mapKey);
  }

  public static PropertyDescriptor<Long> createLong(
    final String mapKey,
    final String propName
  )
  {
    return new PlainPropertyDescriptor<>(
      mapKey,
      propName,
      Long.class,
      Long.class::cast,
      (desc, m) -> MapUtils.getLong(m, mapKey),
      (desc, m, v) -> m.put(
        mapKey,
        (null == v ? null : ConvertUtils.convert(v, Long.class))));
  }

  public static PropertyDescriptor<Double> createDouble(
    final String mapKey
  )
  {
    return createDouble(
      mapKey,
      mapKey);
  }

  public static PropertyDescriptor<Double> createDouble(
    final String mapKey,
    final String propName
  )
  {
    return new PlainPropertyDescriptor<>(
      mapKey,
      propName,
      Double.class,
      Double.class::cast,
      (desc, m) -> MapUtils.getDouble(m, mapKey),
      (desc, m, v) -> m.put(
        mapKey,
        (null == v ? null : ConvertUtils.convert(v, Double.class))));
  }

  public static PropertyDescriptor<String> createString(
    final String mapKey
  )
  {
    return createString(
      mapKey,
      mapKey);
  }

  public static PropertyDescriptor<String> createString(
    final String mapKey,
    final String propName
  )
  {
    return new PlainPropertyDescriptor<>(
      mapKey,
      propName,
      String.class,
      String.class::cast,
      (desc, m) -> MapUtils.getString(m, mapKey),
      (desc, m, v) -> m.put(
        mapKey,
        (null == v ? null : ConvertUtils.convert(v, String.class))));
  }

  public static PropertyDescriptor<Instant> createInstant(
    final String mapKey
  )
  {
    return createInstant(
      mapKey,
      mapKey);
  }

  public static PropertyDescriptor<Instant> createInstant(
    final String mapKey,
    final String propName
  )
  {
    return new PlainPropertyDescriptor<>(
      mapKey,
      propName,
      Instant.class,
      Instant.class::cast,
      (desc, m) -> (Instant)MapUtils.getObject(m, mapKey),
      (desc, m, v) ->
      {
        Instant finalValue = null;

        if(null != v)
        {
          if(v instanceof Instant)
          {
            finalValue = (Instant)v;
          }
          else if(v instanceof CharSequence)
          {
            finalValue = Instant.parse(((CharSequence)v).toString());
          }
          else
          {
            throw new IllegalArgumentException(String.format(
              "Cannot coerce '%s' to '%s'.",
              v.getClass().getName(),
              URI.class.getName()));
          }
        }

        if(null != finalValue)
        {
          m.put(mapKey, finalValue);
        }
      });
  }

  public static PropertyDescriptor<ZonedDateTime> createZonedDateTime(
    final String mapKey
  )
  {
    return createZonedDateTime(
      mapKey,
      mapKey);
  }

  public static PropertyDescriptor<ZonedDateTime> createZonedDateTime(
    final String mapKey,
    final String propName
  )
  {
    return new PlainPropertyDescriptor<>(
      mapKey,
      propName,
      ZonedDateTime.class,
      ZonedDateTime.class::cast,
      (desc, m) -> (ZonedDateTime)MapUtils.getObject(m, mapKey),
      (desc, m, v) ->
      {
        ZonedDateTime finalValue = null;

        if(null != v)
        {
          if(v instanceof ZonedDateTime)
          {
            finalValue = (ZonedDateTime)v;
          }
          else if(v instanceof CharSequence)
          {
            finalValue = ZonedDateTime.parse(((CharSequence)v).toString());
          }
          else
          {
            throw new IllegalArgumentException(String.format(
              "Cannot coerce '%s' to '%s'.",
              v.getClass().getName(),
              URI.class.getName()));
          }
        }

        if(null != finalValue)
        {
          m.put(mapKey, finalValue);
        }
      });
  }

  public static PropertyDescriptor<URI> createUri(
    final String mapKey
  )
  {
    return createUri(
      mapKey,
      mapKey);
  }

  public static PropertyDescriptor<URI> createUri(
    final String mapKey,
    final String propName
  )
  {
    return new PlainPropertyDescriptor<>(
      mapKey,
      propName,
      URI.class,
      URI.class::cast,
      (desc, m) -> (URI)MapUtils.getObject(m, mapKey),
      (desc, m, v) ->
      {
        URI finalValue = null;

        if(null != v)
        {
          if(v instanceof URI)
          {
            finalValue = (URI)v;
          }
          else if(v instanceof CharSequence)
          {
            finalValue = URI.create(((CharSequence)v).toString());
          }
          else
          {
            throw new IllegalArgumentException(String.format(
              "Cannot coerce '%s' to '%s'.",
              v.getClass().getName(),
              URI.class.getName()));
          }
        }

        if(null != finalValue)
        {
          m.put(mapKey, finalValue);
        }
      });
  }

  public static <E> PropertyDescriptor<List<E>> createList(
    final String mapKey,
    final Class<E> elementType
  )
  {
    return createList(
      mapKey,
      mapKey,
      elementType);
  }

  @SuppressWarnings("unchecked")
  public static <E> PropertyDescriptor<List<E>> createList(
    final String mapKey,
    final String propName,
    final Class<E> elementType
  )
  {
    return new PlainPropertyDescriptor<>(
      mapKey,
      propName,
      (new ParameterizedType()
      {
        @Override
        public Type getOwnerType()
        {
          return null;
        }

        @Override
        public Type getRawType()
        {
          return List.class;
        }

        @Override
        public Type[] getActualTypeArguments()
        {
          return new Type[] {elementType};
        }

        @Override
        public String getTypeName()
        {
          return String.format(
            "%s<%s>",
            ((Class<?>)getRawType()).getTypeName(),
            elementType.getName());
        }
      }),
      List.class::cast,
      (desc, m) -> (List<E>)(m.get(mapKey)),
      (desc, m, v) ->
      {
        List<E> finalValue = null;
        if(null != v)
        {
          if(v instanceof List<?>)
          {
            finalValue = (List<E>)v;
          }
          else if(v instanceof Collection<?>)
          {
            v = new ArrayList<>((Collection<E>)v);
          }
          else
          {
            throw new IllegalArgumentException(String.format(
              "Cannot coerce '%s' to '%s'.",
              v.getClass().getName(),
              List.class.getName()));
          }
        }

        if(null != finalValue)
        {
          m.put(mapKey, finalValue);
        }
      });
  }

  public static <E> PropertyDescriptor<Set<E>> createSet(
    final String mapKey,
    final Class<E> elementType
  )
  {
    return createSet(
      mapKey,
      mapKey,
      elementType);
  }

  @SuppressWarnings("unchecked")
  public static <E> PropertyDescriptor<Set<E>> createSet(
    final String mapKey,
    final String propName,
    final Class<E> elementType
  )
  {
    return new PlainPropertyDescriptor<>(
      mapKey,
      propName,
      (new ParameterizedType()
      {
        @Override
        public Type getOwnerType()
        {
          return null;
        }

        @Override
        public Type getRawType()
        {
          return Set.class;
        }

        @Override
        public Type[] getActualTypeArguments()
        {
          return new Type[] {elementType};
        }

        @Override
        public String getTypeName()
        {
          return String.format(
            "%s<%s>",
            ((Class<?>)getRawType()).getTypeName(),
            elementType.getName());
        }
      }),
      Set.class::cast,
      (desc, m) -> (Set<E>)(m.get(mapKey)),
      (desc, m, v) ->
      {
        Set<E> finalValue = null;
        if(null != v)
        {
          if(v instanceof Set<?>)
          {
            finalValue = (Set<E>)v;
          }
          else if(v instanceof Collection<?>)
          {
            v = new LinkedHashSet<>((Collection<E>)v);
          }
          else
          {
            throw new IllegalArgumentException(String.format(
              "Cannot coerce '%s' to '%s'.",
              v.getClass().getName(),
              Set.class.getName()));
          }
        }

        if(null != finalValue)
        {
          m.put(mapKey, finalValue);
        }
      });
  }

  public static PropertyDescriptor<Map<String, Object>> createStringObjectMap(
    final String mapKey
  )
  {
    return createStringObjectMap(
      mapKey,
      mapKey);
  }

  @SuppressWarnings("unchecked")
  public static PropertyDescriptor<Map<String, Object>> createStringObjectMap(
    final String mapKey,
    final String propName
  )
  {
    return new PlainPropertyDescriptor<>(
      mapKey,
      propName,
      (new ParameterizedType()
      {
        @Override
        public Type getOwnerType()
        {
          return null;
        }

        @Override
        public Type getRawType()
        {
          return Map.class;
        }

        @Override
        public Type[] getActualTypeArguments()
        {
          return new Type[] {String.class, Object.class};
        }

        @Override
        public String getTypeName()
        {
          return String.format(
            "%s<%s,%s>",
            ((Class<?>)getRawType()).getTypeName(),
            String.class.getName(),
            Object.class.getName());
        }
      }),
      Map.class::cast,
      (desc, m) -> (Map<String, Object>)MapUtils.getMap(m, mapKey),
      (desc, m, v) -> m.put(mapKey, (Map<String, Object>)v));
  }

  public static PropertyDescriptor<Map<String, String>> createStringStringMap(
    final String mapKey
  )
  {
    return createStringStringMap(
      mapKey,
      mapKey);
  }

  @SuppressWarnings("unchecked")
  public static PropertyDescriptor<Map<String, String>> createStringStringMap(
    final String mapKey,
    final String propName
  )
  {
    return new PlainPropertyDescriptor<>(
      mapKey,
      propName,
      (new ParameterizedType()
      {
        @Override
        public Type getOwnerType()
        {
          return null;
        }

        @Override
        public Type getRawType()
        {
          return Map.class;
        }

        @Override
        public Type[] getActualTypeArguments()
        {
          return new Type[] {String.class, String.class};
        }

        @Override
        public String getTypeName()
        {
          return String.format(
            "%s<%s,%s>",
            ((Class<?>)getRawType()).getTypeName(),
            String.class.getName(),
            Object.class.getName());
        }
      }),
      Map.class::cast,
      (desc, m) -> (Map<String, String>)MapUtils.getMap(m, mapKey),
      (desc, m, v) -> m.put(mapKey, (Map<String, String>)v));
  }

  public static <T> PropertyDescriptor<T> createSimple(
    final String mapKey,
    final Class<T> type
  )
  {
    return createSimple(
      mapKey,
      mapKey,
      type);
  }

  public static <T> PropertyDescriptor<T> createSimple(
    final String mapKey,
    final String propName,
    final Class<T> type
  )
  {
    return new PlainPropertyDescriptor<>(
      mapKey,
      propName,
      type,
      type::cast,
      (desc, m) -> desc.getCoercer().coerce(m.get(mapKey)),
      (desc, m, v) -> m.put(
        mapKey,
        (null == v ? null : desc.getCoercer().coerce((v)))));
  }
}
