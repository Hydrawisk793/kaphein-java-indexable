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
      (ctx) -> (null == ctx.getValue() ? null : (Boolean)ConvertUtils.convert(ctx.getValue(), Boolean.class)),
      (ctx) -> (null == ctx.getValue() ? null : ConvertUtils.convert(ctx.getValue(), Boolean.class)));
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
      (ctx) -> (null == ctx.getValue() ? null : (Integer)ConvertUtils.convert(ctx.getValue(), Integer.class)),
      (ctx) -> (null == ctx.getValue() ? null : ConvertUtils.convert(ctx.getValue(), Integer.class)));
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
      (ctx) -> (null == ctx.getValue() ? null : (Long)ConvertUtils.convert(ctx.getValue(), Long.class)),
      (ctx) -> (null == ctx.getValue() ? null : ConvertUtils.convert(ctx.getValue(), Long.class)));
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
      (ctx) -> (null == ctx.getValue() ? null : (Double)ConvertUtils.convert(ctx.getValue(), Double.class)),
      (ctx) -> (null == ctx.getValue() ? null : ConvertUtils.convert(ctx.getValue(), Double.class)));
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
      (ctx) -> (null == ctx.getValue() ? null : (String)ConvertUtils.convert(ctx.getValue(), String.class)),
      (ctx) -> (null == ctx.getValue() ? null : ConvertUtils.convert(ctx.getValue(), String.class)));
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
      (ctx) ->
      {
        final Object v = ctx.getValue();
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
              Instant.class.getName()));
          }
        }

        return finalValue;
      },
      (ctx) -> ctx.getDescriptor().getGetter().apply(new PropertyGetter.Context<>(
        ctx.getDescriptor(),
        ctx.getTarget(),
        ctx.getValue())));
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
      (ctx) ->
      {
        final Object v = ctx.getValue();
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

        return finalValue;
      },
      (ctx) -> ctx.getDescriptor().getGetter().apply(new PropertyGetter.Context<>(
        ctx.getDescriptor(),
        ctx.getTarget(),
        ctx.getValue())));
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
      (ctx) ->
      {
        final Object v = ctx.getValue();
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

        return finalValue;
      },
      (ctx) -> ctx.getDescriptor().getGetter().apply(new PropertyGetter.Context<>(
        ctx.getDescriptor(),
        ctx.getTarget(),
        ctx.getValue())));
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
      (ctx) ->
      {
        final Object v = ctx.getValue();
        List<E> finalValue = null;
        if(null != v)
        {
          if(v instanceof List<?>)
          {
            finalValue = (List<E>)v;
          }
          else if(v instanceof Collection<?>)
          {
            finalValue = new ArrayList<>((Collection<E>)v);
          }
          else
          {
            throw new IllegalArgumentException(String.format(
              "Cannot coerce '%s' to '%s'.",
              v.getClass().getName(),
              List.class.getName()));
          }
        }

        return finalValue;
      },
      (ctx) -> ctx.getDescriptor().getGetter().apply(new PropertyGetter.Context<>(
        ctx.getDescriptor(),
        ctx.getTarget(),
        ctx.getValue())));
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
      (ctx) ->
      {
        final Object v = ctx.getValue();
        Set<E> finalValue = null;
        if(null != v)
        {
          if(v instanceof Set<?>)
          {
            finalValue = (Set<E>)v;
          }
          else if(v instanceof Collection<?>)
          {
            finalValue = new LinkedHashSet<>((Collection<E>)v);
          }
          else
          {
            throw new IllegalArgumentException(String.format(
              "Cannot coerce '%s' to '%s'.",
              v.getClass().getName(),
              Set.class.getName()));
          }
        }

        return finalValue;
      },
      (ctx) -> ctx.getDescriptor().getGetter().apply(new PropertyGetter.Context<>(
        ctx.getDescriptor(),
        ctx.getTarget(),
        ctx.getValue())));
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
      (ctx) -> (Map<String, Object>)ctx.getValue(),
      (ctx) -> (Map<String, Object>)ctx.getValue());
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
      (ctx) -> (Map<String, String>)ctx.getValue(),
      (ctx) -> (Map<String, String>)ctx.getValue());
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

  @SuppressWarnings("unchecked")
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
      (ctx) -> (null == ctx.getValue()
        ? null
        : ((Class<T>)ctx.getDescriptor().getType()).cast(ctx.getValue())),
      (ctx) -> (null == ctx.getValue()
        ? null
        : ((Class<T>)ctx.getDescriptor().getType()).cast(ctx.getValue())));
  }
}
