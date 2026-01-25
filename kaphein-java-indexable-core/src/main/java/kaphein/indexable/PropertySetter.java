package kaphein.indexable;

import java.util.Map;

@FunctionalInterface
public interface PropertySetter<T>
{
  void apply(
    PropertyDescriptor<T> desc,
    Map<String, Object> m,
    Object v
  );
}
