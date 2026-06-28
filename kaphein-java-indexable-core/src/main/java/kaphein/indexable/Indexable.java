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
 *  <p>The core interface for the {@code Indexable} contract to make implementations comply with the the contract.</p>
 *
 *  <p>This interface defines essential operations for accessing object properties by string keys,
 *  with additional type-safe access via {@link #getByDescriptor(PropertyDescriptor)}.</p>
 *
 *  <p>Implementations <b>MUST</b> manually write {@code PropertyDescriptors} static inner class holding known properties
 *  because Java language does not support static method contracts via interfaces.</p>
 *
 *  <p>See the {@link kaphein.indexable} package documentation for
 *  detailed contract explanation and usage examples.</p>
 *
 *  @see kahpein.indexable
 *  @see PropertyDescriptor
 */
public interface Indexable
{
  /**
   *  <p>Returns {@code true} if this {@link Indexable} contains no property mappings.</p>
   *
   *  @return {@code true} if this {@link Indexable} contains no property mappings.
   */
  boolean isEmpty();

  /**
   *  <p>Returns {@code true} if this {@link Indexable} contains a mapping for the specified key.</p>
   *
   *  <p>Returns {@code true} if and only if this {@link Indexable} contains a mapping for a key {@code k}
   *  such that {@code (null == key ? null == k : key.equals(k))}.
   *  (There can be at most one such mapping.)</p>
   *
   *  @param key A key whose presence in this {@link Indexable} is to be tested.
   *  @throws NullPointerException If the specified key is {@code null} and this {@link Indexable} does not permit {@code null} keys.
   */
  boolean containsKey(String key);

  /**
   *  <p>Returns the value to which the specified key is mapped,
   *  or {@code null} if this {@link Indexable} contains no mapping for the key.</p>
   *
   *  <p>Implementations <b>MUST</b> look up the {@link PropertyDescriptor} associated by the specified {@code key} first.
   *  If such {@link PropertyDescriptor} exists, the {@link PropertyDescriptor} <b>MUST</b> be used to get actual property value of the mapping.
   *  If no such {@link PropertyDescriptor} exists, the stored value of the mapping is returned.</p>
   *
   *  <p>If this {@link Indexable} contains a mapping from a key {@code k} to a value {@code v}
   *  such that {@code (null == key ? null == k : key.equals(k))},
   *  then this method returns {@code v}; otherwise it returns {@code null}.
   *  (There can be at most one such mapping.)</p>
   *
   *  <p>If this {@link Indexable} permits {@code null} values, 
   *  then a return value of {@code null} does not <i>necessarily</i> indicate that 
   *  the {@link Indexable} contains no mapping for the key; 
   *  it's also possible that the {@link Indexable} explicitly maps the key to {@code null}.
   *  The {@link #containsKey containsKey} operation may be used to distinguish these two cases.</p>
   *
   *  @param key The key whose associated value is to be returned.
   *  @return The value to which the specified key is mapped, 
   *  or {@code null} if this {@link Indexable} contains no mapping for the key.
   *  @throws NullPointerException If the specified key is {@code null} and this {@link Indexable} does not permit {@code null} keys.
   */
  Object get(String key);

  /**
   *  <p>Returns the value to which the specified key is mapped,
   *  or {@code defaultValue} if this {@link Indexable} contains no mapping for the key.</p>
   *
   *  @param key The key whose associated value is to be returned.
   *  @param defaultValue The default mapping of the key.
   *  @return the value to which the specified key is mapped,
   *  or {@code defaultValue} if this {@link Indexable} contains no mapping for the key.
   *  @throws NullPointerException If the specified key is {@code null} and this {@link Indexable} does not permit {@code null} keys.
   */
  default Object getOrDefault(
    final String key,
    final Object defaultValue
  )
  {
    final Object value = get(key);

    return (null == value && containsKey(key) ? defaultValue : value);
  }

  /**
   *  <p>Returns the value of the property described by the specified {@link PropertyDescriptor}.</p>
   *
   *  <p>Implemenations MAY prohibit {@link PropertyDescriptor}s that is not declared in the {@code PropertyDescriptors} static inner class,
   *  but it is recommended to allow any {@link PropertyDescriptor}s for flexibility.</p>
   *
   *  <p>If this {@link Indexable} contains a mapping from a key {@code k} to a value {@code v}
   *  such that {@code (null == key ? null == k : key.equals(k))},
   *  then this method returns {@code v}; otherwise it returns {@code null}.
   *  (There can be at most one such mapping.)</p>
   *
   *  <p>If this {@link Indexable} permits {@code null} values, 
   *  then a return value of {@code null} does not <i>necessarily</i> indicate that 
   *  the {@link Indexable} contains no mapping for the key; 
   *  it's also possible that the {@link Indexable} explicitly maps the key to {@code null}.
   *  The {@link #containsKey containsKey} operation may be used to distinguish these two cases.</p>
   *
   *  @param <T> The type of the property.
   *  @param desc The {@link PropertyDescriptor} of the property to read. 
   *  @return The value of the property described by the specified {@link PropertyDescriptor},
   *  or {@code null} if this {@link Indexable} contains no mapping for the key.
   *  @throws IllegalArgumentException If the specified {@code desc} is {@code null}.
   */
  <T> T getByDescriptor(PropertyDescriptor<T> desc);

  /**
   *  <p>Returns an {@link Iterable} of extra property mappings of this {@link Indexable}.</p>
   *
   *  <p>Extra property mappings are key-value mappings of properties that is not declared in the {@code PropertyDescriptors} static inner class.</p>
   *
   *  @return An {@link Iterable} of extra property mappings of this {@link Indexable}.
   */
  Iterable<Map.Entry<String, Object>> getExtraProperties();

  /**
   *  <p>Associates the specified value with the specified key in this {@link Indexable}. (optional operation)</p>
   *
   *  <p>If the {@link Indexable} previously contained a mapping for the key,
   *  the old value is replaced by the specified value.
   *  (An {@link Indexable} {@code x} is said to contain a mapping for a key {@code k}
   *  if and only if {@link #containsKey(Object) x.containsKey(k)} would return {@code true}.)</p>
   *
   *  @param key A key with which the specified value is to be associated.
   *  @param value A value to be associated with the specified key.
   *  @throws UnsupportedOperationException If the {@code put} operation is not supported by this {@link Indexable}.
   *  @throws NullPointerException If the specified key or value is {@code null} and this {@link Indexable} does not permit {@code null} keys or values.
   *  @throws IllegalArgumentException If some property of the specified key or value prevents it from being stored in this {@link Indexable}.
   */
  default void put(
    final String key,
    final Object value
  )
  {
    throw new UnsupportedOperationException("'put' is not supported.");
  }

  /**
   *  <p>Copies all of the mappings from the specified map to this {@link Indexable}. (optional operation)</p>
   * 
   *  <p>The effect of this call is equivalent to that of calling {@link #put(Object,Object) put(k, v)} on this {@link Indexable}
   *  once for each mapping from key {@code k} to value {@code v} in the specified map.
   *  The behavior of this operation is undefined if the specified map is modified while the operation is in progress.</p>
   *
   *  @param m Mappings to be stored in this {@link Indexable}.
   *  @throws UnsupportedOperationException If the {@code putAll} operation is not supported by this {@link Indexable}.
   *  @throws NullPointerException If this {@link Indexable} does not permit {@code null} keys or values,
   *  and the specified map contains {@code null} keys or values.
   *  @throws IllegalArgumentException If the specified map is {@code null}
   *  or if some property of a key or value in the specified map prevents it from being stored in this {@link Indexable}.
   */
  default void putAll(final Map<? extends String, ? extends Object> m)
  {
    putAll(m.entrySet());
  }

  /**
   *  <p>Copies all of the specified mappings to this {@link Indexable}. (optional operation)</p>
   * 
   *  <p>The effect of this call is equivalent to that of calling {@link #put(Object,Object) put(k, v)} on this {@link Indexable}
   *  once for each mapping from key {@code k} to value {@code v} in the specified entries.
   *  The behavior of this operation is undefined if the specified {@code entries} is modified while the operation is in progress.</p>
   *
   *  @param entries A collection of mappings to be stored in this {@link Indexable}.
   *  @throws UnsupportedOperationException If the {@code putAll} operation is not supported by this {@link Indexable}.
   *  @throws NullPointerException If this {@link Indexable} does not permit {@code null} keys or values,
   *  and the mappings in the specified {@code entries} contain {@code null} keys or values.
   *  @throws IllegalArgumentException If the specified {@code entries} is {@code null}
   *  or if some property of a key or value in the mappings prevents it from being stored in this {@link Indexable}.
   */
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

  /**
   *  <p>Removes the mapping for a key from this {@link Indexable} if it is present. (optional operation)</p>
   * 
   *  <p>If this {@link Indexable} contains a mapping from key {@code k} to value {@code v}
   *  such that {@code (null == key ? null == k : key.equals(k))},
   *  that mapping is removed. (The {@link Indexable} can contain at most one such mapping.)</p>
   *
   *  <p>Returns the value to which this {@link Indexable} previously associated the key,
   *  or {@code null} if the {@link Indexable} contained no mapping for the key.</p>
   *
   *  <p>If this {@link Indexable} permits {@code null} values,
   *  then a return value of {@code null} does not <i>necessarily</i> indicate that
   *  the {@link Indexable} contained no mapping for the key;
   *  it's also possible that the {@link Indexable} explicitly mapped the key to {@code null}.</p>
   *
   *  <p>The {@link Indexable} will not contain a mapping for the specified key once the call returns.</p>
   *
   *  @param key A key whose mapping is to be removed from the {@link Indexable}.
   *  @throws UnsupportedOperationException If the {@code remove} operation is not supported by this {@link Indexable}.
   *  @throws NullPointerException If the specified key or value is {@code null} and this {@link Indexable} does not permit {@code null} keys or values.
   */
  default Object remove(final String key)
  {
    throw new UnsupportedOperationException("'remove' is not supported.");
  }

  /**
   *  <p>Removes all of the mappings from this {@link Indexable}. (optional operation)</p>
   *
   *  <p>The {@link Indexable} will be empty after this call returns.</p>
   *
   *  @throws UnsupportedOperationException If the {@code clear} operation is not supported by this {@link Indexable}.
   */
  default void clear()
  {
    throw new UnsupportedOperationException("'clear' is not supported.");
  }

  /**
   *  <p>Returns an {@link Iterable} view of the keys contained in this {@link Indexable}.</p>
   *
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
   *
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

  /**
   *  <p>Performs the given action for each entry in this {@link Indexable} until all entries have been processed or the action throws an exception.</p>
   *
   *  <p>Exceptions thrown by the action are relayed to the caller.</p>
   *
   *  @param action The action to be performed for each entry.
   *  @throws IllegalArgumentException If the specified {@code action} is {@code null}.
   *  @throws ConcurrentModificationException If an entry is found to be removed during iteration.
   */
  default void forEach(final BiConsumer<? super String, ? super Object> action)
  {
    AssertArg.isNotNull(action, "action");

    String key = null;
    Object value = null;
    for(
      final Iterator<Map.Entry<String, Object>> iter = entries().iterator(); iter.hasNext(); action.accept(key,
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

  /**
   *  <p>Returns a copy of this {@link Indexable} with the specified {@code entries} put into it. (optional operation)</p>
   *
   *  <p>This operation does not modify the current {@link Indexable}, but creates a copy of it.
   *  If the specified {@code entries} is empty, the copy has exactly the same property mappings as this {@link Indexable}.</p>
   *
   *  @param entries A collection of entries to put into this {@link Indexable}.
   *  @return A copy of this {@link Indexable} with the specified {@code entries} put into it.
   *  @throws UnsupportedOperationException If the {@code withEntries} operation is not supported by this {@link Indexable}.
   *  @throws IllegalArgumentException If the specified {@code entries} is {@code null}.
   */
  default Indexable withEntries(
    final Collection<? extends Map.Entry<? extends String, ? extends Object>> entries
  )
  {
    throw new UnsupportedOperationException("'withEntries' is not supported.");
  }

  /**
   *  Converts this {@link Indexable} to a {@link java.util.Map}.
   *
   *  @return An {@link java.util.Map} instance with all property mappings.
   */
  default Map<String, Object> toMap()
  {
    return toMap(LinkedHashMap::new);
  }

  /**
   *  Converts this {@link Indexable} to a {@link java.util.Map}.
   *
   *  @param mapSupplier A supplier for {@link java.util.Map} instance where the property pairs returned in.
   *  @return An {@link java.util.Map} instance with all property mappings.
   */
  Map<String, Object> toMap(Supplier<Map<String, Object>> mapSupplier);
}
