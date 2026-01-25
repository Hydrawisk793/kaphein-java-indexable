package kaphein.indexable.internal;

import java.util.function.Function;

public final class ThrowableExtensions
{
  private ThrowableExtensions()
  {
    // Empty.
  }

  public static final RuntimeException coerceToRuntimeException(final Throwable exception)
  {
    return coerceToRuntimeException(exception, RuntimeException::new);
  }

  public static final RuntimeException coerceToRuntimeException(
    final Throwable exception,
    final Function<Throwable, ? extends RuntimeException> wrapper
  )
  {
    AssertArg.isNotNull(exception, "exception");
    AssertArg.isNotNull(wrapper, "wrapper");

    RuntimeException finalException = null;
    if(exception instanceof RuntimeException)
    {
      finalException = (RuntimeException)exception;
    }
    else
    {
      finalException = wrapper.apply(exception);
    }

    return finalException;
  }
}
