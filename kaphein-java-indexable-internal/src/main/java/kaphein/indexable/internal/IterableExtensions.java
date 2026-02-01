package kaphein.indexable.internal;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class IterableExtensions
{
  public static <T> Object[] toArray(
    final Iterable<T> iterable
  )
  {
    AssertArg.isNotNull(iterable, "iterable");

    return StreamSupport
      .stream(
        iterable.spliterator(),
        false)
      .collect(Collectors.toList())
      .toArray();
  }

  public static <T> T[] toArray(
    final Iterable<T> iterable,
    final T[] a
  )
  {
    AssertArg.isNotNull(iterable, "iterable");

    return StreamSupport
      .stream(
        iterable.spliterator(),
        false)
      .collect(Collectors.toList())
      .toArray(a);
  }

  public static <T> Stream<T> stream(
    final Iterable<T> iterable
  )
  {
    AssertArg.isNotNull(iterable, "iterable");

    return StreamSupport
      .stream(
        iterable.spliterator(),
        false);
  }

  public static <T> List<T> toList(
    final Iterable<T> iterable
  )
  {
    return stream(iterable).collect(Collectors.toList());
  }
}
