package kaphein.indexable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import kaphein.indexable.internal.AssertArg;

public final class PropertyDescriptorHelpers
{
  private PropertyDescriptorHelpers()
  {
    // Empty.
  }

  @SafeVarargs
  public static Map<String, ? extends PropertyDescriptor<?>> ownDescriptors(
    final PropertyDescriptor<?>... descs
  )
  {
    AssertArg.isNotNull(descs, "descs");

    return ownDescriptors(Arrays.stream(descs));
  }

  public static Map<String, ? extends PropertyDescriptor<?>> ownDescriptors(
    final List<? extends PropertyDescriptor<?>> descs
  )
  {
    AssertArg.isNotNull(descs, "descs");

    return descs
      .stream()
      .collect(Collectors.collectingAndThen(
        Collectors.toMap(
          PropertyDescriptor::getIndexKey,
          Function.identity(),
          ChooseTheLastOneOperator.getInstance(),
          LinkedHashMap::new),
        Collections::unmodifiableMap));
  }

  public static Map<String, ? extends PropertyDescriptor<?>> ownDescriptors(
    final Stream<? extends PropertyDescriptor<?>> descs
  )
  {
    AssertArg.isNotNull(descs, "descs");

    return descs.collect(Collectors.collectingAndThen(
      Collectors.toMap(
        PropertyDescriptor::getIndexKey,
        Function.identity(),
        ChooseTheLastOneOperator.getInstance(),
        LinkedHashMap::new),
      Collections::unmodifiableMap));
  }

  public static Map<String, ? extends PropertyDescriptor<?>> allDescriptors(
    final Collection<? extends PropertyDescriptor<?>> parentDescs,
    final Collection<? extends PropertyDescriptor<?>> ownDescs
  )
  {
    AssertArg.isNotNull(parentDescs, "parentDescs");
    AssertArg.isNotNull(ownDescs, "ownDescs");

    return Stream
      .of(
        parentDescs,
        ownDescs)
      .flatMap(Collection::stream)
      .collect(Collectors.collectingAndThen(
        Collectors.toMap(
          PropertyDescriptor::getIndexKey,
          Function.identity(),
          ChooseTheLastOneOperator.getInstance(),
          LinkedHashMap::new),
        Collections::unmodifiableMap));
  }
}
