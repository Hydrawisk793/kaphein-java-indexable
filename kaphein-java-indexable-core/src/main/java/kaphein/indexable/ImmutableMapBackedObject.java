package kaphein.indexable;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@IndexableComplient
public class ImmutableMapBackedObject extends AbstractMapBackedObject
{
  public static class PropertyDescriptors extends AbstractMapBackedObject.PropertyDescriptors
  {
    private static final Map<String, ? extends PropertyDescriptor<?>> OWN_DESC_MAP = Stream
      .<PropertyDescriptor<?>>of(
      // Empty.
      )
      .collect(Collectors.collectingAndThen(
        Collectors.toMap(
          PropertyDescriptor::getIndexKey,
          Function.identity(),
          (l, r) -> r,
          LinkedHashMap::new),
        Collections::unmodifiableMap));

    public static Map<String, ? extends PropertyDescriptor<?>> getOwnDescriptors()
    {
      return OWN_DESC_MAP;
    }

    private static final Map<String, ? extends PropertyDescriptor<?>> ALL_DESC_MAP = Stream
      .of(
        AbstractMapBackedObject.PropertyDescriptors.getAllDescriptors().values(),
        getOwnDescriptors().values())
      .flatMap(Collection::stream)
      .collect(Collectors.collectingAndThen(
        Collectors.toMap(
          PropertyDescriptor::getIndexKey,
          Function.identity(),
          (l, r) -> r,
          LinkedHashMap::new),
        Collections::unmodifiableMap));

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
      MapBackedObject::new,
      LinkedHashMap::new,
      PropertyDescriptors.getAllDescriptors().values(),
      entries);
  }

  protected ImmutableMapBackedObject(
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
}
