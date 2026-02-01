/**
 * <p>Provides string-indexed property access for Java objects.</p>
 * 
 * <h2>Overview</h2>
 * <p>The {@code Indexable} contract enables POJOs to support:</p>
 * <ul>
 *   <li>Type-safe access to known properties via descriptors.</li>
 *   <li>Dynamic access to unknown/extra properties via string keys.</li>
 *   <li>Non-Java identifier property keys like URIs.</li>
 * </ul>
 * 
 * <h2>Inspiration</h2>
 * <p>This contract is inspired by C#'s indexer feature, which allows custom
 * classes to be accessed using bracket notation: {@code obj["propertyName"]}.
 * In Java, this becomes: {@code obj.get("propertyName")} or the type-safe
 * {@code obj.getByDescriptor(descriptor)}.</p>
 * 
 * <h2>Primary Use Case: Schema Evolution</h2>
 * <p>OpenAPI systems often add new properties at runtime without notice.
 * The {@code Indexable} contract allows POJOs to:</p>
 * <ul>
 *   <li>Accept and store unknown properties without errors</li>
 *   <li>Provide type-safe access to known properties</li>
 *   <li>Support non-Java-identifier property names (e.g., URIs)</li>
 * </ul>
 * 
 * <h2>Core Components</h2>
 * <ul>
 *   <li>{@link IndexableComplient} - The marker annotation.</li>
 *   <li>{@link Indexable} - The main contract interface.</li>
 *   <li>{@link PropertyDescriptor} - Property metadata.</li>
 *   <li>{@link MapBackedObject} - A sub interface for {@code java.util.Map}-backed implementations.</li>
 *   <li>{@link MutableMapBackedObject} - A reference implementation for mutable ones.</li>
 *   <li>{@link ImmutableMapBackedObject} - A reference implementation for immutable ones.</li>
 * </ul>
 * 
 * <h2>Requirements</h2>
 * <p>Classes annotated with {@code @IndexableComplient} declare their intent
 * to follow the contract's requirements, including:</p>
 * <ul>
 *   <li>Getting readable properties by a string key.</li>
 *   <li>Setting writable properties by a string key.</li>
 *   <li>Providing a {@code PropertyDescriptors} inner class.</li>
 *   <li>Distinguishing between known and extra properties.</li>
 * </ul>
 * 
 * <h2>Thread safety</h2>
 * <p>Implementations are <b>NOT</b> need to be thread-safe. 
 * Using immutable variants is recommended for thread safety requirements.</p>
 * 
 * <h2>How to follow the contract</h2>
 * <p>There are several ways to follow this contract:</p>
 * <ul>
 *  <li>Annotate target classes with {@link IndexableComplient} and manually write the essential components in the class defintion.</li>
 *  <li>Implement {@link Indexable} interface and manually write the essential components in the class defintion.</li>
 *  <li>Extend {@link MabBackedObject} class and manually write the essential components in the class defintion.</li>
 * </ul>
 * 
 * <h2>Example Usage</h2>
 * <pre>
 * &#64;IndexableComplient
 * public class Person extends MapBackedObject
 * {
 *   public static class PropertyDescriptors extends MapBackedObject.PropertyDescriptors
 *   {
 *     public static final PropertyDescriptor&lt;String&gt; NAME = PropertyDescriptorFactories.createString(
 *       "name");
 *
 *     private static final Map&lt;String, ? extends PropertyDescriptor&lt;?&gt;&gt; OWN_PROP_DESCS = PropertyDescriptorHelpers
 *       .ownDescriptors(
 *         NAME);
 *
 *     public static Map&lt;String, ? extends PropertyDescriptor&lt;?&gt;&gt; getOwnDescriptors()
 *     {
 *       return OWN_PROP_DESCS;
 *     }
 *
 *     private static final Map&lt;String, ? extends PropertyDescriptor&lt;?&gt;&gt; ALL_PROP_DESCS = PropertyDescriptorHelpers
 *       .allDescriptors(
 *         MapBackedObject.PropertyDescriptors.getAllDescriptors().values(),
 *         getOwnDescriptors().values());
 *
 *     public static Map&lt;String, ? extends PropertyDescriptor&lt;?&gt;&gt; getAllDescriptors()
 *     {
 *       return ALL_PROP_DESCS;
 *     }
 *   }
 *
 *   public Person()
 *   {
 *     this(Collections.emptyList());
 *   }
 *
 *   public Person(
 *     final Map&lt;? extends String, ? extends Object&gt; map
 *   )
 *   {
 *     this(map.entrySet());
 *   }
 *
 *   public Person(
 *     final Collection&lt;? extends Map.Entry&lt;? extends String, ? extends Object&gt;&gt; entries
 *   )
 *   {
 *     this(
 *       Person::new,
 *       LinkedHashMap::new,
 *       PropertyDescriptors.getAllDescriptors().values(),
 *       entries);
 *   }
 *
 *   protected Person(
 *     final Supplier&lt;? extends Indexable&gt; emptySupplier,
 *     final Supplier&lt;Map&lt;String, Object&gt;&gt; mapSupplier,
 *     final Collection&lt;? extends PropertyDescriptor&lt;?&gt;&gt; descs,
 *     final Collection&lt;? extends Map.Entry&lt;? extends String, ? extends Object&gt;&gt; entries
 *   )
 *   {
 *     super(
 *       emptySupplier,
 *       mapSupplier,
 *       descs,
 *       entries);
 *   }
 *
 *   public String getName()
 *   {
 *     return getByDescriptor(PropertyDescriptors.NAME);
 *   }
 *
 *   &#64;Override
 *   public Person withEntries(
 *     final Collection&lt;? extends Entry&lt;? extends String, ? extends Object&gt;&gt; entries
 *   )
 *   {
 *     return (Person)super.withEntries(entries);
 *   }
 * }
 * 
 * Person person = new Person();
 * person.put("name", "Alice");           // A known property,
 * person.put("extraField", "value");     // A extra property - no error.
 * </pre>
 * 
 * @see kaphein.indexable.Indexable
 * @see kaphein.indexable.IndexableComplient
 */
package kaphein.indexable;
