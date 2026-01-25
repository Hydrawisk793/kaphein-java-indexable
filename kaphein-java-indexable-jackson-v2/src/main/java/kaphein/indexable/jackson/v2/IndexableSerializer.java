package kaphein.indexable.jackson.v2;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import kaphein.indexable.Indexable;

public class IndexableSerializer extends StdSerializer<Indexable>
{
  private static final long serialVersionUID = -6858398726130393543L;

  public IndexableSerializer()
  {
    super(Indexable.class);
  }

  @Override
  public void serialize(
    final Indexable value,
    final JsonGenerator gen,
    final SerializerProvider provider
  )
    throws IOException,
    JsonProcessingException
  {
    if(null == value)
    {
      gen.writeNull();
    }
    else
    {
      gen.writeStartObject();

      serializeEntries(value, gen, provider);

      gen.writeEndObject();
    }
  }

  @Override
  public void serializeWithType(
    final Indexable value,
    final JsonGenerator gen,
    final SerializerProvider provider,
    final TypeSerializer typeSer
  )
    throws IOException,
    JsonProcessingException
  {
    if(null == value)
    {
      gen.writeNull();
    }
    else
    {
      final WritableTypeId typeId = typeSer.typeId(value, JsonToken.START_OBJECT);
      typeSer.writeTypePrefix(gen, typeId);

      serializeEntries(value, gen, provider);

      typeSer.writeTypeSuffix(gen, typeId);
    }
  }

  private void serializeEntries(
    final Indexable value,
    final JsonGenerator gen,
    final SerializerProvider provider
  )
    throws IOException,
    JsonProcessingException
  {
    for(final String k : value.keySet())
    {
      if(null != k)
      {
        final Object v = value.get(k);
        if(null != v)
        {
          gen.writeObjectField(k, v);
        }
      }
    }
  }
}
