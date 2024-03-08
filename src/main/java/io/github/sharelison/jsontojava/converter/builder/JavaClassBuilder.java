package io.github.sharelison.jsontojava.converter.builder;

import java.util.HashSet;

import io.github.sharelison.jsontojava.constants.JsonToJavaConstants;
import io.github.sharelison.jsontojava.converter.builder.enums.ComplexPropertyType;
import io.github.sharelison.jsontojava.exception.JsonToJavaException;

public class JavaClassBuilder {

    private static final char END_STATEMENT = ';';
    private static final String NEW_LINE = System.lineSeparator();
    private static final String SPACE = " ";
    private static final String BIG_SPACE = "    ";
    private static final String DOUBLE_NEW_LINE = System.lineSeparator() + System.lineSeparator();
    private static final char BLOCK_OPEN = '{';
    private static final char BLOCK_CLOSED = '}';
    private static final String METHOD_NO_ARGS = "()";
    private static final char METHOD_OPEN = '(';
    private static final char METHOD_CLOSED = ')';

    private static final String JSONPROPERTY_IMPORT_STATEMENT = "com.fasterxml.jackson.annotation.JsonProperty";

    private String javaClassDeclaration;

    private StringBuilder properties;
    private StringBuilder gettersAndSetters;
    private StringBuilder importStatements;

    private HashSet<String> importedClasses;

    private HashSet<String> propertyKeyNames;

    private String className;

    private boolean withAnnotations = JsonToJavaConstants.DEFAULT_FOR_WITH_ANNOTATIONS;

    public JavaClassBuilder(String className, String packagename) {
        validClassNameAndPackageName(className, packagename);
        declareClass(JavaClassBuilder.firstCharToUpperCase(className), packagename);
        properties = new StringBuilder();
        gettersAndSetters = new StringBuilder();
        importStatements = new StringBuilder();
        importedClasses = new HashSet<>();
        propertyKeyNames = new HashSet<>();
        addImportStatement(JSONPROPERTY_IMPORT_STATEMENT);
    }

    public JavaClassBuilder(String className, String packagename, boolean withAnnotations) {
        validClassNameAndPackageName(className, packagename);
        declareClass(JavaClassBuilder.firstCharToUpperCase(className), packagename);
        this.withAnnotations = withAnnotations;
        properties = new StringBuilder();
        gettersAndSetters = new StringBuilder();
        importStatements = new StringBuilder();
        importedClasses = new HashSet<>();
        propertyKeyNames = new HashSet<>();

        if (this.withAnnotations)
            addImportStatement(JSONPROPERTY_IMPORT_STATEMENT);
    }

    private void validClassNameAndPackageName(String className, String packagename) {
        if (className == null || className.isEmpty() || packagename == null || packagename.isEmpty())
            throw new JsonToJavaException("Class name or package name is empty");
    }

    public String build() {
        String actualImportStatements = importStatements.length() == 0 ? "" : importStatements.toString() + NEW_LINE;
        return String.format(javaClassDeclaration, actualImportStatements, properties.toString(),
                gettersAndSetters.toString());
    }

    public String getClassName() {
        return className;
    }

    public void addProperty(String originalPropertyName, String declareName) {
        String propertyName = removeUnwantedCharacters(originalPropertyName);
        if (!propertyKeyNames.contains(propertyName)) {
            appendAnnotations(originalPropertyName);
            properties
                    .append(BIG_SPACE)
                    .append("private ")
                    .append(declareName)
                    .append(SPACE)
                    .append(propertyName)
                    .append(END_STATEMENT)
                    .append(NEW_LINE);
            addGettersAndSetters(propertyName, declareName);
            propertyKeyNames.add(propertyName);
        }
    }

    private void appendAnnotations(String originalPropertyName) {
        if (this.withAnnotations) {
            properties
                    .append(BIG_SPACE)
                    .append("@JsonProperty(\"").append(originalPropertyName).append("\"").append(METHOD_CLOSED)
                    .append(NEW_LINE);
        }
    }

