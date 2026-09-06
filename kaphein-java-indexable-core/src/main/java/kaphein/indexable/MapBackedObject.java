package kaphein.indexable;

import java.util.Map;

/**
 *  <p>A sub interface for {@code java.util.Map}-backed implementations of {@link Indexable}.</p>
 */
public interface MapBackedObject extends Indexable
{
  /**
   * <p>Returns a {@link Map} accessor for the {@link MapBackedObject}.</p>
   * <p>Unlike {@link toMap} method, this does not create a copied {@link Map}.</p>
   * 
   * @return A {@link Map} accessor for the {@link MapBackedObject}.
   */
  Map<String, Object> asMap();
}
