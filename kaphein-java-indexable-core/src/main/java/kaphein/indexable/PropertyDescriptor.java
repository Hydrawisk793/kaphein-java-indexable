package kaphein.indexable;

import java.lang.reflect.Type;

public interface PropertyDescriptor<T>
{
  String getIndexKey();

  String getPropertyName();

  Type getType();

  PropertyCoercer<T> getCoercer();

  PropertyGetter<T> getGetter();

  PropertySetter<T> getSetter();
}
