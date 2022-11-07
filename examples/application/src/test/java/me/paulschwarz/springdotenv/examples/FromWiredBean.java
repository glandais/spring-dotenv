package me.paulschwarz.springdotenv.examples;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath*:spring.xml")
public class FromWiredBean {

  String value1;
  String value2;
  String value3;
  String value4;

  public FromWiredBean(String value1, String value2, String value3, String value4) {
    this.value1 = value1;
    this.value2 = value2;
    this.value3 = value3;
    this.value4 = value4;
  }

  public String getValue1() {
    return value1;
  }

  public String getValue2() {
    return value2;
  }

  public String getValue3() {
    return value3;
  }

  public String getValue4() {
    return value4;
  }
}
