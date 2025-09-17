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
package org.htmlunit.cssparser.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.htmlunit.cssparser.parser.condition.Condition;
import org.htmlunit.cssparser.parser.condition.Condition.ConditionType;
import org.htmlunit.cssparser.parser.condition.HasPseudoClassCondition;
import org.htmlunit.cssparser.parser.selector.ChildSelector;
import org.htmlunit.cssparser.parser.selector.ElementSelector;
import org.htmlunit.cssparser.parser.selector.RelativeSelector;
import org.htmlunit.cssparser.parser.selector.Selector;
import org.htmlunit.cssparser.parser.selector.Selector.SelectorType;
import org.htmlunit.cssparser.parser.selector.SelectorList;
import org.junit.jupiter.api.Test;

/**
 * @author Ronald Brill
 */
public class CSS3ParserHasSelectorTest extends AbstractCSSParserTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void hasPlus() throws Exception {
        // element name
        final SelectorList selectors = createSelectors(":has(+ div#topic > #reference)");
        assertEquals("*:has(+ div#topic > *#reference)", selectors.get(0).toString());

        assertEquals(1, selectors.size());
        final Selector selector = selectors.get(0);

        assertEquals(SelectorType.ELEMENT_NODE_SELECTOR, selector.getSelectorType());

        final ElementSelector elemSel = (ElementSelector) selector;
        assertEquals(1, elemSel.getConditions().size());

        final Condition condition = elemSel.getConditions().get(0);
        assertEquals(ConditionType.HAS_PSEUDO_CLASS_CONDITION, condition.getConditionType());

        final HasPseudoClassCondition pseudo = (HasPseudoClassCondition) condition;
        assertEquals("+ div#topic > *#reference", pseudo.getValue());
        assertEquals(":has(+ div#topic > *#reference)", pseudo.toString());

        final SelectorList conditionSelectors = pseudo.getSelectors();
        assertEquals(1, conditionSelectors.size());
        final Selector conditionSelector = conditionSelectors.get(0);
        final RelativeSelector conditionRelativeSelector = (RelativeSelector) conditionSelector;

        final ChildSelector conditionChildSelector = (ChildSelector) conditionRelativeSelector.getSelector();
        assertEquals("div#topic > *#reference", conditionChildSelector.toString());
    }
}
