package kaphein.indexable;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import kaphein.indexable.internal.AssertArg;

@IndexableComplient
abstract class AbstractMapBackedObject implements Indexable
{
  public static class PropertyDescriptors
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
        Collections.<PropertyDescriptor<?>>emptyList(),
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

  private final Supplier<? extends Indexable> selfSupplier;

  private final Map<String, PropertyDescriptor<?>> descMap;

  private final Map<String, Object> propMap;

  protected AbstractMapBackedObject(
    final Supplier<? extends Indexable> selfSupplier,
    final Supplier<Map<String, Object>> mapSupplier,
    final Collection<? extends PropertyDescriptor<?>> descs,
    final Collection<? extends Map.Entry<? extends String, ? extends Object>> entries
  )
  {
    this.selfSupplier = AssertArg.isNotNull(selfSupplier, "selfSupplier");
    this.descMap = AssertArg
      .isNotNull(descs, "descs")
      .stream()
      .collect(Collectors.collectingAndThen(
        Collectors.toMap(
          PropertyDescriptor::getIndexKey,
          Function.identity(),
          (l, r) -> r,
          LinkedHashMap::new),
        Collections::unmodifiableMap));
    this.propMap = AssertArg.isNotNull(mapSupplier, "mapSupplier").get();

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
  public int size()
  {
    return propMap.size();
  }

  @Override
  public boolean containsKey(final Object key)
  {
    return propMap.containsKey(key);
  }

  @Override
  public boolean containsValue(final Object value)
  {
    return propMap.containsValue(value);
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
    for(final String key : propMap.keySet())
    {
      if(!descMap.containsKey(key))
      {
        extraProps.put(key, propMap.get(key));
      }
    }

    return extraProps;
  }

  @Override
  public Set<String> keySet()
  {
    return propMap.keySet();
  }

  @Override
  public Collection<Object> values()
  {
    return propMap.values();
  }

  @Override
  public Set<Map.Entry<String, Object>> entrySet()
  {
    return propMap.entrySet();
  }

  @Override
  public <R extends Indexable> R withEntries(
    final Collection<? extends Map.Entry<? extends String, ? extends Object>> entries
  )
  {
    AssertArg.isNotNull(entries, "entries");

    @SuppressWarnings("unchecked")
    final R result = (R)selfSupplier.get();
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
    if(null != desc)
    {
      oldValue = get(key);

      desc.getSetter().apply(desc, propMap, value);
    }
    else
    {
      oldValue = propMap.put(key, value);
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

}
