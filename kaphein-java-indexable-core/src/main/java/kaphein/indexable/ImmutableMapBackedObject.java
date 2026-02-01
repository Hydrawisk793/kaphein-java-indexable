package kaphein.indexable;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

/**
 * <p>An immutable, map-backed implementation of {@link Indexable}.</p>
 * 
 * @see ImmutableMapBackedObject
 */
@IndexableComplient
public class ImmutableMapBackedObject extends AbstractMapBackedObject
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

  public ImmutableMapBackedObject()
  {
    this(Collections.emptyList());
  }

  public ImmutableMapBackedObject(
    final Map<? extends String, ? extends Object> map
  )
  {
    this(map.entrySet());
  }

  public ImmutableMapBackedObject(
    final Collection<? extends Map.Entry<? extends String, ? extends Object>> entries
  )
  {
    this(
      ImmutableMapBackedObject::new,
      LinkedHashMap::new,
      PropertyDescriptors.getAllDescriptors().values(),
      entries);
  }

  protected ImmutableMapBackedObject(
    final Supplier<? extends Indexable> emptySupplier,
    final Supplier<Map<String, Object>> mapSupplier,
    final Collection<? extends PropertyDescriptor<?>> descs,
    final Collection<? extends Map.Entry<? extends String, ? extends Object>> entries
  )
  {
    super(
      emptySupplier,
      mapSupplier,
      descs,
      entries);
  }

  @Override
  public Object put(final String key, final Object value)
  {
    throw new UnsupportedOperationException("'put' is not supported.");
  }

  @Override
  public Object remove(final String key)
  {
    throw new UnsupportedOperationException("'remove' is not supported.");
  }

  @Override
  public void clear()
  {
    throw new UnsupportedOperationException("'clear' is not supported.");
  }

  @Override
  public ImmutableMapBackedObject withEntries(
    final Collection<? extends Entry<? extends String, ? extends Object>> entries
  )
  {
    return (ImmutableMapBackedObject)super.withEntries(entries);
  }
}
