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

/**
 * Optional performance metrics for the CSS parser.
 * Enable with -Dhtmlunit.cssparser.metrics=true
 *
 * @author Ronald Brill
 */
public class PerformanceMetrics {

    private static final boolean ENABLED =
        Boolean.getBoolean("htmlunit.cssparser.metrics");

    private long parseTimeMs_;
    private int tokenCount_;
    private int ruleCount_;
    private int propertyCount_;

    /**
     * Creates a new PerformanceMetrics instance if enabled.
     *
     * @return a new instance or null if disabled
     */
    public static PerformanceMetrics start() {
        return ENABLED ? new PerformanceMetrics() : null;
    }

    /**
     * Records the parse time.
     *
     * @param ms the time in milliseconds
     */
    public void recordParseTime(final long ms) {
        if (ENABLED) {
            parseTimeMs_ = ms;
        }
    }

    /**
     * Increments the token count.
     */
    public void incrementTokens() {
        if (ENABLED) {
            tokenCount_++;
        }
    }

    /**
     * Increments the rule count.
     */
    public void incrementRules() {
        if (ENABLED) {
            ruleCount_++;
        }
    }

    /**
     * Increments the property count.
     */
    public void incrementProperties() {
        if (ENABLED) {
            propertyCount_++;
        }
    }

    /**
     * Prints the metrics report to System.out.
     */
    public void report() {
        if (ENABLED) {
            System.out.println("=== CSS Parser Performance Metrics ===");
            System.out.println("Parse time: " + parseTimeMs_ + "ms");
            System.out.println("Tokens: " + tokenCount_);
            System.out.println("Rules: " + ruleCount_);
            System.out.println("Properties: " + propertyCount_);
            System.out.println("=====================================");
        }
    }

    /**
     * @return the parse time in milliseconds
     */
    public long getParseTimeMs() {
        return parseTimeMs_;
    }

    /**
     * @return the token count
     */
    public int getTokenCount() {
        return tokenCount_;
    }

    /**
     * @return the rule count
     */
    public int getRuleCount() {
        return ruleCount_;
    }

    /**
     * @return the property count
     */
    public int getPropertyCount() {
        return propertyCount_;
    }
}
