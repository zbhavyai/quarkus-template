package io.github.zbhavyai.quarkustemplate.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jboss.logging.Logger;

public class JSONMapper {

  private static final Logger LOGGER = Logger.getLogger(JSONMapper.class.getSimpleName());
  private static final ObjectMapper objectMapper = new ObjectMapper();

  static {
    objectMapper.registerModule(new JavaTimeModule());
    // objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
  }

  private JSONMapper() {}

  public static <T> String prettyPrint(T value) {
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    String str = serialize(value);
    objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
    return str;
  }

  public static <T> String serialize(T obj) {
    try {
      return objectMapper.writeValueAsString(obj);
    } catch (Throwable t) {
      LOGGER.errorf("Serialization error: %s", t.getMessage());
      return "";
    }
  }

  public static <T> T deserialize(String str, Class<T> type) {
    try {
      return objectMapper.readValue(str, type);
    } catch (Throwable t) {
      LOGGER.errorf("Deserialization error: %s", t.getMessage());
      return null;
    }
  }

  public static <T> T deserialize(String str, TypeReference<T> type) {
    try {
      return objectMapper.readValue(str, type);
    } catch (Throwable t) {
      LOGGER.errorf("Deserialization error: %s", t.getMessage());
      return null;
    }
  }
}
