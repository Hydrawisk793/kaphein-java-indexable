package kaphein.indexable;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import kaphein.indexable.internal.AssertArg;

/**
 * <p>The core interface for the {@code Indexable} contract, providing essential
 * method definitions for string-indexed property access.</p>
 * 
 * <p>This interface defines {@link java.util.Map}-like operations for accessing object properties
 * by string keys, with additional type-safe access via
 * {@link #getByDescriptor(PropertyDescriptor)}.</p>
 * 
 * <p>Implementations must support:</p>
 * <ul>
 *   <li>Getting/setting properties by string key</li>
 *   <li>Type-safe access via property descriptors</li>
 *   <li>Distinguishing between known and extra properties</li>
 * </ul>
 * 
 * <p>Implementations are <b>NOT</b> need to be thread-safe. 
 * Using immutable variants is recommended for thread safety requirements.</p>
 * 
 * <p>See the {@linkplain kaphein.indexable package documentation} for
 * detailed contract explanation and usage examples.</p>
 * 
 * @see IndexableComplient
 * @see PropertyDescriptor
 * @see MapBackedObject
 */
public interface Indexable
{
  int size();

  boolean isEmpty();

  boolean containsKey(Object key);

  boolean containsValue(Object value);

  Object get(Object key);

  default Object getOrDefault(
    final Object key,
    final Object defaultValue
  )
  {
    final Object value = get(key);

    return (null == value && containsKey(key) ? defaultValue : value);
  }

  <T> T getByDescriptor(PropertyDescriptor<T> desc);

  Map<String, Object> getExtraProperties();

  default Object put(
    final String key,
    final Object value
  )
  {
    throw new UnsupportedOperationException("'put' is not supported.");
  }

  default void putAll(final Map<? extends String, ? extends Object> m)
  {
    putAll(m.entrySet());
  }

  default void putAll(
    final Collection<? extends Map.Entry<? extends String, ? extends Object>> entries
  )
  {
    AssertArg.isNotNull(entries, "entries");

    for(final Map.Entry<? extends String, ? extends Object> entry : entries)
    {
      put(entry.getKey(), entry.getValue());
    }
  }

  default Object remove(final Object key)
  {
    throw new UnsupportedOperationException("'remove' is not supported.");
  }

  default void clear()
  {
    throw new UnsupportedOperationException("'clear' is not supported.");
  }

  Set<String> keySet();

  Collection<Object> values();

  Set<Map.Entry<String, Object>> entrySet();

  default void forEach(final BiConsumer<? super String, ? super Object> consumer)
  {
    AssertArg.isNotNull(consumer, "consumer");

    String key = null;
    Object value = null;
    for(
      final Iterator<Map.Entry<String, Object>> iter = entrySet().iterator(); iter.hasNext(); consumer.accept(key,
        value)
    )
    {
      final Map.Entry<String, Object> entry = iter.next();

      try
      {
        key = entry.getKey();
        value = entry.getValue();
      }
      catch(final IllegalStateException ise)
      {
        throw new ConcurrentModificationException(ise);
      }
    }
  }

  Indexable withEntries(
    Collection<? extends Map.Entry<? extends String, ? extends Object>> entries
  );

  default Map<String, Object> toMap()
  {
    return toMap(LinkedHashMap::new);
  }

  Map<String, Object> toMap(Supplier<Map<String, Object>> mapSupplier);
}
