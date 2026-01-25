package kaphein.indexable.internal;

public final class AssertArg
{
  private AssertArg()
  {
    // Empty.
  }

  public static <T> T isNotNull(
    final T v,
    final String argName
  )
  {
    if(null == v)
    {
      throw new IllegalArgumentException('\''
        + argName
        + '\''
        + " cannot be null.");
    }

    return v;
  }

  public static String isNotBlank(
    final String v,
    final String argName
  )
  {
    AssertArg.isNotNull(v, argName);

    final int l = v.length();
    for(int i = 0; i < l; ++i)
    {
      final int cp = v.codePointAt(i);
      if(Character.isWhitespace(cp))
      {
        throw new IllegalArgumentException('\''
          + argName
          + '\''
          + " cannot be blank.");
      }
    }

    return v;
  }
}
