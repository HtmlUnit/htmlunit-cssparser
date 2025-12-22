/*
 * Copyright (c) 2019-2024 Ronald Brill.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.cssparser.parser.validator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.htmlunit.cssparser.parser.CSSParseException;
import org.htmlunit.cssparser.parser.LexicalUnit;
import org.htmlunit.cssparser.parser.LexicalUnit.LexicalUnitType;
import org.htmlunit.cssparser.parser.Locator;

/**
 * Validates CSS properties and values for semantic correctness.
 * Provides suggestions for common mistakes and typos.
 *
 * @author Ronald Brill
 */
public class CSSValidator {

    /**
     * Value types for CSS properties.
     */
    public enum ValueType {
        /** COLOR. */
        COLOR,
        /** LENGTH. */
        LENGTH,
        /** PERCENTAGE. */
        PERCENTAGE,
        /** NUMBER. */
        NUMBER,
        /** INTEGER. */
        INTEGER,
        /** URL. */
        URL,
        /** STRING. */
        STRING,
        /** IDENTIFIER. */
        IDENTIFIER,
        /** TIME. */
        TIME,
        /** ANGLE. */
        ANGLE,
        /** FREQUENCY. */
        FREQUENCY,
        /** RESOLUTION. */
        RESOLUTION,
        /** FUNCTION. */
        FUNCTION,
        /** KEYWORD. */
        KEYWORD
    }

    private final Set<String> knownProperties_;
    private final Map<String, Set<ValueType>> propertyValueTypes_;
    private final boolean strict_;

    /**
     * Creates new CSSValidator.
     */
    public CSSValidator() {
        this(false);
    }

    /**
     * Creates new CSSValidator.
     * @param strict if true, throws exceptions on validation errors
     */
    public CSSValidator(final boolean strict) {
        strict_ = strict;
        knownProperties_ = loadKnownProperties();
        propertyValueTypes_ = loadPropertyValueTypes();
    }

    /**
     * Validates a CSS property declaration.
     *
     * @param property The property name
     * @param value The lexical unit value
     * @param locator The locator for error reporting
     * @return List of validation warnings (empty if valid)
     * @throws CSSParseException if validation fails in strict mode
     */
    public List<ValidationWarning> validateProperty(final String property, final LexicalUnit value,
            final Locator locator) {
        final List<ValidationWarning> warnings = new ArrayList<>();

        // Validate property name
        if (!isKnownProperty(property)) {
            final String suggestion = findClosestPropertyMatch(property);
            String message = "Unknown CSS property '" + property + "'.";
            if (suggestion != null) {
                message += " Did you mean '" + suggestion + "'?";
            }

            final ValidationWarning warning = new ValidationWarning(
                message,
                locator,
                ValidationWarning.Level.ERROR
            );
            warnings.add(warning);

            if (strict_) {
                throw new CSSParseException(message, locator);
            }
            return warnings; // Don't validate value if property is unknown
        }

        // Validate value type
        if (value != null && !isValidValueForProperty(property, value)) {
            final Set<ValueType> expectedTypes = propertyValueTypes_.get(property.toLowerCase(Locale.ROOT));
            final String message = "Invalid value type for property '" + property + "'. "
                           + "Expected: " + formatExpectedTypes(expectedTypes)
                           + ", got: " + getValueType(value);

            final ValidationWarning warning = new ValidationWarning(
                message,
                locator,
                ValidationWarning.Level.WARNING
            );
            warnings.add(warning);
        }

        // Validate specific property rules
        warnings.addAll(validatePropertySpecificRules(property, value, locator));

        return warnings;
    }

    /**
     * Checks if a property name is a known CSS property.
     * @param property the property name
     * @return true if known
     */
    public boolean isKnownProperty(final String property) {
        if (property == null) {
            return false;
        }

        // Custom properties (CSS variables) are always valid
        if (property.startsWith("--")) {
            return true;
        }

        // Vendor prefixes are allowed
        if (property.startsWith("-webkit-")
            || property.startsWith("-moz-")
            || property.startsWith("-ms-")
            || property.startsWith("-o-")) {
            return true;
        }

        return knownProperties_.contains(property.toLowerCase(Locale.ROOT));
    }

    /**
     * Finds the closest matching property name using Levenshtein distance.
     * @param property the property name
     * @return the closest match or null
     */
    public String findClosestPropertyMatch(final String property) {
        if (property == null || property.isEmpty()) {
            return null;
        }

        final String normalized = property.toLowerCase(Locale.ROOT);
        String closest = null;
        int minDistance = Integer.MAX_VALUE;

        for (final String known : knownProperties_) {
            final int distance = levenshteinDistance(normalized, known);
            // Only suggest if distance is small (likely a typo)
            if (distance < minDistance && distance <= 2) {
                minDistance = distance;
                closest = known;
            }
        }

        return closest;
    }

