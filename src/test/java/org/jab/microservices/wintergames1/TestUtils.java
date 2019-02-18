package org.jab.microservices.wintergames1;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static java.nio.charset.Charset.defaultCharset;
import static org.springframework.util.StreamUtils.copyToString;

public class TestUtils {

    public static String getResourceAsString(final String pathToFile) throws IOException {
        return copyToString(new ClassPathResource(pathToFile).getInputStream(), defaultCharset());
    }

}