package kaphein.indexable.jackson.v2;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import kaphein.indexable.IndexableComplient;
import kaphein.indexable.PropertyDescriptor;
import kaphein.indexable.PropertyDescriptorFactories;
import kaphein.indexable.PropertyDescriptorHelpers;
import kaphein.indexable.MapBackedObject;
import kaphein.indexable.internal.AssertArg;
import kaphein.indexable.internal.MapFactories;
import lombok.EqualsAndHashCode;
import lombok.Getter;

public class IndexableJacksonV2InheritanceTest
{
  @EqualsAndHashCode
  @Getter
  public static class Foo
  {
    private final Integer foo;

    private final String bar;

    private final boolean baz;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Foo(
      @JsonProperty("foo") final Integer foo,
      @JsonProperty("bar") final String bar,
      @JsonProperty("baz") final boolean baz
    )
    {
      this.foo = foo;
      this.bar = bar;
      this.baz = baz;
    }
  }

  @IndexableComplient
  public static class Bar extends MapBackedObject
  {
    public static class PropertyDescriptors extends MapBackedObject.PropertyDescriptors
    {
      public static final PropertyDescriptor<
        String> PROP_FOO = PropertyDescriptorFactories
          .createString(
            "prop_foo",
            "propFoo");

      public static final PropertyDescriptor<Long> PROP_BAR = PropertyDescriptorFactories
        .createLong(
          "prop_bar",
          "propBar");

      public static final PropertyDescriptor<
        Boolean> PROP_BAZ = PropertyDescriptorFactories
          .createBoolean(
            "prop_baz",
            "propBaz");

      public static final PropertyDescriptor<
        Double> PROP_QUX = PropertyDescriptorFactories
          .createDouble(
            "prop_qux",
            "propQux");

      public static final PropertyDescriptor<
        List<String>> PROP_PIYO = PropertyDescriptorFactories
          .createList(
            "prop_piyo",
            "propPiyo",
            String.class);

      public static final PropertyDescriptor<
        Map<String, Object>> PROP_PIYO_PIYO = PropertyDescriptorFactories
          .createStringObjectMap(
            "prop_piyo_piyo",
            "propPiyoPiyo");

      public static final PropertyDescriptor<
        Foo> PROP_PIYO_PIYO_PIYO = PropertyDescriptorFactories
          .createSimple(
            "prop_piyo_piyo_piyo",
            "propPiyoPiyoPiyo",
            Foo.class);

      public static final PropertyDescriptor<
        String> PROP_URI_NAMED_CLAIM = PropertyDescriptorFactories
          .createString(
            "com.example.scheme-01:/segment_1/segment_2/segment_3",
            "propUriNamedClaim");

      private static final Map<
        String,
        ? extends PropertyDescriptor<?>> OWN_PROP_DESCS = PropertyDescriptorHelpers
          .ownDescriptors(
            PROP_FOO,
            PROP_BAR,
            PROP_BAZ,
            PROP_QUX,
            PROP_PIYO,
            PROP_PIYO_PIYO,
            PROP_PIYO_PIYO_PIYO,
            PROP_URI_NAMED_CLAIM);

      public static Map<String, ? extends PropertyDescriptor<?>> getOwnDescriptors()
      {
        return OWN_PROP_DESCS;
      }

      private static final Map<
        String,
        ? extends PropertyDescriptor<?>> ALL_PROP_DESCS = PropertyDescriptorHelpers
          .allDescriptors(
            MapBackedObject.PropertyDescriptors.getAllDescriptors().values(),
            getOwnDescriptors().values());

      public static Map<String, ? extends PropertyDescriptor<?>> getAllDescriptors()
      {
        return ALL_PROP_DESCS;
      }
    }

    public static PropertyDescriptor<?> getDescriptorByKey(final String key)
    {
      return PropertyDescriptors.getAllDescriptors().get(key);
    }

    public Bar()
    {
      super(
        Bar::new,
        LinkedHashMap::new,
        PropertyDescriptors.getAllDescriptors().values(),
        Collections.emptyList());
    }

