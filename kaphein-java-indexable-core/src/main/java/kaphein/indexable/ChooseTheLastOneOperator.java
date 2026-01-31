package kaphein.indexable;

import java.util.function.BinaryOperator;

/**
 *  A function that always returns the second (the last) argument.
 */
class ChooseTheLastOneOperator<T> implements BinaryOperator<T>
{
  private static final ChooseTheLastOneOperator<Object> INSTANCE = new ChooseTheLastOneOperator<>();

  @SuppressWarnings("unchecked")
  public static final <T> ChooseTheLastOneOperator<T> getInstance()
  {
    return (ChooseTheLastOneOperator<T>)INSTANCE;
  }

  @Override
  public T apply(final T l, final T r)
  {
    return r;
  }
}