    /**
     * Validates if a value is compatible with a property.
     */
    private boolean isValidValueForProperty(final String property, final LexicalUnit value) {
        final Set<ValueType> allowedTypes = propertyValueTypes_.get(property.toLowerCase(Locale.ROOT));
        if (allowedTypes == null) {
            return true; // Unknown property, skip value validation
        }

        // Special keywords are usually valid
        if (isGlobalKeyword(value)) {
            return true;
        }

        final ValueType actualType = getValueType(value);
        return allowedTypes.contains(actualType);
    }

    /**
     * Determines the value type from a LexicalUnit.
     */
    private ValueType getValueType(final LexicalUnit unit) {
        if (unit == null) {
            return ValueType.IDENTIFIER;
        }

        final LexicalUnitType type = unit.getLexicalUnitType();
        if (type == LexicalUnitType.PIXEL
            || type == LexicalUnitType.CENTIMETER
            || type == LexicalUnitType.MILLIMETER
            || type == LexicalUnitType.INCH
            || type == LexicalUnitType.POINT
            || type == LexicalUnitType.PICA
            || type == LexicalUnitType.EM
            || type == LexicalUnitType.REM
            || type == LexicalUnitType.EX
            || type == LexicalUnitType.CH
            || type == LexicalUnitType.VW
            || type == LexicalUnitType.VH
            || type == LexicalUnitType.VMIN
            || type == LexicalUnitType.VMAX) {
            return ValueType.LENGTH;
        }

        if (type == LexicalUnitType.PERCENTAGE) {
            return ValueType.PERCENTAGE;
        }

        if (type == LexicalUnitType.INTEGER) {
            return ValueType.INTEGER;
        }

        if (type == LexicalUnitType.REAL) {
            return ValueType.NUMBER;
        }

        if (type == LexicalUnitType.DEGREE
            || type == LexicalUnitType.RADIAN
            || type == LexicalUnitType.GRADIAN
            || type == LexicalUnitType.TURN) {
            return ValueType.ANGLE;
        }

        if (type == LexicalUnitType.MILLISECOND
            || type == LexicalUnitType.SECOND) {
            return ValueType.TIME;
        }

        if (type == LexicalUnitType.HERTZ
            || type == LexicalUnitType.KILOHERTZ) {
            return ValueType.FREQUENCY;
        }

        if (type == LexicalUnitType.DIMENSION) {
            return ValueType.LENGTH; // Assume length for unknown dimensions
        }

        if (type == LexicalUnitType.URI) {
            return ValueType.URL;
        }

        if (type == LexicalUnitType.STRING_VALUE) {
            return ValueType.STRING;
        }

        if (type == LexicalUnitType.IDENT) {
            return ValueType.IDENTIFIER;
        }

        if (type == LexicalUnitType.RGBCOLOR
            || type == LexicalUnitType.HSLCOLOR
            || type == LexicalUnitType.HWBCOLOR
            || type == LexicalUnitType.LABCOLOR
            || type == LexicalUnitType.LCHCOLOR) {
            return ValueType.COLOR;
        }

        if (type == LexicalUnitType.FUNCTION
            || type == LexicalUnitType.FUNCTION_CALC) {
            return ValueType.FUNCTION;
        }

        return ValueType.IDENTIFIER;
    }

    /**
     * Checks if a value is a global CSS keyword.
     */
    private boolean isGlobalKeyword(final LexicalUnit unit) {
        if (unit.getLexicalUnitType() != LexicalUnitType.IDENT) {
            return false;
        }

        final String value = unit.getStringValue();
        if (value == null) {
            return false;
        }

        final String normalized = value.toLowerCase(Locale.ROOT);
        return normalized.equals("inherit")
               || normalized.equals("initial")
               || normalized.equals("unset")
               || normalized.equals("revert")
               || normalized.equals("revert-layer");
    }

