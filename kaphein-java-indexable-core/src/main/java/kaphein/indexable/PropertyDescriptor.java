package kaphein.indexable;

import java.lang.reflect.Type;

public interface PropertyDescriptor<T>
{
  /**
   *  <p>Returns the actual key for indexing the property.</p>
   *
   *  @return The actual key for indexing the property.
   */
  String getIndexKey();

  /**
   *  <p>Returns the Java property name of the property.</p>
   *
   *  @return The Java property name.
   */
  String getPropertyName();

  /**
   *  <p>Returns the type of the propety.</p>
   *  
   *  @return The type of the propety.
   */
  Type getType();

  /**
   *  <p>Returns the getter function of the propety.</p>
   *  
   *  @return The getter function of the propety.
   *  @throws UnsupportedOperationException If the property is write-only.
   */
  PropertyGetter<T> getGetter();

  /**
   *  <p>Returns the setter function of the propety.</p>
   *  
   *  @return The setter function of the propety.
   *  @throws UnsupportedOperationException If the property is read-only.
   */
  PropertySetter<T> getSetter();
}
