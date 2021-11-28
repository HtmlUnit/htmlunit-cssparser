/*
 * Copyright (c) 2019-2021 Ronald Brill.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.css.dom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMException;

import com.gargoylesoftware.css.parser.CSSOMParser;
import com.gargoylesoftware.css.parser.LexicalUnit;
import com.gargoylesoftware.css.parser.LexicalUnitImpl;

/**
 * Unit tests for {@link RectImpl}.
 *
 * @author Ronald Brill
 */
public class RectImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLU() throws Exception {
        final LexicalUnit rectLU = LexicalUnitImpl.createNumber(null, 10);
        LexicalUnit lu = LexicalUnitImpl.createComma(rectLU);
        lu = LexicalUnitImpl.createNumber(lu, 20);
        lu = LexicalUnitImpl.createComma(lu);
        lu = LexicalUnitImpl.createNumber(lu, 30);
        lu = LexicalUnitImpl.createComma(lu);
        lu = LexicalUnitImpl.createNumber(lu, 40);

        final RectImpl rect = new RectImpl(rectLU);
        assertEquals("rect(10, 20, 30, 40)", rect.toString());
        assertEquals("10", rect.getTop().getCssText());
        assertEquals("20", rect.getRight().getCssText());
        assertEquals("30", rect.getBottom().getCssText());
        assertEquals("40", rect.getLeft().getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLUWithoutComma() throws Exception {
        final LexicalUnit rectLU = LexicalUnitImpl.createNumber(null, 10);
        LexicalUnit lu = LexicalUnitImpl.createNumber(rectLU, 20);
        lu = LexicalUnitImpl.createNumber(lu, 30);
        lu = LexicalUnitImpl.createNumber(lu, 40);

        final RectImpl rect = new RectImpl(rectLU);
        assertEquals("rect(10, 20, 30, 40)", rect.toString());
        assertEquals("10", rect.getTop().getCssText());
        assertEquals("20", rect.getRight().getCssText());
        assertEquals("30", rect.getBottom().getCssText());
        assertEquals("40", rect.getLeft().getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLUMissingValues() throws Exception {
        LexicalUnit rectLU = LexicalUnitImpl.createNumber(null, 10);

        try {
            new RectImpl(rectLU);
            fail("DOMException expected");
        }
        catch (final DOMException e) {
            assertEquals("Rect misses second parameter.", e.getMessage());
        }

        rectLU = LexicalUnitImpl.createNumber(null, 10);
        LexicalUnit lu = LexicalUnitImpl.createNumber(rectLU, 20);
        lu = LexicalUnitImpl.createNumber(lu, 30);

        try {
            new RectImpl(rectLU);
            fail("DOMException expected");
        }
        catch (final DOMException e) {
            assertEquals("Rect misses fourth parameter.", e.getMessage());
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLUWithoutOneComma() throws Exception {
        final LexicalUnit rectLU = LexicalUnitImpl.createNumber(null, 10);
        LexicalUnit lu = LexicalUnitImpl.createComma(rectLU);
        lu = LexicalUnitImpl.createNumber(lu, 20);
        lu = LexicalUnitImpl.createNumber(lu, 30);
        lu = LexicalUnitImpl.createNumber(lu, 40);

        try {
            new RectImpl(rectLU);
            fail("DOMException expected");
        }
        catch (final DOMException e) {
            assertEquals("All or none rect parameters must be separated by ','.", e.getMessage());
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLUWithoutLateComma() throws Exception {
        final LexicalUnit rectLU = LexicalUnitImpl.createNumber(null, 10);
        LexicalUnit lu = LexicalUnitImpl.createNumber(rectLU, 20);
        lu = LexicalUnitImpl.createComma(lu);
        lu = LexicalUnitImpl.createNumber(lu, 30);
        lu = LexicalUnitImpl.createNumber(lu, 40);

        try {
            new RectImpl(rectLU);
            fail("DOMException expected");
        }
        catch (final DOMException e) {
            assertEquals("All or none rect parameters must be separated by ','.", e.getMessage());
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLUTooManyValuesException() throws Exception {
        final LexicalUnit rectLU = LexicalUnitImpl.createNumber(null, 10);
        LexicalUnit lu = LexicalUnitImpl.createComma(rectLU);
        lu = LexicalUnitImpl.createNumber(lu, 20);
        lu = LexicalUnitImpl.createComma(lu);
        lu = LexicalUnitImpl.createNumber(lu, 30);
        lu = LexicalUnitImpl.createComma(lu);
        lu = LexicalUnitImpl.createNumber(lu, 40);
        lu = LexicalUnitImpl.createComma(lu);

        try {
            new RectImpl(rectLU);
            fail("DOMException expected");
        }
        catch (final DOMException e) {
            assertEquals("Too many parameters for rect function.", e.getMessage());
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseRule() throws Exception {
        final String testRule = "img { clip: rect(1px, 2px, -3px, 4px); }";
        final AbstractCSSRuleImpl rule = new CSSOMParser().parseRule(testRule);

        assertEquals(testRule, rule.getCssText());

        final CSSStyleDeclarationImpl style = ((CSSStyleRuleImpl) rule).getStyle();
        final Property prop = style.getPropertyDeclaration("clip");
        final RectImpl rect = (RectImpl) prop.getValue().getValue();

        assertEquals("1px", rect.getTop().toString());
        assertEquals("2px", rect.getRight().toString());
        assertEquals("-3px", rect.getBottom().toString());
        assertEquals("4px", rect.getLeft().toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssText() throws Exception {
        final LexicalUnit rectLU = LexicalUnitImpl.createNumber(null, 10);
        LexicalUnit lu = LexicalUnitImpl.createComma(rectLU);
        lu = LexicalUnitImpl.createNumber(lu, 20);
        lu = LexicalUnitImpl.createComma(lu);
        lu = LexicalUnitImpl.createNumber(lu, 30);
        lu = LexicalUnitImpl.createComma(lu);
        lu = LexicalUnitImpl.createNumber(lu, 40);

        final RectImpl rect = new RectImpl(rectLU);

        assertEquals("rect(10, 20, 30, 40)", rect.toString());
    }
}