    /**
     * Validates property-specific rules (e.g., color must be valid color).
     */
    private List<ValidationWarning> validatePropertySpecificRules(
            final String property, final LexicalUnit value, final Locator locator) {
        final List<ValidationWarning> warnings = new ArrayList<>();

        // Color properties
        if (isColorProperty(property) && value != null) {
            if (!isValidColor(value)) {
                warnings.add(new ValidationWarning(
                    "Invalid color value for property '" + property + "'",
                    locator,
                    ValidationWarning.Level.WARNING
                ));
            }
        }

        // Length properties can't be negative (for certain properties)
        if (isNonNegativeLengthProperty(property) && value != null) {
            if (isNegativeValue(value)) {
                warnings.add(new ValidationWarning(
                    "Property '" + property + "' cannot have negative values",
                    locator,
                    ValidationWarning.Level.ERROR
                ));
            }
        }

        return warnings;
    }

    /**
     * Calculates Levenshtein distance between two strings.
     */
    private int levenshteinDistance(final String s1, final String s2) {
        final int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                final int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(
                    Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                    dp[i - 1][j - 1] + cost
                );
            }
        }

        return dp[s1.length()][s2.length()];
    }

    /**
     * Loads known CSS properties from resource file.
     */
    private Set<String> loadKnownProperties() {
        final Set<String> properties = new HashSet<>();

        try (InputStream is = getClass().getResourceAsStream("/css-properties.txt")) {
            if (is != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (!line.isEmpty() && !line.startsWith("#")) {
                            properties.add(line.toLowerCase(Locale.ROOT));
                        }
                    }
                }
            }
        }
        catch (final IOException e) {
            // Fallback to hardcoded list
        }

        // Add common properties as fallback
        if (properties.isEmpty()) {
            final String[] commonProps = {
                "color", "background", "background-color", "background-image",
                "background-position", "background-repeat", "background-size",
                "border", "border-color", "border-width", "border-style",
                "border-top", "border-right", "border-bottom", "border-left",
                "margin", "margin-top", "margin-right", "margin-bottom", "margin-left",
                "padding", "padding-top", "padding-right", "padding-bottom", "padding-left",
                "width", "height", "min-width", "max-width", "min-height", "max-height",
                "display", "position", "top", "right", "bottom", "left",
                "float", "clear", "overflow", "overflow-x", "overflow-y",
                "font", "font-family", "font-size", "font-weight", "font-style",
                "text-align", "text-decoration", "text-transform", "line-height",
                "opacity", "visibility", "z-index", "cursor",
                "flex", "flex-direction", "flex-wrap", "justify-content", "align-items",
                "grid", "grid-template-columns", "grid-template-rows", "gap",
                "transition", "transform", "animation"
            };
            properties.addAll(Arrays.asList(commonProps));
        }

        return properties;
    }

    /**
     * Loads property-value type mappings.
     */
    private Map<String, Set<ValueType>> loadPropertyValueTypes() {
        final Map<String, Set<ValueType>> map = new HashMap<>();

        // Color properties
        final Set<ValueType> colorTypes = EnumSet.of(ValueType.COLOR, ValueType.IDENTIFIER);
        map.put("color", colorTypes);
        map.put("background-color", colorTypes);
        map.put("border-color", colorTypes);

        // Length properties
        final Set<ValueType> lengthTypes = EnumSet.of(
            ValueType.LENGTH, ValueType.PERCENTAGE, ValueType.NUMBER, ValueType.IDENTIFIER
        );
        map.put("width", lengthTypes);
        map.put("height", lengthTypes);
        map.put("margin", lengthTypes);
        map.put("padding", lengthTypes);

        return map;
    }

    private boolean isColorProperty(final String property) {
        final String normalized = property.toLowerCase(Locale.ROOT);
        return normalized.contains("color");
    }

    private boolean isNonNegativeLengthProperty(final String property) {
        final String normalized = property.toLowerCase(Locale.ROOT);
        return normalized.equals("width") || normalized.equals("height")
               || normalized.contains("padding");
    }

    private boolean isValidColor(final LexicalUnit value) {
        final ValueType type = getValueType(value);
        return type == ValueType.COLOR || isGlobalKeyword(value);
    }

    private boolean isNegativeValue(final LexicalUnit value) {
        final LexicalUnitType type = value.getLexicalUnitType();
        if (type == LexicalUnitType.INTEGER || type == LexicalUnitType.REAL) {
            return value.getDoubleValue() < 0;
        }
        return false;
    }

    private String formatExpectedTypes(final Set<ValueType> types) {
        if (types == null || types.isEmpty()) {
            return "any value";
        }
        final StringBuilder sb = new StringBuilder();
        int count = 0;
        for (final ValueType t : types) {
            if (count > 0) {
                sb.append(", ");
            }
            sb.append(t.name().toLowerCase(Locale.ROOT));
            count++;
        }
        return sb.toString();
    }
}
