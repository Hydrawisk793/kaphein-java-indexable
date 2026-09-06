package kaphein.indexable;

import java.util.Iterator;
import java.util.NoSuchElementException;

import kaphein.indexable.internal.AssertArg;

class IndexableExtraPropertyKeysView implements Iterable<String>
{
  private static final class KeyIterator implements Iterator<String>
  {
    private final IndexableExtraPropertyKeysView owner;

    private final Iterator<String> lookaheadIter;

    private String nextKey;

    private boolean found;

    private KeyIterator(
      final IndexableExtraPropertyKeysView owner
    )
    {
      this.owner = AssertArg.isNotNull(owner, "owner");
      this.lookaheadIter = owner
        .getSource()
        .keys()
        .iterator();
      this.nextKey = null;
      this.found = false;

      moveToNext();
    }

    @Override
    public boolean hasNext()
    {
      return found;
    }

    @Override
    public String next()
    {
      final String result;
      if(found)
      {
        result = nextKey;

        moveToNext();
      }
      else
      {
        throw new NoSuchElementException();
      }

      return result;
    }

    private void moveToNext()
    {
      final Indexable source = owner.getSource();

      found = false;
      while(!found && lookaheadIter.hasNext())
      {
        final String key = lookaheadIter.next();
        if(!source.hasDescriptorFor(key))
        {
          nextKey = key;
          found = true;
        }
      }
    }
  }

  private final Indexable source;

  IndexableExtraPropertyKeysView(
    final Indexable source
  )
  {
    this.source = AssertArg.isNotNull(source, "source");
  }

  @Override
  public Iterator<String> iterator()
  {
    return new KeyIterator(this);
  }

  private Indexable getSource()
  {
    return source;
  }
}
