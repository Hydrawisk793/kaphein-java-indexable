package kaphein.indexable;

import kaphein.indexable.internal.AssertArg;

@FunctionalInterface
public interface PropertySetter<T>
{
  public static class Context<T>
  {
    private final PropertyDescriptor<T> desc;

    private final Indexable target;

    private final Object value;

    public Context(
      final PropertyDescriptor<T> desc,
      final Indexable target,
      final Object value
    )
    {
      this.desc = AssertArg.isNotNull(desc, "desc");
      this.target = AssertArg.isNotNull(target, "target");
      this.value = value;
    }

    public PropertyDescriptor<T> getDescriptor()
    {
      return desc;
    }

    public Indexable getTarget()
    {
      return target;
    }

    public Object getValue()
    {
      return value;
    }
  }

  Object apply(PropertySetter.Context<T> ctx);
}
