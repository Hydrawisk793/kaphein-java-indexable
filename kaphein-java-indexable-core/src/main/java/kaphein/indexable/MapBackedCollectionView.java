package kaphein.indexable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import kaphein.indexable.internal.AssertArg;

abstract class MapBackedCollectionView<K, V, M extends Map<K, V>, I> implements Collection<I>
{
  private final M map;

  protected MapBackedCollectionView(final M map)
  {
    this.map = AssertArg.isNotNull(map, "map");
  }

  @Override
  public boolean isEmpty()
  {
    return map.isEmpty();
  }

  @Override
  public int size()
  {
    return map.size();
  }

  @Override
  public boolean containsAll(final Collection<?> c)
  {
    boolean result = true;
    for(final Object other : c)
    {
      result = contains(other);
      if(!result)
      {
        break;
      }
    }

    return result;
  }

  @Override
  public boolean add(final I e)
  {
    throw new UnsupportedOperationException("'add' is not supported.");
  }

  @Override
  public boolean addAll(final Collection<? extends I> c)
  {
    throw new UnsupportedOperationException("'addAll' is not supported.");
  }

  @Override
  public boolean removeAll(final Collection<?> c)
  {
    boolean modified = false;

    for(final Object other : c)
    {
      modified |= remove(other);
    }

    return modified;
  }

  @Override
  public boolean retainAll(final Collection<?> c)
  {
    boolean modified = false;

    boolean iterRemoveNotSupported = false;
    final Iterator<I> iter = iterator();
    while(iter.hasNext())
    {
      final I item = iter.next();
      if(!c.contains(item))
      {
        try
        {
          iter.remove();
          modified = true;
        }
        catch(final UnsupportedOperationException uoe)
        {
          iterRemoveNotSupported = true;
          break;
        }
      }
    }

    if(iterRemoveNotSupported)
    {
      final ArrayList<I> targets = new ArrayList<>();
      for(final I item : this)
      {
        if(!c.contains(item))
        {
          targets.add(item);
        }
      }

      for(final I target : targets)
      {
        modified |= remove(target);
      }
    }

    return modified;
  }

  @Override
  public void clear()
  {
    map.clear();
  }

  @Override
  public Object[] toArray()
  {
    final Object[] result = new Object[map.size()];

    int index = 0;
    final Iterator<I> iter = iterator();
    while(iter.hasNext())
    {
      result[index++] = iter.next();
    }

    return result;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T[] toArray(final T[] a)
  {
    final int s = map.size();
    final T[] result = ((a.length >= s)
      ? a
      : (T[])java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), s));

    int index = 0;
    final Iterator<I> iter = iterator();
    while(iter.hasNext())
    {
      result[index++] = (T)iter.next();
    }

    if(index < result.length)
    {
      result[index] = null;
    }

    return result;
  }

  protected final M getMap()
  {
    return map;
  }
}
