package kaphein.indexable;

import java.util.List;
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

  /**
   *  <p>Returns a {@link List} of extra property mappings of this {@link Indexable}.</p>
   *
   *  <p>Extra property mappings are key-value mappings of properties that is not declared in the {@code PropertyDescriptors} static inner class.</p>
   *
   *  @return A {@link List} of extra property mappings of this {@link Indexable}.
   */
  @Override
  List<Map.Entry<String, Object>> getExtraProperties();
}
