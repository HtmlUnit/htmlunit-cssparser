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

import java.io.Serializable;

import org.htmlunit.cssparser.parser.LexicalUnit;
import org.htmlunit.cssparser.parser.LexicalUnit.LexicalUnitType;
import org.w3c.dom.DOMException;

/**
 * Implementation of Counter.
 *
 * @author Ronald Brill
 */
public class CounterImpl implements Serializable {

    private final String identifier_;
    private String listStyle_;
    private String separator_;

    /**
     * Creates new CounterImpl.
     *
     * @param separatorSpecified true if the separator is specified
     * @param lu the lexical unit
     * @throws DOMException in case of error
     */
    public CounterImpl(final boolean separatorSpecified, final LexicalUnit lu) throws DOMException {
        LexicalUnit next = lu;
        identifier_ = next.getStringValue();
        next = next.getNextLexicalUnit();   // ','
        if (next != null) {
            if (next.getLexicalUnitType() != LexicalUnitType.OPERATOR_COMMA) {
                // error
                throw new DOMException(DOMException.SYNTAX_ERR,
                    "Counter parameters must be separated by ','.");
            }
            next = next.getNextLexicalUnit();
            if (separatorSpecified && (next != null)) {
                separator_ = next.getStringValue();
                next = next.getNextLexicalUnit();   // ','
                if (next != null) {
                    if (next.getLexicalUnitType() != LexicalUnitType.OPERATOR_COMMA) {
                        // error
                        throw new DOMException(DOMException.SYNTAX_ERR,
                            "Counter parameters must be separated by ','.");
                    }
                    next = next.getNextLexicalUnit();
                }
            }
            if (next != null) {
                listStyle_ = next.getStringValue();
                next = next.getNextLexicalUnit();
                if (next != null) {
                    // error
                    throw new DOMException(DOMException.SYNTAX_ERR,
                        "Too many parameters for counter function.");
                }
            }
        }
    }

    /**
     * <p>getIdentifier.</p>
     *
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier_;
    }

    /**
     * <p>getListStyle.</p>
     *
     * @return the list style
     */
    public String getListStyle() {
        return listStyle_;
    }

    /**
     * <p>getSeparator.</p>
     *
     * @return the separator
     */
    public String getSeparator() {
        return separator_;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (separator_ == null) {
            // This is a 'counter()' function
            sb.append("counter(");
        }
        else {
            // This is a 'counters()' function
            sb.append("counters(");
        }
        sb.append(identifier_);
        if (separator_ != null) {
            sb.append(", \"").append(separator_).append("\"");
        }
        if (listStyle_ != null) {
            sb.append(", ").append(listStyle_);
        }
        sb.append(")");
        return sb.toString();
    }
}
