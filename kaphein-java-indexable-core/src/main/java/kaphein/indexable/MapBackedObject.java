package kaphein.indexable;

import java.util.Collection;
import java.util.Map;

/**
 *  <p>A sub interface of {@link Indexable} that represents its implementations backed by {@link java.util.Map}.</p>
 */
public interface MapBackedObject extends Indexable
{
  boolean containsValue(Object value);

  Collection<Object> values();

  /**
   *  <p>Compares the specified object with this entry for equality.</p>
   *  <p>The comparison rules are same as the comparison rules of {@link java.util.Map}.</p>
   *
   *  @param o An object to be compared.
   *  @return {@code true} if the specified object is equal to this {@link MapBackedObject}.
   *  @see java.util.Map#equals
   */
  @Override
  boolean equals(Object o);

  /**
   *  <p>Returns the hash code value for this {@link MapBackedObject}.</p>
   *  <p>The generation rules are are same as the generation rules of {@link java.util.Map}.</p>
   *
   *  @return The hash code value.
   *  @see java.util.Map#hashCode
   */
  @Override
  int hashCode();

  /**
   *  <p>Returns a {@link Map} accessor for the {@link MapBackedObject}.</p>
   * 
   *  <p>Unlike {@link MapBackedObject#toMap} method, this does not create a copied {@link Map}.</p>
   * 
   *  @return A {@link Map} accessor for the {@link MapBackedObject}.
   */
  Map<String, Object> asMap();
}
