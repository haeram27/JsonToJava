package io.github.sharelison.jsontojava.validator;

import org.json.JSONArray;

import io.github.sharelison.jsontojava.exception.JsonToJavaException;

public class InputJsonValidator implements JsonValidator {

    private final JsonTypeChecker jsonTypeChecker;

    public InputJsonValidator() {
        jsonTypeChecker = new JsonType();
    }

    /**
     * Default is {@link io.github.sharelison.jsontojava.validator.JsonType}
     * @param jsonTypeChecker used to check weather given string is a jsonstring or jsonobject
     */
    public InputJsonValidator(JsonTypeChecker jsonTypeChecker) {
        this.jsonTypeChecker = jsonTypeChecker;
    }

    public boolean isValidJson(final String jsonString) {
        return !nullOrEmpty(jsonString) && (validArray(jsonString) || jsonTypeChecker.isObject(jsonString));
    }

    private boolean validArray(String jsonString) {
        boolean valid = false;

        if (jsonTypeChecker.isArray(jsonString)) {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                String elementJsonString = jsonArray.get(i).toString();
                if (jsonTypeChecker.isObject(elementJsonString)) {
                    valid = true;
                    break;
                } else if (jsonTypeChecker.isArray(elementJsonString)) {
                    valid = validArray(elementJsonString);
                }
            }

            if (!valid)
                throw new JsonToJavaException("JSON Array does not contain any objects, no class can be created");
        }

        return valid;
    }

    private boolean nullOrEmpty(String jsonString) {
        return jsonString == null || jsonString.isEmpty();
    }
}
