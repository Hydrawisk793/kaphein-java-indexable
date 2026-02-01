package kaphein.indexable;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
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
abstract class AbstractMapBackedObject implements MapBackedObject
{
  public static class PropertyDescriptors
  {
    private static final Map<String, ? extends PropertyDescriptor<?>> OWN_DESC_MAP = PropertyDescriptorMaps
      .ownDescriptors(Collections.emptyList());

    public static Map<String, ? extends PropertyDescriptor<?>> getOwnDescriptors()
    {
      return OWN_DESC_MAP;
    }

    private static final Map<String, ? extends PropertyDescriptor<?>> ALL_DESC_MAP = PropertyDescriptorMaps
      .allDescriptors(
        Collections.emptyList(),
        getOwnDescriptors().values());

    public static Map<String, ? extends PropertyDescriptor<?>> getAllDescriptors()
    {
      return ALL_DESC_MAP;
    }
  }

  private final Map<String, PropertyDescriptor<?>> descMap;

  private final Map<String, Object> propMap;

  private final Supplier<? extends Indexable> emptySupplier;

  private final AtomicReference<MapBackedObjectMapView> mapViewRef;

  protected AbstractMapBackedObject(
    final Supplier<Map<String, Object>> mapSupplier,
    final Collection<? extends PropertyDescriptor<?>> descs,
    final Collection<? extends Map.Entry<? extends String, ? extends Object>> entries,
    final Supplier<? extends Indexable> emptySupplier
  )
  {
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
    this.emptySupplier = emptySupplier;
    this.mapViewRef = new AtomicReference<>();

    for(final Map.Entry<? extends String, ? extends Object> entry : entries)
    {
      doPut(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public boolean isEmpty()
  {
    return propMap.isEmpty();
  }

  @Override
  public boolean containsKey(final String key)
  {
    return propMap.containsKey(key);
  }

  @Override
  public Object get(final String key)
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

    return desc.getGetter().apply(desc, propMap);
  }

  @Override
  public List<Map.Entry<String, Object>> getExtraProperties()
  {
    final List<Map.Entry<String, Object>> extraProps = new ArrayList<>();
    for(final String key : keys())
    {
      if(!descMap.containsKey(key))
      {
        extraProps.add(new AbstractMap.SimpleImmutableEntry<>(key, propMap.get(key)));
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

    if(null == emptySupplier)
    {
      throw new UnsupportedOperationException("'withEntries' is not supported.");
    }

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

  protected Object doRemove(final String key)
  {
    return propMap.remove(key);
  }

  protected void doClear()
  {
    propMap.clear();
  }

  Map<String, Object> getPropMap()
  {
    return propMap;
  }

  private MapBackedObjectMapView createOrGetMapView(
    final MapBackedObjectMapView mapView
  )
  {
    return ((null == mapView) ? new MapBackedObjectMapView(this) : mapView);
  }
}
