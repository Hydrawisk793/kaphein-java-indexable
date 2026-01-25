package kaphein.indexable.jackson.v2;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;

import kaphein.indexable.Indexable;

public class IndexableModule extends SimpleModule
{
  private static final long serialVersionUID = 986348809458386690L;

  public IndexableModule()
  {
    super(IndexableModule.class.getName());

    setDeserializerModifier(new BeanDeserializerModifier()
    {
      @Override
      public JsonDeserializer<?> modifyDeserializer(
        final DeserializationConfig config,
        final BeanDescription beanDesc,
        final JsonDeserializer<?> deserializer
      )
      {
        if(Indexable.class.isAssignableFrom(beanDesc.getBeanClass()))
        {
          return new IndexableDeserializer(beanDesc.getType());
        }

        return deserializer;
      }
    });

    addSerializer(Indexable.class, new IndexableSerializer());
    addDeserializer(Indexable.class, new IndexableDeserializer());
  }
}
