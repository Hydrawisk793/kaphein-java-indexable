package kaphein.indexable;

import java.lang.reflect.Type;

import kaphein.indexable.internal.AssertArg;

/**
 *  A POJO implemenation of {@link PropertyDescriptor}.
 */
public class PlainPropertyDescriptor<T> implements PropertyDescriptor<T>
{
  private final String propertyName;

  private final String indexKey;

  private final Type type;

  private final PropertyCoercer<T> coercer;

  private final PropertyGetter<T> getter;

  private final PropertySetter<T> setter;

  public PlainPropertyDescriptor(
    final PropertyDescriptor<T> src
  )
  {
    this(
      AssertArg.isNotNull(src, "src").getPropertyName(),
      AssertArg.isNotNull(src, "src").getIndexKey(),
      AssertArg.isNotNull(src, "src").getType(),
      AssertArg.isNotNull(src, "src").getCoercer(),
      AssertArg.isNotNull(src, "src").getGetter(),
      AssertArg.isNotNull(src, "src").getSetter());
  }

  public PlainPropertyDescriptor(
    final String indexKey,
    final Type type,
    final PropertyCoercer<T> coercer,
    final PropertyGetter<T> getter,
    final PropertySetter<T> setter
  )
  {
    this(
      indexKey,
      indexKey,
      type,
      coercer,
      getter,
      setter);
  }

  public PlainPropertyDescriptor(
    final String indexKey,
    final String propertyName,
    final Type type,
    final PropertyCoercer<T> coercer,
    final PropertyGetter<T> getter,
    final PropertySetter<T> setter
  )
  {
    this.indexKey = AssertArg.isNotBlank(indexKey, "indexKey");
    this.propertyName = AssertArg.isNotBlank(propertyName, "propertyName");
    this.type = AssertArg.isNotNull(type, "type");
    this.coercer = AssertArg.isNotNull(coercer, "coercer");
    this.getter = AssertArg.isNotNull(getter, "getter");
    this.setter = AssertArg.isNotNull(setter, "setter");
  }

  @Override
  public String getIndexKey()
  {
    return indexKey;
  }

  @Override
  public String getPropertyName()
  {
    return propertyName;
  }

  @Override
  public Type getType()
  {
    return type;
  }

  @Override
  public PropertyCoercer<T> getCoercer()
  {
    return coercer;
  }

  @Override
  public PropertyGetter<T> getGetter()
  {
    return getter;
  }

  @Override
  public PropertySetter<T> getSetter()
  {
    return setter;
  }
}
