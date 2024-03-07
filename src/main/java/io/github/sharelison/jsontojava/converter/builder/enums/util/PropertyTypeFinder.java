package io.github.sharelison.jsontojava.converter.builder.enums.util;

import io.github.sharelison.jsontojava.converter.builder.enums.ComplexPropertyType;
import io.github.sharelison.jsontojava.converter.builder.enums.PropertyType;
import io.github.sharelison.jsontojava.converter.builder.enums.SinglePropertyType;
import io.github.sharelison.jsontojava.validator.JsonTypeChecker;

public final class PropertyTypeFinder {

    private PropertyTypeFinder() {}

    public static PropertyType getPropertyType(Object object, JsonTypeChecker jsonTypeChecker) {
        PropertyType propertyType = SinglePropertyType.NEW;

        if(!jsonTypeChecker.isArray(object.toString())) {
            if(object instanceof String) {
                propertyType = SinglePropertyType.STRING;
            } else if(object instanceof Integer) {
                propertyType = SinglePropertyType.INTEGER;
            }  else if(object instanceof Long) {
                propertyType = SinglePropertyType.LONG;
            } else if(object instanceof Double) {
                propertyType = SinglePropertyType.DOUBLE;
            } else if (object instanceof Boolean) {
                propertyType = SinglePropertyType.BOOLEAN;
            }
        } else {
            propertyType = ComplexPropertyType.LIST;
        }

        return propertyType;
    }
}
