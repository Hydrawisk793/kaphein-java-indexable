package kaphein.indexable;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

/**
 * <p>A mutable, map-backed implementation of {@link Indexable}.</p>
 * 
 * <p>This class is <b>NOT</b> thread-safe.
 * External synchronization is required for concurrent access.</p>
 * 
 * @see ImmutableMapBackedObject
 */
@IndexableComplient
public class MapBackedObject extends AbstractMapBackedObject
{
  public static class PropertyDescriptors extends AbstractMapBackedObject.PropertyDescriptors
  {
    private static final Map<String, ? extends PropertyDescriptor<?>> OWN_DESC_MAP = PropertyDescriptorHelpers
      .ownDescriptors(Collections.emptyList());

    public static Map<String, ? extends PropertyDescriptor<?>> getOwnDescriptors()
    {
      return OWN_DESC_MAP;
    }

    private static final Map<String, ? extends PropertyDescriptor<?>> ALL_DESC_MAP = PropertyDescriptorHelpers
      .allDescriptors(
        AbstractMapBackedObject.PropertyDescriptors.getAllDescriptors().values(),
        getOwnDescriptors().values());

    public static Map<String, ? extends PropertyDescriptor<?>> getAllDescriptors()
    {
      return ALL_DESC_MAP;
    }
  }

  public MapBackedObject()
  {
    this(Collections.emptyList());
  }

  public MapBackedObject(
    final Map<? extends String, ? extends Object> map
  )
  {
    this(map.entrySet());
  }

  public MapBackedObject(
    final Collection<? extends Map.Entry<? extends String, ? extends Object>> entries
  )
  {
    this(
      MapBackedObject::new,
      LinkedHashMap::new,
      PropertyDescriptors.getAllDescriptors().values(),
      entries);
  }

  protected MapBackedObject(
    final Supplier<? extends Indexable> selfSupplier,
    final Supplier<Map<String, Object>> mapSupplier,
    final Collection<? extends PropertyDescriptor<?>> descs,
    final Collection<? extends Map.Entry<? extends String, ? extends Object>> entries
  )
  {
    super(
      selfSupplier,
      mapSupplier,
      descs,
      entries);
  }

  @Override
  public Object put(final String key, final Object value)
  {
    return doPut(key, value);
  }

  @Override
  public Object remove(final Object key)
  {
    return doRemove(key);
  }

  @Override
  public void clear()
  {
    doClear();
  }

  @Override
  public MapBackedObject withEntries(
    final Collection<? extends Entry<? extends String, ? extends Object>> entries
  )
  {
    return (MapBackedObject)super.withEntries(entries);
  }
}
