/**
 * <p>Provides string-indexed property access for Java objects.</p>
 * 
 * <h2>Overview</h2>
 * <p>The {@code Indexable} contract enables POJOs to support both:</p>
 * <ul>
 *   <li>Type-safe access to known properties via descriptors</li>
 *   <li>Dynamic access to unknown/extra properties via string keys</li>
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
 *   <li>{@link kaphein.indexable.IndexableComplient} - Marker annotation.</li>
 *   <li>{@link kaphein.indexable.Indexable} - Main contract interface.</li>
 *   <li>{@link kaphein.indexable.PropertyDescriptor} - Property metadata.</li>
 *   <li>{@link kaphein.indexable.MapBackedObject} - A reference implementation.</li>
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
 *     final Supplier&lt;? extends Indexable&gt; selfSupplier,
 *     final Supplier&lt;Map&lt;String, Object&gt;&gt; mapSupplier,
 *     final Collection&lt;? extends PropertyDescriptor&lt;?&gt;&gt; descs,
 *     final Collection&lt;? extends Map.Entry&lt;? extends String, ? extends Object&gt;&gt; entries
 *   )
 *   {
 *     super(
 *       selfSupplier,
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
 * </pre>
 * 
 * @see kaphein.indexable.Indexable
 * @see kaphein.indexable.IndexableComplient
 */
package kaphein.indexable;
