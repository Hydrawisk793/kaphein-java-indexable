package kaphein.indexable.jackson.v2;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThrows;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import kaphein.indexable.Indexable;
import kaphein.indexable.MapBackedObject;

public class IndexableJacksonV2BasicTest
{
  private final JsonMapper jsonMapper;

  public IndexableJacksonV2BasicTest()
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
    final MapBackedObject input = null;
    final String json = jsonMapper.writeValueAsString(input);

    assertThat(json, equalTo("null"));
  }

  @Test
  public void serializeEmpty()
    throws Exception
  {
    final Indexable input = new MapBackedObject();
    final String json = jsonMapper.writeValueAsString(input);

    assertThat(json, equalTo("{}"));
  }

  @Test
  public void serializeNonEmpty()
    throws Exception
  {
    final Indexable input = new MapBackedObject(Arrays.asList(
      Pair.of("foo", 1),
      Pair.of("bar", true),
      Pair.of("baz", "text")));
    final String json = jsonMapper.writeValueAsString(input);

    assertThat(json, equalTo("{\"foo\":1,\"bar\":true,\"baz\":\"text\"}"));
  }

  @Test
  public void serializeNonEmptyAndSkipNullValues()
    throws Exception
  {
    final Indexable input = new MapBackedObject(Arrays.asList(
      new AbstractMap.SimpleImmutableEntry<>("foo", 1),
      new AbstractMap.SimpleImmutableEntry<>("bar", null),
      new AbstractMap.SimpleImmutableEntry<>("baz", "text")));
    final String json = jsonMapper.writeValueAsString(input);

    assertThat(json, equalTo("{\"foo\":1,\"baz\":\"text\"}"));
  }

  @Test
  public void serializeNonEmptyAndIncludeEmptyStringValues()
    throws Exception
  {
    final Indexable input = new MapBackedObject(Arrays.asList(
      new AbstractMap.SimpleImmutableEntry<>("foo", 1),
      new AbstractMap.SimpleImmutableEntry<>("baz", "")));
    final String json = jsonMapper.writeValueAsString(input);

    assertThat(json, equalTo("{\"foo\":1,\"baz\":\"\"}"));
  }

  @Test
  public void deserializeNull()
    throws Exception
  {
    final String json = "null";
    final Indexable result = jsonMapper.readValue(json, MapBackedObject.class);

    assertThat(result, nullValue());
  }

  @Test
  public void deserializeEmpty()
    throws Exception
  {
    final String json = "{}";
    final Indexable result = jsonMapper.readValue(json, MapBackedObject.class);

    assertThat(result.keySet(), empty());
  }

  @Test
  public void deserializeNonEmpty()
    throws Exception
  {
    final String json = "{\"foo\":1,\"bar\":true,\"baz\":\"text\"}";
    final Indexable result = jsonMapper.readValue(json, MapBackedObject.class);
    final Indexable expected = new MapBackedObject(Arrays.asList(
      Pair.of("foo", 1),
      Pair.of("bar", true),
      Pair.of("baz", "text")));

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
      () -> jsonMapper.readValue("[]", MapBackedObject.class));
    assertThrows(
      MismatchedInputException.class,
      () -> jsonMapper.readValue("true", MapBackedObject.class));
    assertThrows(
      MismatchedInputException.class,
      () -> jsonMapper.readValue("\"foo\"", MapBackedObject.class));
    assertThrows(
      MismatchedInputException.class,
      () -> jsonMapper.readValue("3", MapBackedObject.class));
    assertThrows(
      MismatchedInputException.class,
      () -> jsonMapper.readValue("3.14", MapBackedObject.class));
  }
}
