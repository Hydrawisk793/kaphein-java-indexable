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
 * @see ImmutableMapBackedObject
 */
public class MutableMapBackedObject extends AbstractMapBackedObject
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

  public MutableMapBackedObject()
  {
    this(Collections.emptyList());
  }

  public MutableMapBackedObject(
    final Map<? extends String, ? extends Object> map
  )
  {
    this(map.entrySet());
  }

  public MutableMapBackedObject(
    final Collection<? extends Map.Entry<? extends String, ? extends Object>> entries
  )
  {
    this(
      LinkedHashMap::new,
      PropertyDescriptors.getAllDescriptors().values(),
      entries,
      MutableMapBackedObject::new);
  }

  protected MutableMapBackedObject(
    final Supplier<Map<String, Object>> mapSupplier,
    final Collection<? extends PropertyDescriptor<?>> descs,
    final Collection<? extends Map.Entry<? extends String, ? extends Object>> entries,
    final Supplier<? extends Indexable> emptySupplier
  )
  {
    super(
      mapSupplier,
      descs,
      entries,
      emptySupplier);
  }

  @Override
  public Object put(final String key, final Object value)
  {
    return doPut(key, value);
  }

  @Override
  public Object remove(final String key)
  {
    return doRemove(key);
  }

  @Override
  public void clear()
  {
    doClear();
  }

  @Override
  public MutableMapBackedObject withEntries(
    final Collection<? extends Entry<? extends String, ? extends Object>> entries
  )
  {
    return (MutableMapBackedObject)super.withEntries(entries);
  }
}
