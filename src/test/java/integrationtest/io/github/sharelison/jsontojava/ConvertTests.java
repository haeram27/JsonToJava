package integrationtest.io.github.sharelison.jsontojava;

import java.io.IOException;

import org.junit.Test;

import io.github.sharelison.jsontojava.JsonToJava;

public class ConvertTests {

    public static final String TEST_JSON = "src/test/resources/integrationtest/test.json";
    public static final String PACKAGE_NAME = "integrationtest";
    public static final String OBJECT_NAME = "Person";
    public static final String ACTUAL_OUTPUT = "src/test/resources/integrationtest/actual";

    @Test
    public void convertTest() throws IOException {
        JsonToJava jsonToJava = new JsonToJava();

        jsonToJava.jsonToJava(TEST_JSON, OBJECT_NAME, PACKAGE_NAME, ACTUAL_OUTPUT);
    }
}
