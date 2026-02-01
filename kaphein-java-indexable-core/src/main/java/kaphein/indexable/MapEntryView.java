package kaphein.indexable;

import java.util.Map;
import java.util.Objects;

import kaphein.indexable.internal.AssertArg;

class MapEntryView<K, V> implements Map.Entry<K, V>
{
  private final Map<K, V> map;

  private final K key;

  MapEntryView(
    final Map<K, V> map,
    final K key
  )
  {
    this.map = AssertArg.isNotNull(map, "map");
    this.key = key;
  }

  @Override
  public K getKey()
  {
    if(!map.containsKey(key))
    {
      throw new IllegalStateException("The entry is detached from the map.");
    }

    return key;
  }

  @Override
  public V getValue()
  {
    if(!map.containsKey(key))
    {
      throw new IllegalStateException("The entry is detached from the map.");
    }

    return map.get(key);
  }

  @Override
  public V setValue(final V value)
  {
    if(!map.containsKey(key))
    {
      throw new IllegalStateException("The entry is detached from the map.");
    }

    return map.replace(key, value);
  }

  @Override
  public boolean equals(final Object o)
  {
    boolean result = this == o;
    if(!result)
    {
      if((o instanceof Map.Entry<?, ?>))
      {
        final Map.Entry<?, ?> other = (Map.Entry<?, ?>)o;

        result = Objects.equals(getKey(), other.getKey())
          && Objects.equals(getValue(), other.getValue());
      }
    }

    return result;
  }

  @Override
  public int hashCode()
  {
    return (Objects.hashCode(getKey())
      ^ Objects.hashCode(getValue()));
  }
}
