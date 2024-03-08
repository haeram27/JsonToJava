package io.github.sharelison.jsontojava.file;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.github.sharelison.jsontojava.exception.JsonToJavaException;

public class JsonFileReader implements FileReader {

    @Override
    public String readJsonFromFile(String file) {
        try {
            return String.join(System.lineSeparator(),
                    Files.readAllLines(Paths.get(file), Charset.forName(StandardCharsets.UTF_8.name())));
        } catch (IOException ioException) {
            throw new JsonToJavaException("IO error while accessing file", ioException);
        } catch (RuntimeException runtimeException) {
            throw new JsonToJavaException("Unexpected error while accessing file", runtimeException);
        }
    }
}
