package kaphein.indexable;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import kaphein.indexable.internal.AssertArg;

/**
 * <p>
 * The core interface for the {@code Indexable} contract to make target class
 * {@link IndexableComplient}.
 * </p>
 * 
 * <p>
 * This interface defines essential operations for accessing object properties
 * by string keys,
 * with additional type-safe access via
 * {@link #getByDescriptor(PropertyDescriptor)}.
 * </p>
 * 
 * <p>
 * See the {@link kaphein.indexable} package documentation for
 * detailed contract explanation and usage examples.
 * </p>
 * 
 * @see kahpein.indexable
 * @see IndexableComplient
 * @see PropertyDescriptor
 */
public interface Indexable
{
  boolean isEmpty();

  boolean containsKey(Object key);

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

  /**
   *  <p>Returns an {@link Iterable} view of the keys contained in this {@link Indexable}.</p>
   *  <p>The iterable is backed by the indexable, so changes to the indexable are reflected in the iterable, and vice-versa.
   *  If the indexable is modified while an iteration over the iterable is in progress 
   *  (except through the iterator's own {@link Iterator#remove} operation), 
   *  the results of the iteration are undefined.
   *  The iterable supports element removal, which removes the corresponding mapping from the indexable, 
   *  via the {@link Iterator#remove} operation.
   *  It <b>DOES NOT</b> support adding elements into the indexable.</p>
   *
   *  @return An {@link Iterable} view of the keys contained in this {@link Indexable}.
   */
  Iterable<String> keys();

  /**
   *  <p>Returns an {@link Iterable} view of the mappings contained in this {@link Indexable}.</p>
   *  <p>The iterable is backed by the indexable, so changes to the indexable are reflected in the iterable, and vice-versa.
   *  If the indexable is modified while an iteration over the iterable is in progress 
   *  (except through the iterator's own {@link Iterator#remove} operation, 
   *  or through the {@link Map.Entry#setValue} operation on a mapping entry returned by the iterator), 
   *  the results of the iteration are undefined.
   *  The iterable supports element removal, which removes the corresponding mapping from the indexable, 
   *  via the {@link Iterator#remove} operation.
   *  It <b>DOES NOT</b> support adding elements into the indexable.</p>
   * 
   *  @return An {@link Iterable} view of the mappings contained in this {@link Indexable}.
   */
  Iterable<Map.Entry<String, Object>> entries();

  default void forEach(final BiConsumer<? super String, ? super Object> consumer)
  {
    AssertArg.isNotNull(consumer, "consumer");

    String key = null;
    Object value = null;
    for(
      final Iterator<Map.Entry<String, Object>> iter = entries().iterator(); iter.hasNext(); consumer.accept(key,
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

  default Indexable withEntries(
    final Collection<? extends Map.Entry<? extends String, ? extends Object>> entries
  )
  {
    throw new UnsupportedOperationException("'withEntries' is not supported.");
  }

  /**
   * Converts this {@link Indexable} to a {@link java.util.Map} instance.
   *
   * @return An {@link java.util.Map} instance with all property pairs.
   */
  default Map<String, Object> toMap()
  {
    return toMap(LinkedHashMap::new);
  }

  /**
   * Converts this {@link Indexable} to a {@link java.util.Map} instance.
   *
   * @param mapSupplier A supplier for {@link java.util.Map} instance where the
   *                    property pairs returned in.
   * @return An {@link java.util.Map} instance with all property pairs.
   */
  Map<String, Object> toMap(Supplier<Map<String, Object>> mapSupplier);
}
