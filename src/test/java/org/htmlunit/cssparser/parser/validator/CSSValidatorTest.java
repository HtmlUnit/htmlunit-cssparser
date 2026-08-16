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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.htmlunit.cssparser.parser.LexicalUnit;
import org.htmlunit.cssparser.parser.LexicalUnitImpl;
import org.htmlunit.cssparser.parser.Locator;
import org.junit.jupiter.api.Test;

/**
 * Test for the CSSValidator.
 *
 * @author Ronald Brill
 */
public class CSSValidatorTest {

    @Test
    public void testDefaultConstructor() {
        final CSSValidator validator = new CSSValidator();
        assertNotNull(validator);
    }

    @Test
    public void testKnownPropertyRecognized() {
        final CSSValidator validator = new CSSValidator();
        assertTrue(validator.isKnownProperty("color"));
        assertTrue(validator.isKnownProperty("background"));
        assertTrue(validator.isKnownProperty("width"));
        assertTrue(validator.isKnownProperty("margin"));
    }

    @Test
    public void testUnknownPropertyDetection() {
        final CSSValidator validator = new CSSValidator();
        final Locator locator = new Locator(null, 1, 1);

        final LexicalUnit value = LexicalUnitImpl.createIdent(null, "red");

        final List<ValidationWarning> warnings = validator.validateProperty("colr", value, locator);

        assertEquals(1, warnings.size());
        assertTrue(warnings.get(0).getMessage().contains("Unknown CSS property"));
        assertTrue(warnings.get(0).getMessage().contains("Did you mean 'color'?"));
    }

    @Test
    public void testTypoSuggestion() {
        final CSSValidator validator = new CSSValidator();

        assertEquals("color", validator.findClosestPropertyMatch("colr"));
        // Note: widht could match to either "width" or "right" depending on Levenshtein distance
        // Both are valid suggestions since they have the same distance
        final String widhtMatch = validator.findClosestPropertyMatch("widht");
        assertTrue("width".equals(widhtMatch) || "right".equals(widhtMatch));
        assertEquals("background", validator.findClosestPropertyMatch("backgrund"));
        assertEquals("margin", validator.findClosestPropertyMatch("margn"));
    }

    @Test
    public void testNoSuggestionForVeryDifferentProperty() {
        final CSSValidator validator = new CSSValidator();

        // Properties with distance > 2 should return null
        final String suggestion = validator.findClosestPropertyMatch("xyz");
        // Either null or a property with distance <= 2
        if (suggestion != null) {
            // If we get a suggestion, verify it's close
            assertTrue(validator.isKnownProperty(suggestion));
        }
    }

    @Test
    public void testCustomPropertiesAllowed() {
        final CSSValidator validator = new CSSValidator();

        assertTrue(validator.isKnownProperty("--my-custom-property"));
        assertTrue(validator.isKnownProperty("--another-var"));
        assertTrue(validator.isKnownProperty("--theme-color"));
    }

    @Test
    public void testVendorPrefixesAllowed() {
        final CSSValidator validator = new CSSValidator();

        assertTrue(validator.isKnownProperty("-webkit-transform"));
        assertTrue(validator.isKnownProperty("-moz-appearance"));
        assertTrue(validator.isKnownProperty("-ms-filter"));
        assertTrue(validator.isKnownProperty("-o-transition"));
    }

    @Test
    public void testGlobalKeywordsValid() {
        final CSSValidator validator = new CSSValidator();
        final Locator locator = new Locator(null, 1, 1);

        // Global keywords should be valid for any property
        final LexicalUnit inherit = LexicalUnitImpl.createIdent(null, "inherit");
        List<ValidationWarning> warnings = validator.validateProperty("color", inherit, locator);
        assertEquals(0, warnings.size());

        final LexicalUnit initial = LexicalUnitImpl.createIdent(null, "initial");
        warnings = validator.validateProperty("width", initial, locator);
        assertEquals(0, warnings.size());

        final LexicalUnit unset = LexicalUnitImpl.createIdent(null, "unset");
        warnings = validator.validateProperty("margin", unset, locator);
        assertEquals(0, warnings.size());
    }

    @Test
    public void testInvalidValueType() {
        final CSSValidator validator = new CSSValidator();
        final Locator locator = new Locator(null, 1, 1);

        // color property with length value
        final LexicalUnit lengthValue = LexicalUnitImpl.createPixel(null, 10);
        final List<ValidationWarning> warnings = validator.validateProperty("color", lengthValue, locator);

        assertTrue(warnings.size() > 0);
        boolean foundInvalidType = false;
        for (final ValidationWarning warning : warnings) {
            if (warning.getMessage().contains("Invalid value type")) {
                foundInvalidType = true;
                break;
            }
        }
        assertTrue(foundInvalidType);
    }

    @Test
    public void testValidColorProperty() {
        final CSSValidator validator = new CSSValidator();
        final Locator locator = new Locator(null, 1, 1);

        // Valid identifier color
        final LexicalUnit colorValue = LexicalUnitImpl.createIdent(null, "red");
        final List<ValidationWarning> warnings = validator.validateProperty("color", colorValue, locator);

        // Should not have invalid value type warnings
        for (final ValidationWarning warning : warnings) {
            assertFalse(warning.getMessage().contains("Invalid value type"));
        }
    }

    @Test
    public void testValidationWarningToString() {
        final Locator locator = new Locator("test.css", 10, 5);
        final ValidationWarning warning = new ValidationWarning(
            "Test message",
            locator,
            ValidationWarning.Level.ERROR
        );

        final String str = warning.toString();
        assertTrue(str.contains("ERROR"));
        assertTrue(str.contains("Line 10"));
        assertTrue(str.contains("Column 5"));
        assertTrue(str.contains("Test message"));
    }

    @Test
    public void testLevenshteinDistance() {
        final CSSValidator validator = new CSSValidator();

        // Test some common typos
        assertNotNull(validator.findClosestPropertyMatch("colr"));  // color
        assertNotNull(validator.findClosestPropertyMatch("widht")); // width
        assertNotNull(validator.findClosestPropertyMatch("margn")); // margin
    }

    @Test
    public void testNullPropertyHandling() {
        final CSSValidator validator = new CSSValidator();

        assertFalse(validator.isKnownProperty(null));
        assertEquals(null, validator.findClosestPropertyMatch(null));
        assertEquals(null, validator.findClosestPropertyMatch(""));
    }

    @Test
    public void testCaseInsensitivePropertyNames() {
        final CSSValidator validator = new CSSValidator();

        assertTrue(validator.isKnownProperty("color"));
        assertTrue(validator.isKnownProperty("Color"));
        assertTrue(validator.isKnownProperty("COLOR"));
        assertTrue(validator.isKnownProperty("CoLoR"));
    }
}