    public void addProperty(String originalPropertyName, ComplexPropertyType complexPropertyType, String genericType) {
        String propertyName = removeUnwantedCharacters(originalPropertyName);
        if (!propertyKeyNames.contains(propertyName)) {
            String declareName = String.format(complexPropertyType.getDeclareName(), genericType);
            appendAnnotations(originalPropertyName);
            properties
                    .append(BIG_SPACE)
                    .append("private ")
                    .append(declareName)
                    .append(" ")
                    .append(propertyName)
                    .append(END_STATEMENT)
                    .append(NEW_LINE);
            addGettersAndSetters(propertyName, declareName);
            propertyKeyNames.add(propertyName);
        }
    }

    public void addImportStatement(String importStatement) {
        if (!importedClasses.contains(importStatement)) {
            importStatements.append("import ").append(importStatement).append(END_STATEMENT).append(NEW_LINE);
            importedClasses.add(importStatement);
        }
    }

    public boolean hasProperty(String propertyName) {
        return propertyKeyNames.contains(propertyName);
    }

    public static String firstCharToUpperCase(String propertyName) {
        return propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    }

    private void declareClass(String className, String packagename) {
        this.className = removeUnwantedCharacters(className);
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append("package ").append(packagename).append(END_STATEMENT)
                .append(DOUBLE_NEW_LINE)
                .append("%s") //import statements
                .append("public class ").append(this.className).append(SPACE)
                .append(BLOCK_OPEN)
                .append(DOUBLE_NEW_LINE)
                .append("%s") //properties
                .append(NEW_LINE)
                .append("%s") //getters and setters
                .append(BLOCK_CLOSED);

        javaClassDeclaration = stringBuilder.toString();
    }

    private void addGettersAndSetters(String propertyName, String propertyType) {
        String firstUpperProperty = firstCharToUpperCase(propertyName);
        gettersAndSetters
                .append(BIG_SPACE)
                .append("public ")
                .append(propertyType).append(SPACE)
                .append("get").append(firstUpperProperty).append(METHOD_NO_ARGS)
                .append(BLOCK_OPEN).append(NEW_LINE).append(BIG_SPACE).append(BIG_SPACE)
                .append("return this.").append(propertyName).append(END_STATEMENT)
                .append(NEW_LINE).append(BIG_SPACE)
                .append(BLOCK_CLOSED)
                .append(DOUBLE_NEW_LINE);

        gettersAndSetters
                .append(BIG_SPACE)
                .append("public void").append(SPACE)
                .append("set").append(firstUpperProperty)
                .append(METHOD_OPEN).append(propertyType).append(SPACE).append(propertyName).append(METHOD_CLOSED)
                .append(BLOCK_OPEN).append(NEW_LINE).append(BIG_SPACE).append(BIG_SPACE)
                .append("this.").append(propertyName).append(" = ").append(propertyName).append(END_STATEMENT)
                .append(NEW_LINE).append(BIG_SPACE)
                .append(BLOCK_CLOSED)
                .append(DOUBLE_NEW_LINE);
    }

    private String removeUnwantedCharacters(String javaIdentifier) {
        StringBuilder validJavaIdentifier = new StringBuilder();

        char[] givenClassChars = javaIdentifier.toCharArray();
        for (int i = 0; i < givenClassChars.length; i++) {
            char character = givenClassChars[i];
            if (Character.isJavaIdentifierPart(character)) {
                if (i == 0) {
                    if (Character.isJavaIdentifierStart(character)) {
                        validJavaIdentifier.append(character);
                    }
                } else {
                    validJavaIdentifier.append(character);
                }
            }
        }

        if (validJavaIdentifier.length() == 0) {
            throw new JsonToJavaException("No valid characters in class name or property name");
        }

        return validJavaIdentifier.toString();
    }
}
