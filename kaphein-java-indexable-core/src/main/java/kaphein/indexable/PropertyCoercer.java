package kaphein.indexable;

public interface PropertyCoercer<T>
{
  T coerce(Object v);
}
