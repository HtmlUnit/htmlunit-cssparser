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
package org.htmlunit.cssparser.dom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link AbstractCSSRuleImpl}.
 *
 * @author Ronald Brill
 */
public class AbstractCSSRuleImplTest {

    /**
     * Test parent style sheet getter/setter.
     * @throws Exception if any error occurs
     */
    @Test
    public void parentStyleSheet() throws Exception {
        final CSSStyleSheetImpl styleSheet = new CSSStyleSheetImpl();
        final CSSStyleRuleImpl rule = new CSSStyleRuleImpl(styleSheet, null, null);
        
        assertEquals(styleSheet, rule.getParentStyleSheet());
        
        final CSSStyleSheetImpl newStyleSheet = new CSSStyleSheetImpl();
        rule.setParentStyleSheet(newStyleSheet);
        
        assertEquals(newStyleSheet, rule.getParentStyleSheet());
    }

    /**
     * Test parent rule getter/setter.
     * @throws Exception if any error occurs
     */
    @Test
    public void parentRule() throws Exception {
        final CSSStyleSheetImpl styleSheet = new CSSStyleSheetImpl();
        final CSSPageRuleImpl parentRule = new CSSPageRuleImpl(styleSheet, null, null);
        final CSSStyleRuleImpl rule = new CSSStyleRuleImpl(styleSheet, parentRule, null);
        
        assertEquals(parentRule, rule.getParentRule());
        
        final CSSPageRuleImpl newParentRule = new CSSPageRuleImpl(styleSheet, null, null);
        rule.setParentRule(newParentRule);
        
        assertEquals(newParentRule, rule.getParentRule());
    }

    /**
     * Test with null parent style sheet.
     * @throws Exception if any error occurs
     */
    @Test
    public void nullParentStyleSheet() throws Exception {
        final CSSStyleRuleImpl rule = new CSSStyleRuleImpl(null, null, null);
        
        assertNull(rule.getParentStyleSheet());
    }

    /**
     * Test with null parent rule.
     * @throws Exception if any error occurs
     */
    @Test
    public void nullParentRule() throws Exception {
        final CSSStyleSheetImpl styleSheet = new CSSStyleSheetImpl();
        final CSSStyleRuleImpl rule = new CSSStyleRuleImpl(styleSheet, null, null);
        
        assertNull(rule.getParentRule());
    }

    /**
     * Test equals with same object.
     * @throws Exception if any error occurs
     */
    @Test
    public void equalsSameObject() throws Exception {
        final CSSStyleSheetImpl styleSheet = new CSSStyleSheetImpl();
        final CSSStyleRuleImpl rule = new CSSStyleRuleImpl(styleSheet, null, null);
        
        assertTrue(rule.equals(rule));
    }

    /**
     * Test equals with different types.
     * @throws Exception if any error occurs
     */
    @Test
    public void equalsDifferentType() throws Exception {
        final CSSStyleSheetImpl styleSheet = new CSSStyleSheetImpl();
        final CSSStyleRuleImpl rule = new CSSStyleRuleImpl(styleSheet, null, null);
        
        assertFalse(rule.equals("not a rule"));
        assertFalse(rule.equals(null));
    }

    /**
     * Test that parent relationships don't cause stack overflow in equals.
     * @throws Exception if any error occurs
     */
    @Test
    public void equalsNoStackOverflow() throws Exception {
        final CSSStyleSheetImpl styleSheet = new CSSStyleSheetImpl();
        final CSSPageRuleImpl parent1 = new CSSPageRuleImpl(styleSheet, null, null);
        final CSSStyleRuleImpl rule1 = new CSSStyleRuleImpl(styleSheet, parent1, null);
        
        final CSSPageRuleImpl parent2 = new CSSPageRuleImpl(styleSheet, null, null);
        final CSSStyleRuleImpl rule2 = new CSSStyleRuleImpl(styleSheet, parent2, null);
        
        // This should not cause stack overflow even with parent relationships
        rule1.equals(rule2);
    }

    /**
     * Test hashCode consistency.
     * @throws Exception if any error occurs
     */
    @Test
    public void hashCodeConsistency() throws Exception {
        final CSSStyleSheetImpl styleSheet = new CSSStyleSheetImpl();
        final CSSStyleRuleImpl rule = new CSSStyleRuleImpl(styleSheet, null, null);
        
        final int hash1 = rule.hashCode();
        final int hash2 = rule.hashCode();
        
        assertEquals(hash1, hash2);
    }

    /**
     * Test that parent relationships don't cause stack overflow in hashCode.
     * @throws Exception if any error occurs
     */
    @Test
    public void hashCodeNoStackOverflow() throws Exception {
        final CSSStyleSheetImpl styleSheet = new CSSStyleSheetImpl();
        final CSSPageRuleImpl parent = new CSSPageRuleImpl(styleSheet, null, null);
        final CSSStyleRuleImpl rule = new CSSStyleRuleImpl(styleSheet, parent, null);
        
        // This should not cause stack overflow
        final int hash = rule.hashCode();
        assertNotEquals(0, hash); // Just verify it computed something
    }

    /**
     * Test changing parent style sheet after construction.
     * @throws Exception if any error occurs
     */
    @Test
    public void changeParentStyleSheet() throws Exception {
        final CSSStyleSheetImpl styleSheet1 = new CSSStyleSheetImpl();
        final CSSStyleRuleImpl rule = new CSSStyleRuleImpl(styleSheet1, null, null);
        
        assertEquals(styleSheet1, rule.getParentStyleSheet());
        
        final CSSStyleSheetImpl styleSheet2 = new CSSStyleSheetImpl();
        rule.setParentStyleSheet(styleSheet2);
        
        assertEquals(styleSheet2, rule.getParentStyleSheet());
        
        rule.setParentStyleSheet(null);
        assertNull(rule.getParentStyleSheet());
    }

    /**
     * Test changing parent rule after construction.
     * @throws Exception if any error occurs
     */
    @Test
    public void changeParentRule() throws Exception {
        final CSSStyleSheetImpl styleSheet = new CSSStyleSheetImpl();
        final CSSPageRuleImpl parent1 = new CSSPageRuleImpl(styleSheet, null, null);
        final CSSStyleRuleImpl rule = new CSSStyleRuleImpl(styleSheet, parent1, null);
        
        assertEquals(parent1, rule.getParentRule());
        
        final CSSPageRuleImpl parent2 = new CSSPageRuleImpl(styleSheet, null, null);
        rule.setParentRule(parent2);
        
        assertEquals(parent2, rule.getParentRule());
        
        rule.setParentRule(null);
        assertNull(rule.getParentRule());
    }
}
