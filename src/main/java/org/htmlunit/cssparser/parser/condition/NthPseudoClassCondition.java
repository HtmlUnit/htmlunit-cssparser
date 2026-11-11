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

import java.io.Serializable;
import java.util.Locale;

import org.htmlunit.cssparser.parser.AbstractLocatable;
import org.htmlunit.cssparser.parser.Locator;
import org.htmlunit.cssparser.parser.selector.SelectorList;

/**
 * Implementation of {@link Condition} for the {@code :nth-child()}, {@code :nth-last-child()},
 * {@code :nth-of-type()} and {@code :nth-last-of-type()} pseudo-classes.
 * The {@code An+B} argument is already parsed; the optional {@code of S} selector list
 * is supported by {@code :nth-child()} and {@code :nth-last-child()} only.
 *
 * @author Ronald Brill
 */
public class NthPseudoClassCondition extends AbstractLocatable implements Condition, Serializable {

    /**
     * The supported nth pseudo-classes.
     */
    public enum NthType {
        /** {@code :nth-child()}. */
        NTH_CHILD("nth-child", false, false),

        /** {@code :nth-last-child()}. */
        NTH_LAST_CHILD("nth-last-child", true, false),

        /** {@code :nth-of-type()}. */
        NTH_OF_TYPE("nth-of-type", false, true),

        /** {@code :nth-last-of-type()}. */
        NTH_LAST_OF_TYPE("nth-last-of-type", true, true);

        private final String name_;
        private final boolean fromEnd_;
        private final boolean ofType_;

        NthType(final String name, final boolean fromEnd, final boolean ofType) {
            name_ = name;
            fromEnd_ = fromEnd;
            ofType_ = ofType;
        }

        /**
         * Returns the name of the pseudo-class.
         *
         * @return the name e.g. {@code nth-child}
         */
        public String getName() {
            return name_;
        }

        /**
         * Returns whether the elements are counted from the end (the {@code nth-last-xxx()} pseudo-classes).
         *
         * @return true if the elements are counted from the end
         */
        public boolean isFromEnd() {
            return fromEnd_;
        }

        /**
         * Returns whether only the siblings of the same type are counted
         * (the {@code nth-xxx-of-type()} pseudo-classes).
         *
         * @return true if only the siblings of the same type are counted
         */
        public boolean isOfType() {
            return ofType_;
        }

        /**
         * Returns the type for the given pseudo-class name.
         *
         * @param name the name of the pseudo-class (ASCII case-insensitive)
         * @return the type or {@code null} if the name is not one of the nth pseudo-classes
         */
        public static NthType fromName(final String name) {
            final String lcName = name.toLowerCase(Locale.ROOT);
            for (final NthType type : values()) {
                if (type.name_.equals(lcName)) {
                    return type;
                }
            }
            return null;
        }
    }

    private final NthType type_;
    private final String nth_;
    private final int aVal_;
    private final int bVal_;
    private final SelectorList selectors_;

    /**
     * Ctor.
     *
     * @param type the type of the pseudo-class
     * @param nth the {@code An+B} argument as written in the source (used for serialization)
     * @param a the step size {@code A}
     * @param b the offset {@code B}
     * @param selectors the selector list following the {@code of} keyword; may be {@code null}
     * @param locator the locator
     */
    public NthPseudoClassCondition(final NthType type, final String nth, final int a, final int b,
            final SelectorList selectors, final Locator locator) {
        type_ = type;
        nth_ = nth;
        aVal_ = a;
        bVal_ = b;
        selectors_ = selectors;
        setLocator(locator);
    }

    /** {@inheritDoc} */
    @Override
    public ConditionType getConditionType() {
        return ConditionType.NTH_PSEUDO_CLASS_CONDITION;
    }

    /** {@inheritDoc} */
    @Override
    public String getLocalName() {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @return the pseudo-class without the leading colon e.g. {@code nth-child(2n+1 of *.x)}
     */
    @Override
    public String getValue() {
        if (selectors_ == null) {
            return type_.getName() + "(" + nth_ + ")";
        }
        return type_.getName() + "(" + nth_ + " of " + selectors_ + ")";
    }

    /**
     * Returns the type of the pseudo-class.
     *
     * @return the type
     */
    public NthType getType() {
        return type_;
    }

    /**
     * Returns the {@code An+B} argument as written in the source.
     *
     * @return the argument e.g. {@code 2n+1} or {@code odd}
     */
    public String getNth() {
        return nth_;
    }

    /**
     * Returns the step size {@code A} of the {@code An+B} argument ({@code 2} for {@code odd} and {@code even}).
     *
     * @return the step size
     */
    public int getA() {
        return aVal_;
    }

    /**
     * Returns the offset {@code B} of the {@code An+B} argument
     * ({@code 1} for {@code odd}, {@code 0} for {@code even}).
     *
     * @return the offset
     */
    public int getB() {
        return bVal_;
    }

    /**
     * Returns the selector list following the {@code of} keyword.
     *
     * @return the list of selectors or {@code null} if there is no {@code of S} part
     */
    public SelectorList getSelectors() {
        return selectors_;
    }

    /**
     * Returns whether the given position is selected by the {@code An+B} argument,
     * i.e. if there is an integer {@code n >= 0} with {@code A*n + B == position}.
     *
     * @param position the 1-based position of the element among the counted siblings
     * @return true if the position is selected
     */
    public boolean matches(final int position) {
        if (aVal_ == 0) {
            return position == bVal_;
        }
        final long diff = (long) position - bVal_;
        return diff % aVal_ == 0 && diff / aVal_ >= 0;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ":" + getValue();
    }
}
