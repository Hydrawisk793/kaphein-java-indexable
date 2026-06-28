package kaphein.indexable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import kaphein.indexable.internal.AssertArg;

class MapBackedObjectMapView implements Map<String, Object>
{
  private static class Values
    extends MapBackedCollectionView<String, Object, MapBackedObjectMapView, Object>
  {
    private static class ValueIterator implements Iterator<Object>
    {
      private final Values values;

      private final Iterator<String> keyIter;

      private ValueIterator(final Values values)
      {
        this.values = AssertArg.isNotNull(values, "values");
        this.keyIter = values.getMap().keySet().iterator();
      }

      @Override
      public boolean hasNext()
      {
        return keyIter.hasNext();
      }

      @Override
      public Object next()
      {
        return values.getMap().get(keyIter.next());
      }

      @Override
      public void remove()
      {
        keyIter.remove();
      }
    }

    private Values(final MapBackedObjectMapView m)
    {
      super(m);
    }

    @Override
    public boolean contains(final Object o)
    {
      return getMap().containsValue(o);
    }

    @Override
    public boolean remove(final Object o)
    {
      boolean modified = false;

      final MapBackedObjectMapView map = getMap();
      final Iterator<String> keyIter = map.keySet().iterator();
      while(keyIter.hasNext())
      {
        final String k = keyIter.next();
        final Object value = map.get(k);
        if(Objects.equals(value, o))
        {
          try
          {
            keyIter.remove();
            modified = true;
            break;
          }
          catch(final UnsupportedOperationException uoe)
          {
            map.remove(k);
            modified = true;
            break;
          }
        }
      }

      return modified;
    }

    @Override
    public Iterator<Object> iterator()
    {
      return new ValueIterator(this);
    }
  }

  private static class EntrySet
    extends MapBackedSetView<String, Object, MapBackedObjectMapView, Map.Entry<String, Object>>
  {
    private static class EntryIterator implements Iterator<Map.Entry<String, Object>>
    {
      private final MapBackedObjectMapView map;

      private final Iterator<String> keyIter;

      private EntryIterator(final MapBackedObjectMapView map)
      {
        this.map = AssertArg.isNotNull(map, "map");
        this.keyIter = map.keySet().iterator();
      }

      @Override
      public boolean hasNext()
      {
        return keyIter.hasNext();
      }

      @Override
      public Map.Entry<String, Object> next()
      {
        return new MapEntryView<>(map, keyIter.next());
      }

      @Override
      public void remove()
      {
        keyIter.remove();
      }
    }

    private EntrySet(final MapBackedObjectMapView map)
    {
      super(map);
    }

    @Override
    public Iterator<Map.Entry<String, Object>> iterator()
    {
      return new EntryIterator(getMap());
    }

    @Override
    public boolean contains(final Object o)
    {
      boolean result = (o instanceof Map.Entry<?, ?>);
      if(result)
      {
        final MapBackedObjectMapView map = getMap();
        final Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
        final Object key = entry.getKey();

        result = (map.containsKey(key)
          && Objects.equals(map.get(key), entry.getValue()));
      }

      return result;
    }

    @Override
    public boolean remove(final Object o)
    {
      boolean modified = false;

      if((o instanceof Map.Entry<?, ?>))
      {
        final Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
        modified = getMap().remove(entry.getKey(), entry.getValue());
      }

      return modified;
    }
  }

  private final AbstractMapBackedObject owner;

  private final AtomicReference<Values> valuesRef;

  private final AtomicReference<EntrySet> entrySetRef;

  MapBackedObjectMapView(final AbstractMapBackedObject owner)
  {
    this.owner = AssertArg.isNotNull(owner, "owner");
    this.valuesRef = new AtomicReference<>();
    this.entrySetRef = new AtomicReference<>();
  }

  @Override
  public int size()
  {
    return owner.getPropMap().size();
  }

  @Override
  public boolean isEmpty()
  {
    return owner.isEmpty();
  }

  @Override
  public boolean containsKey(final Object key)
  {
    return owner.containsKey((String)key);
  }

  @Override
  public boolean containsValue(final Object value)
  {
    boolean result = false;
    for(final Iterator<String> keyIter = owner.keys().iterator(); !result && keyIter.hasNext();)
    {
      result = Objects.equals(value, owner.get(keyIter.next()));
    }

    return result;
  }

  @Override
  public Object get(final Object key)
  {
    return owner.get((String)key);
  }

  @Override
  public Object put(final String key, final Object value)
  {
    final Object oldValue = owner.get(key);

    owner.put(key, value);

    return oldValue;
  }

  @Override
  public Object remove(final Object key)
  {
    return owner.remove((String)key);
  }

  @Override
  public void putAll(final Map<? extends String, ? extends Object> m)
  {
    owner.putAll(m);
  }

  @Override
  public void clear()
  {
    owner.clear();
  }

  @Override
  public Set<String> keySet()
  {
    return owner.getPropMap().keySet();
  }

  @Override
  public Collection<Object> values()
  {
    return valuesRef.getAndUpdate(this::createOrGetValues);
  }

  @Override
  public Set<Entry<String, Object>> entrySet()
  {
    return entrySetRef.getAndUpdate(this::createOrGetEntrySet);
  }

  @Override
  public boolean equals(final Object o)
  {
    boolean result = this == o;
    if(!result)
    {
      if((o instanceof Map<?, ?>))
      {
        final Map<?, ?> other = (Map<?, ?>)o;
        result = (size() == other.size());
        if(result)
        {
          try
          {
            for(final Map.Entry<String, Object> entry : entrySet())
            {
              final String key = entry.getKey();
              final Object otherValue = other.get(key);
              final Object value = entry.getValue();
              if(null == value)
              {
                result = null == otherValue && other.containsKey(key);
              }
              else
              {
                result = Objects.equals(value, otherValue);
              }

              if(!result)
              {
                break;
              }
            }
          }
          catch(ClassCastException | NullPointerException e)
          {
            // Ignore the exception because they are not equal anymore.

            result = false;
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
    for(final Map.Entry<String, Object> entry : entrySet())
    {
      hash += entry.hashCode();
    }

    return hash;
  }

  private Values createOrGetValues(
    final Values mapView
  )
  {
    return ((null == mapView) ? new Values(this) : mapView);
  }

  private EntrySet createOrGetEntrySet(
    final EntrySet mapView
  )
  {
    return ((null == mapView) ? new EntrySet(this) : mapView);
  }
}