    public Bar(final Bar src)
    {
      super(
        Bar::new,
        LinkedHashMap::new,
        PropertyDescriptors.getAllDescriptors().values(),
        AssertArg.isNotNull(src, "src").toMap().entrySet());
    }

    public Bar(final Collection<? extends Map.Entry<? extends String, ? extends Object>> entries)
    {
      super(
        Bar::new,
        LinkedHashMap::new,
        PropertyDescriptors.getAllDescriptors().values(),
        AssertArg.isNotNull(entries, "entries"));
    }

    public String getPropFoo()
    {
      return getByDescriptor(PropertyDescriptors.PROP_FOO);
    }

    public Long getPropBar()
    {
      return getByDescriptor(PropertyDescriptors.PROP_BAR);
    }

    public Boolean getPropBaz()
    {
      return getByDescriptor(PropertyDescriptors.PROP_BAZ);
    }

    public Double getPropQux()
    {
      return getByDescriptor(PropertyDescriptors.PROP_QUX);
    }

    public List<String> getPropPiyo()
    {
      return getByDescriptor(PropertyDescriptors.PROP_PIYO);
    }

    public Map<String, Object> getPropPiyoPiyo()
    {
      return getByDescriptor(PropertyDescriptors.PROP_PIYO_PIYO);
    }

    public Foo getPropPiyoPiyoPiyo()
    {
      return getByDescriptor(PropertyDescriptors.PROP_PIYO_PIYO_PIYO);
    }

    public String propUriNamedClaim()
    {
      return getByDescriptor(PropertyDescriptors.PROP_URI_NAMED_CLAIM);
    }

    @Override
    public Bar withEntries(
      final Collection<? extends Entry<? extends String, ? extends Object>> entries
    )
    {
      return (Bar)super.withEntries(entries);
    }
  }

  private final JsonMapper jsonMapper;

  public IndexableJacksonV2InheritanceTest()
  {
    this.jsonMapper = JsonMapper
      .builder()
      .addModule(new IndexableModule())
      .disable(MapperFeature.REQUIRE_TYPE_ID_FOR_SUBTYPES)
      .build();
  }

  @Test
  public void serializeNull()
    throws Exception
  {
    final Bar input = null;
    final String json = jsonMapper.writeValueAsString(input);

    assertThat(json, equalTo("null"));
  }

  @Test
  public void serializeEmpty()
    throws Exception
  {
    final Bar input = new Bar();
    final String json = jsonMapper.writeValueAsString(input);

    assertThat(json, equalTo("{}"));
  }

  @Test
  public void serializeNonEmpty()
    throws Exception
  {
    final Bar input = new Bar(Arrays.asList(
      Pair.of("prop_foo", "text"),
      Pair.of("prop_bar", 2),
      Pair.of("prop_baz", true),
      Pair.of("prop_qux", 3.14),
      Pair.of("prop_piyo", Arrays.asList("a", "b")),
      Pair.of("prop_piyo_piyo", MapFactories.fromOrderedEntries(
        Pair.of("x", 1),
        Pair.of("y", 2))),
      Pair.of("prop_piyo_piyo_piyo", new Foo(1, "2", true)),
      Pair.of("com.example.scheme-01:/segment_1/segment_2/segment_3", "textValue")));
    final String json = jsonMapper.writeValueAsString(input);

    assertThat(
      json,
      equalTo(
        "{\"prop_foo\":\"text\",\"prop_bar\":2,\"prop_baz\":true,\"prop_qux\":3.14,\"prop_piyo\":[\"a\",\"b\"],\"prop_piyo_piyo\":{\"x\":1,\"y\":2},\"prop_piyo_piyo_piyo\":{\"foo\":1,\"bar\":\"2\",\"baz\":true},\"com.example.scheme-01:/segment_1/segment_2/segment_3\":\"textValue\"}"));
  }

