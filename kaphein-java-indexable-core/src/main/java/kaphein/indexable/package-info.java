/**
 * Provides string-indexed property access for Java objects (C#-style indexers).
 * 
 * <h2>Overview</h2>
 * <p>The {@code Indexable} contract enables POJOs to support both:
 * <ul>
 *   <li>Type-safe access to known properties via descriptors</li>
 *   <li>Dynamic access to unknown/extra properties via string keys</li>
 * </ul>
 * 
 * <h2>Inspiration</h2>
 * <p>This contract is inspired by C#'s indexer feature, which allows custom
 * classes to be accessed using bracket notation: {@code obj["propertyName"]}.
 * In Java, this becomes: {@code obj.get("propertyName")} or the type-safe
 * {@code obj.getByDescriptor(descriptor)}.
 * 
 * <h2>Primary Use Case: Schema Evolution</h2>
 * <p>OpenAPI systems often add new properties at runtime without notice.
 * The {@code Indexable} contract allows POJOs to:
 * <ul>
 *   <li>Accept and store unknown properties without errors</li>
 *   <li>Provide type-safe access to known properties</li>
 *   <li>Support non-Java-identifier property names (e.g., URIs)</li>
 * </ul>
 * 
 * <h2>Core Components</h2>
 * <ul>
 *   <li>{@link kaphein.indexable.Indexable} - Main contract interface</li>
 *   <li>{@link kaphein.indexable.IndexableComplient} - Marker annotation</li>
 *   <li>{@link kaphein.indexable.PropertyDescriptor} - Property metadata</li>
 *   <li>{@link kaphein.indexable.MapBackedObject} - Reference implementation</li>
 * </ul>
 * 
 * <h2>Example Usage</h2>
 * <pre>{@code
 * @IndexableComplient
 * public class Person extends MapBackedObject {
 *     public static class PropertyDescriptors {
 *         public static final IndexablePropertyDescriptor<String> NAME =
 *             IndexablePropertyDescriptorFactories.createString("name", "name");
 *     }
 *     
 *     public String getName() {
 *         return getByDescriptor(PropertyDescriptors.NAME);
 *     }
 * }
 * 
 * Person person = new Person();
 * person.put("name", "Alice");           // Known property
 * person.put("extraField", "value");     // Extra property - no error!
 * }</pre>
 * 
 * @see kaphein.indexable.Indexable
 * @see kaphein.indexable.IndexableComplient
 */
package kaphein.indexable;
