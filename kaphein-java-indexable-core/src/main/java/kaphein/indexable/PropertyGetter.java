package kaphein.indexable;

import java.util.Map;

@FunctionalInterface
public interface PropertyGetter<T>
{
  T apply(
    PropertyDescriptor<T> desc,
    Map<String, Object> m
  );
}
