package kaphein.indexable;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import kaphein.indexable.internal.AssertArg;

/**
 *  <p>An abstract class for {@link MapBackedObject} implementations.</p>
 */
@IndexableComplient
abstract class AbstractMapBackedObject implements MapBackedObject
{
  public static class PropertyDescriptors
  {
    private static final Map<String, ? extends PropertyDescriptor<?>> OWN_DESC_MAP = PropertyDescriptorHelpers
      .ownDescriptors(Collections.emptyList());

    public static Map<String, ? extends PropertyDescriptor<?>> getOwnDescriptors()
    {
      return OWN_DESC_MAP;
    }

    private static final Map<String, ? extends PropertyDescriptor<?>> ALL_DESC_MAP = PropertyDescriptorHelpers
      .allDescriptors(
        Collections.emptyList(),
        getOwnDescriptors().values());

    public static Map<String, ? extends PropertyDescriptor<?>> getAllDescriptors()
    {
      return ALL_DESC_MAP;
    }
  }

  private static final class MapView implements Map<String, Object>
  {
    private final AbstractMapBackedObject owner;

    private MapView(final AbstractMapBackedObject owner)
    {
      this.owner = AssertArg.isNotNull(owner, "owner");
    }

    @Override
    public int size()
    {
      return owner.propMap.size();
    }

    @Override
    public boolean isEmpty()
    {
      return owner.propMap.isEmpty();
    }

    @Override
    public boolean containsKey(final Object key)
    {
      return owner.propMap.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value)
    {
      // TODO: [P1] Implement this.
      throw new UnsupportedOperationException("Unimplemented method 'containsValue'");
    }

    @Override
    public Object get(final Object key)
    {
      return owner.get(key);
    }

    @Override
    public Object put(final String key, final Object value)
    {
      return owner.put(key, value);
    }

    @Override
    public Object remove(final Object key)
    {
      return owner.remove(key);
    }

    @Override
    public void putAll(final Map<? extends String, ? extends Object> m)
    {
      owner.putAll(m);
    }

    @Override
    public void clear()
    {
      owner.clear();
    }

    @Override
    public Set<String> keySet()
    {
      return owner.propMap.keySet();
    }

    @Override
    public Collection<Object> values()
    {
      // TODO: [P1] Implement this.
      throw new UnsupportedOperationException("Unimplemented method 'values'");
    }

    @Override
    public Set<Entry<String, Object>> entrySet()
    {
      // TODO: [P1] Implement this.
      throw new UnsupportedOperationException("Unimplemented method 'values'");
    }

    @Override
    public boolean equals(final Object obj)
    {
      return owner.equals(obj);
    }

    @Override
    public int hashCode()
    {
      return owner.hashCode();
    }
  }

  private final Supplier<? extends Indexable> emptySupplier;

  private final Map<String, PropertyDescriptor<?>> descMap;

  private final Map<String, Object> propMap;

  private final AtomicReference<MapView> mapViewRef;

  protected AbstractMapBackedObject(
    final Supplier<? extends Indexable> emptySupplier,
    final Supplier<Map<String, Object>> mapSupplier,
    final Collection<? extends PropertyDescriptor<?>> descs,
    final Collection<? extends Map.Entry<? extends String, ? extends Object>> entries
  )
  {
    this.emptySupplier = AssertArg.isNotNull(emptySupplier, "emptySupplier");
    this.descMap = AssertArg
      .isNotNull(descs, "descs")
      .stream()
      .collect(Collectors.collectingAndThen(
        Collectors.toMap(
          PropertyDescriptor::getIndexKey,
          Function.identity(),
          ChooseTheLastOneOperator.getInstance(),
          LinkedHashMap::new),
        Collections::unmodifiableMap));
    this.propMap = AssertArg.isNotNull(mapSupplier, "mapSupplier").get();
    this.mapViewRef = new AtomicReference<>();

    for(final Map.Entry<? extends String, ? extends Object> entry : entries)
    {
      doPut(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public Object get(final Object key)
  {
    AssertArg.isNotNull(key, "key");

    Object value = null;
    @SuppressWarnings("unchecked")
    final PropertyDescriptor<Object> desc = (PropertyDescriptor<Object>)descMap.get(key);
    if(null == desc)
    {
      value = propMap.get(key);
    }
    else
    {
      value = desc.getGetter().apply(desc, propMap);
    }

    return value;
  }

  @Override
  public <T> T getByDescriptor(
    final PropertyDescriptor<T> desc
  )
  {
    AssertArg.isNotNull(desc, "desc");

    final Object v = propMap.get(desc.getIndexKey());

    return (null == v ? null : desc.getCoercer().coerce(v));
  }

  @Override
  public Map<String, Object> getExtraProperties()
  {
    final Map<String, Object> extraProps = new LinkedHashMap<>();
    for(final String key : keys())
    {
      if(!descMap.containsKey(key))
      {
        extraProps.put(key, propMap.get(key));
      }
    }

    return extraProps;
  }

  @Override
  public Set<String> keys()
  {
    return propMap.keySet();
  }

  @Override
  public Set<Map.Entry<String, Object>> entries()
  {
    return propMap.entrySet();
  }

  @Override
  public boolean equals(final Object obj)
  {
    // TODO: [P1] Implement this.
    throw new UnsupportedOperationException("'equals' is not implemented yet.");
  }

  @Override
  public int hashCode()
  {
    return propMap.hashCode();
  }

  @Override
  public Map<String, Object> asMap()
  {
    return mapViewRef.getAndUpdate(this::createOrGetMapView);
  }

  @Override
  public AbstractMapBackedObject withEntries(
    final Collection<? extends Map.Entry<? extends String, ? extends Object>> entries
  )
  {
    AssertArg.isNotNull(entries, "entries");

    final AbstractMapBackedObject result = (AbstractMapBackedObject)emptySupplier.get();
    result.putAll(entries);

    return result;
  }

  @Override
  public Map<String, Object> toMap(
    final Supplier<Map<String, Object>> mapSupplier
  )
  {
    final Map<String, Object> m = AssertArg
      .isNotNull(mapSupplier, "mapSupplier")
      .get();
    m.putAll(propMap);

    return m;
  }

  protected Object doPut(final String key, final Object value)
  {
    AssertArg.isNotBlank(key, "key");

    Object oldValue = null;
    @SuppressWarnings("unchecked")
    final PropertyDescriptor<Object> desc = (PropertyDescriptor<Object>)descMap.get(key);
    if(null == desc)
    {
      oldValue = propMap.put(key, value);
    }
    else
    {
      oldValue = get(key);

      desc.getSetter().apply(desc, propMap, value);
    }

    return oldValue;
  }

  protected Object doRemove(final Object key)
  {
    return propMap.remove(key);
  }

  protected void doClear()
  {
    propMap.clear();
  }

  private MapView createOrGetMapView(final MapView mapView)
  {
    return ((null == mapView) ? new MapView(this) : mapView);
  }
}
