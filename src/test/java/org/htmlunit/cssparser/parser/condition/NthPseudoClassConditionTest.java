/*
 * Copyright (c) 2019-2026 Ronald Brill.
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
package org.htmlunit.cssparser.parser.condition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.htmlunit.cssparser.parser.condition.Condition.ConditionType;
import org.htmlunit.cssparser.parser.condition.NthPseudoClassCondition.NthType;
import org.junit.jupiter.api.Test;

/**
 * Testcases for {@link NthPseudoClassCondition}.
 *
 * @author Ronald Brill
 */
public class NthPseudoClassConditionTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void values() throws Exception {
        final NthPseudoClassCondition c = new NthPseudoClassCondition(NthType.NTH_LAST_OF_TYPE, "2n + 1", 2, 1, null, null);
        assertEquals(ConditionType.NTH_PSEUDO_CLASS_CONDITION, c.getConditionType());
        assertNull(c.getLocalName());
        assertEquals(NthType.NTH_LAST_OF_TYPE, c.getType());
        assertEquals("2n + 1", c.getNth());
        assertEquals(2, c.getA());
        assertEquals(1, c.getB());
        assertNull(c.getSelectors());
        assertEquals("nth-last-of-type(2n + 1)", c.getValue());
        assertEquals(":nth-last-of-type(2n + 1)", c.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void type() throws Exception {
        assertEquals(NthType.NTH_CHILD, NthType.fromName("nth-child"));
        assertEquals(NthType.NTH_LAST_CHILD, NthType.fromName("NTH-Last-Child"));
        assertEquals(NthType.NTH_OF_TYPE, NthType.fromName("nth-of-type"));
        assertEquals(NthType.NTH_LAST_OF_TYPE, NthType.fromName("nth-last-of-type"));
        assertNull(NthType.fromName("nth-col"));

        assertFalse(NthType.NTH_CHILD.isFromEnd());
        assertFalse(NthType.NTH_CHILD.isOfType());
        assertTrue(NthType.NTH_LAST_CHILD.isFromEnd());
        assertFalse(NthType.NTH_LAST_CHILD.isOfType());
        assertFalse(NthType.NTH_OF_TYPE.isFromEnd());
        assertTrue(NthType.NTH_OF_TYPE.isOfType());
        assertTrue(NthType.NTH_LAST_OF_TYPE.isFromEnd());
        assertTrue(NthType.NTH_LAST_OF_TYPE.isOfType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void matches() throws Exception {
        // 2n+1
        assertMatches(2, 1, 1, 3, 5, 7);
        // 2n
        assertMatches(2, 0, 2, 4, 6, 8);
        // 3
        assertMatches(0, 3, 3);
        // 0
        assertMatches(0, 0);
        // -n+3
        assertMatches(-1, 3, 1, 2, 3);
        // n
        assertMatches(1, 0, 1, 2, 3, 4, 5, 6, 7, 8);
        // 3n-2
        assertMatches(3, -2, 1, 4, 7);
        // -2n+5
        assertMatches(-2, 5, 1, 3, 5);
        // -n-1
        assertMatches(-1, -1);
        // n+6
        assertMatches(1, 6, 6, 7, 8);
        // clamped
        assertMatches(Integer.MAX_VALUE, Integer.MIN_VALUE);
    }

    private static void assertMatches(final int a, final int b, final int... expected) {
        final NthPseudoClassCondition c = new NthPseudoClassCondition(NthType.NTH_CHILD, "", a, b, null, null);
        final StringBuilder matching = new StringBuilder();
        for (int i = 1; i <= 8; i++) {
            if (c.matches(i)) {
                matching.append(i).append(' ');
            }
        }
        final StringBuilder exp = new StringBuilder();
        for (final int e : expected) {
            exp.append(e).append(' ');
        }
        assertEquals(exp.toString(), matching.toString(), "a=" + a + " b=" + b);
    }
}
