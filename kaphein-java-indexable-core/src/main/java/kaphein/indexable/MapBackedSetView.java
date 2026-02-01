package kaphein.indexable;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

abstract class MapBackedSetView<K, V, M extends Map<K, V>, I>
  extends MapBackedCollectionView<K, V, M, I>
  implements Set<I>
{
  protected MapBackedSetView(final M map)
  {
    super(map);
  }

  @Override
  public boolean equals(final Object o)
  {
    boolean result = this == o;
    if(!result)
    {
      if((o instanceof Set<?>))
      {
        final Set<?> other = (Set<?>)o;
        if((size() == other.size()))
        {
          try
          {
            result = containsAll(other);
          }
          catch(ClassCastException | NullPointerException e)
          {
            // Ignore the exception because they are not equal anymore.
          }
        }
      }
    }

    return result;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    final Iterator<I> iter = iterator();
    while(iter.hasNext())
    {
      final I item = iter.next();
      hash += (null == item ? 0 : item.hashCode());
    }

    return hash;
  }
}
