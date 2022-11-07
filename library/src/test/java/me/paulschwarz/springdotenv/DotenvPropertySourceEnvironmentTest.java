package me.paulschwarz.springdotenv;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.ConfigurableEnvironment;

class DotenvPropertySourceEnvironmentTest {

  private DotenvPropertySource source;

  @BeforeEach
  void init() {
    ConfigurableEnvironment configurableEnvironment = mock(ConfigurableEnvironment.class);
    doReturn("").when(configurableEnvironment).getProperty(".env.prefix", (String) null);
    source = new DotenvPropertySource(new DotenvConfig(configurableEnvironment));
  }

  @Test
  void irrelevant() {
    assertThat(source.getProperty("other.VALUE")).isNull();
  }

  @Test
  void missing() {
    assertThat(source.getProperty("MISSING")).isNull();
  }

  @Test
  void valueFromDotenv() {
    assertThat(source.getProperty("EXAMPLE_MESSAGE_1")).isEqualTo("Message 1 from .env");
    assertThat(source.getProperty("example.message.1")).isEqualTo("Message 1 from .env");
    assertThat(source.getProperty("example-message-1")).isEqualTo("Message 1 from .env");
    assertThat(source.getProperty("example.message-1")).isEqualTo("Message 1 from .env");
    assertThat(source.getProperty("EXAMPLE.MESSAGE.1")).isEqualTo("Message 1 from .env");
    assertThat(source.getProperty("EXAMPLE-MESSAGE-1")).isEqualTo("Message 1 from .env");
    assertThat(source.getProperty("EXAMPLE.MESSAGE-1")).isEqualTo("Message 1 from .env");
  }

  @Test
  void givenPrefixEnv_valueIsNull() {
    assertThat(source.getProperty("env.EXAMPLE_MESSAGE_1")).isNull();
  }

  @Test
  void valueFromEnvironment() {
    assertThat(source.getProperty("EXAMPLE_MESSAGE_2")).isEqualTo("Message 2 from system environment");
  }

  @Test
  void valueFromEnvironmentOverride() {
    assertThat(source.getProperty("EXAMPLE_MESSAGE_3")).isEqualTo("Message 3 from system environment");
  }

}