  @Test
  public void serializeNonEmptyAndSkipNullValues()
    throws Exception
  {
    final Bar input = new Bar(Arrays.asList(
      Pair.of("prop_foo", "text"),
      Pair.of("prop_bar", null),
      Pair.of("prop_baz", true),
      Pair.of("prop_qux", 3.14),
      Pair.of("prop_piyo", Arrays.asList("a", "b")),
      Pair.of("prop_piyo_piyo", null),
      Pair.of("prop_piyo_piyo_piyo", new Foo(1, "2", true))));
    final String json = jsonMapper.writeValueAsString(input);

    assertThat(
      json,
      equalTo(
        "{\"prop_foo\":\"text\",\"prop_baz\":true,\"prop_qux\":3.14,\"prop_piyo\":[\"a\",\"b\"],\"prop_piyo_piyo_piyo\":{\"foo\":1,\"bar\":\"2\",\"baz\":true}}"));
  }

  @Test
  public void serializeNonEmptyAndIncludeEmptyStringValues()
    throws Exception
  {
    final Bar input = new Bar(Arrays.asList(
      Pair.of("prop_foo", "")));
    final String json = jsonMapper.writeValueAsString(input);

    assertThat(json, equalTo("{\"prop_foo\":\"\"}"));
  }

  @Test
  public void deserializeNull()
    throws Exception
  {
    final String json = "null";
    final Bar result = jsonMapper.readValue(json, Bar.class);

    assertThat(result, nullValue());
  }

  @Test
  public void deserializeEmpty()
    throws Exception
  {
    final String json = "{}";
    final Bar result = jsonMapper.readValue(json, Bar.class);

    assertThat(result.keySet(), empty());
  }

  @Test
  public void deserializeNonEmpty()
    throws Exception
  {
    final String json = "{\"prop_foo\":\"text\",\"prop_bar\":2,\"prop_baz\":true,\"prop_qux\":3.14,\"prop_piyo\":[\"a\",\"b\"],\"prop_piyo_piyo\":{\"x\":1,\"y\":2},\"prop_piyo_piyo_piyo\":{\"foo\":1,\"bar\":\"2\",\"baz\":true},\"com.example.scheme-01:/segment_1/segment_2/segment_3\":\"textValue\"}";
    final Bar result = jsonMapper.readValue(json, Bar.class);
    final Bar expected = new Bar(Arrays.asList(
      Pair.of("prop_foo", "text"),
      Pair.of("prop_bar", 2),
      Pair.of("prop_baz", true),
      Pair.of("prop_qux", 3.14),
      Pair.of("prop_piyo", Arrays.asList("a", "b")),
      Pair.of("prop_piyo_piyo", MapFactories.fromOrderedEntries(
        Pair.of("x", 1),
        Pair.of("y", 2))),
      Pair.of("prop_piyo_piyo_piyo", new Foo(1, "2", true)),
      Pair.of("com.example.scheme-01:/segment_1/segment_2/segment_3", "textValue")));

    assertThat(result.isEmpty(), equalTo(false));
    assertThat(result.keySet(), containsInAnyOrder(expected.keySet().toArray()));
    assertThat(
      result
        .keySet()
        .stream()
        .map(result::get)
        .collect(Collectors.toList()),
      containsInAnyOrder(expected
        .keySet()
        .stream()
        .map(expected::get)
        .collect(Collectors.toList())
        .toArray()));
  }

  @Test
  public void deserializeInvalid()
    throws Exception
  {
    assertThrows(
      MismatchedInputException.class,
      () -> jsonMapper.readValue("[]", Bar.class));
    assertThrows(
      MismatchedInputException.class,
      () -> jsonMapper.readValue("true", Bar.class));
    assertThrows(
      MismatchedInputException.class,
      () -> jsonMapper.readValue("\"foo\"", Bar.class));
    assertThrows(
      MismatchedInputException.class,
      () -> jsonMapper.readValue("3", Bar.class));
    assertThrows(
      MismatchedInputException.class,
      () -> jsonMapper.readValue("3.14", Bar.class));
  }
}
