package me.paulschwarz.springdotenv;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvBuilder;

public class DotenvPropertyLoader {

  private final Dotenv dotenv;

  public DotenvPropertyLoader(DotenvConfig dotenvConfig) {
    DotenvBuilder dotenvBuilder = Dotenv.configure();

    dotenvConfig.getDirectoryOptional().ifPresent(dotenvBuilder::directory);
    dotenvConfig.getFilenameOptional().ifPresent(dotenvBuilder::filename);
    dotenvConfig.getIgnoreIfMalformedTruth().ifPresent(value -> dotenvBuilder.ignoreIfMalformed());
    dotenvConfig.getIgnoreIfMissingTruth().ifPresent(value -> dotenvBuilder.ignoreIfMissing());
    dotenvConfig.getSystemPropertiesTruth().ifPresent(value -> dotenvBuilder.systemProperties());

    dotenv = dotenvBuilder.load();
  }

  public Object getValue(String key) {
    String actualName = resolvePropertyName(key);
    return dotenv.get(actualName);
  }

  protected final String resolvePropertyName(String name) {
    String resolvedName = checkPropertyName(name);
    if (resolvedName != null) {
      return resolvedName;
    }
    String uppercaseName = name.toUpperCase();
    if (!name.equals(uppercaseName)) {
      resolvedName = checkPropertyName(uppercaseName);
      if (resolvedName != null) {
        return resolvedName;
      }
    }
    return name;
  }

  protected String checkPropertyName(String name) {
    // Check name as-is
    if (containsKey(name)) {
      return name;
    }
    // Check name with just dots replaced
    String noDotName = name.replace('.', '_');
    if (!name.equals(noDotName) && containsKey(noDotName)) {
      return noDotName;
    }
    // Check name with just hyphens replaced
    String noHyphenName = name.replace('-', '_');
    if (!name.equals(noHyphenName) && containsKey(noHyphenName)) {
      return noHyphenName;
    }
    // Check name with dots and hyphens replaced
    String noDotNoHyphenName = noDotName.replace('-', '_');
    if (!noDotName.equals(noDotNoHyphenName) && containsKey(noDotNoHyphenName)) {
      return noDotNoHyphenName;
    }
    // Give up
    return null;
  }

  protected boolean containsKey(String name) {
    return dotenv.get(name) != null;
  }

}
