package kaphein.indexable.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class MapFactories
{
  private MapFactories()
  {
    // Empty.
  }

  @SafeVarargs
  public static <K, V> Map<K, V> fromEntries(
    final Map.Entry<? extends K, ? extends V>... entries
  )
  {
    return fromEntries(HashMap::new, entries);
  }

  @SafeVarargs
  public static <K, V> Map<K, V> fromOrderedEntries(
    final Map.Entry<? extends K, ? extends V>... entries
  )
  {
    return fromEntries(LinkedHashMap::new, entries);
  }

  @SafeVarargs
  public static <K, V> Map<K, V> fromEntries(
    final Supplier<Map<K, V>> mapSupplier,
    final Map.Entry<? extends K, ? extends V>... entries
  )
  {
    AssertArg.isNotNull(mapSupplier, "mapSupplier");
    AssertArg.isNotNull(entries, "entries");

    final Map<K, V> m = mapSupplier.get();
    for(int n = entries.length, i = 0; i < n; ++i)
    {
      final Map.Entry<? extends K, ? extends V> entry = entries[i];
      m.put(entry.getKey(), entry.getValue());
    }

    return m;
  }

  public static <K, V> Map<K, V> toUnmodifiableMap(
    final Map<K, V> src
  )
  {
    return (null == src
      ? Collections.emptyMap()
      : Collections.unmodifiableMap(new HashMap<>(src)));
  }
}
