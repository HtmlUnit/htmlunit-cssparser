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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.htmlunit.cssparser.parser.javacc.Token;

/**
 * Tracks parsing context to provide detailed error messages.
 *
 * @author Ronald Brill
 */
public class ParserContext {
    private final Deque<String> ruleStack_ = new ArrayDeque<>();
    private Token currentToken_;
    private final List<String> expectedTokens_ = new ArrayList<>();
    private String currentProperty_;

    /**
     * Enter a new parsing rule context.
     *
     * @param ruleName the name of the rule being entered
     */
    public void enterRule(final String ruleName) {
        ruleStack_.push(ruleName);
    }

    /**
     * Exit the current parsing rule context.
     */
    public void exitRule() {
        if (!ruleStack_.isEmpty()) {
            ruleStack_.pop();
        }
    }

    /**
     * Set the current token being processed.
     *
     * @param token the current token
     */
    public void setCurrentToken(final Token token) {
        currentToken_ = token;
    }

    /**
     * Add an expected token to the list.
     *
     * @param token the expected token description
     */
    public void addExpectedToken(final String token) {
        expectedTokens_.add(token);
    }

    /**
     * Clear the list of expected tokens.
     */
    public void clearExpectedTokens() {
        expectedTokens_.clear();
    }

    /**
     * Set the current property being parsed.
     *
     * @param property the property name
     */
    public void setCurrentProperty(final String property) {
        currentProperty_ = property;
    }

    /**
     * Build a contextual error message by appending context information
     * to the base message.
     *
     * @param baseMessage the base error message
     * @return the contextual error message
     */
    public String buildContextualMessage(final String baseMessage) {
        final StringBuilder sb = new StringBuilder(baseMessage);

        // Add rule context
        if (!ruleStack_.isEmpty()) {
            sb.append(" (in ");
            final List<String> stack = new ArrayList<>(ruleStack_);
            // Reverse to show from root to current
            for (int i = stack.size() - 1; i >= 0; i--) {
                if (i < stack.size() - 1) {
                    sb.append(" > ");
                }
                sb.append(stack.get(i));
            }
            sb.append(")");
        }

        // Add current token info
        if (currentToken_ != null) {
            sb.append(" at '");
            sb.append(currentToken_.image);
            sb.append("'");
        }

        // Add expected tokens
        if (!expectedTokens_.isEmpty()) {
            sb.append(". Expected: ");
            for (int i = 0; i < expectedTokens_.size(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(expectedTokens_.get(i));
            }
        }

        // Add property context if available
        if (currentProperty_ != null) {
            sb.append(" (property: ");
            sb.append(currentProperty_);
            sb.append(")");
        }

        return sb.toString();
    }

    /**
     * Get the current parsing context as a string.
     *
     * @return the current context description
     */
    public String getCurrentContext() {
        if (ruleStack_.isEmpty()) {
            return "root";
        }
        final StringBuilder sb = new StringBuilder();
        final List<String> stack = new ArrayList<>(ruleStack_);
        // Reverse to show from root to current
        for (int i = stack.size() - 1; i >= 0; i--) {
            if (i < stack.size() - 1) {
                sb.append(" > ");
            }
            sb.append(stack.get(i));
        }
        return sb.toString();
    }
}
