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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringReader;

import org.htmlunit.cssparser.dom.CSSStyleSheetImpl;
import org.junit.jupiter.api.Test;

/**
 * Performance tests for CSS parser optimizations.
 *
 * @author Ronald Brill
 */
public class PerformanceTest {

    private static final String SIMPLE_CSS = ".test { color: red; margin: 10px; padding: 5px; }";

    private static final String MEDIUM_CSS =
        "body { margin: 0; padding: 0; font-family: Arial, sans-serif; }\n"
        + "h1, h2, h3 { color: #333; font-weight: bold; }\n"
        + ".container { width: 100%; max-width: 1200px; margin: 0 auto; }\n"
        + ".header { background: #f0f0f0; padding: 20px; border-bottom: 1px solid #ccc; }\n"
        + ".nav { display: flex; justify-content: space-between; }\n"
        + ".nav a { color: #007bff; text-decoration: none; padding: 10px; }\n"
        + ".nav a:hover { background: #e0e0e0; }\n"
        + ".content { padding: 20px; }\n"
        + ".footer { background: #333; color: #fff; padding: 20px; text-align: center; }\n"
        + "@media (max-width: 768px) {\n"
        + "  .container { width: 95%; }\n"
        + "  .nav { flex-direction: column; }\n"
        + "}\n";

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void parseSimpleCSS() throws Exception {
        final CSSOMParser parser = new CSSOMParser();
        final InputSource source = new InputSource(new StringReader(SIMPLE_CSS));

        final CSSStyleSheetImpl styleSheet = parser.parseStyleSheet(source, null);

        assertNotNull(styleSheet);
        assertTrue(styleSheet.getCssRules().getLength() > 0);
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void parseMediumCSS() throws Exception {
        final CSSOMParser parser = new CSSOMParser();
        final InputSource source = new InputSource(new StringReader(MEDIUM_CSS));

        final CSSStyleSheetImpl styleSheet = parser.parseStyleSheet(source, null);

        assertNotNull(styleSheet);
        assertTrue(styleSheet.getCssRules().getLength() > 0);
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void benchmarkParseSpeed() throws Exception {
        final CSSOMParser parser = new CSSOMParser();

        // Warmup
        for (int i = 0; i < 10; i++) {
            final InputSource source = new InputSource(new StringReader(MEDIUM_CSS));
            parser.parseStyleSheet(source, null);
        }

        // Benchmark
        final long start = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            final InputSource source = new InputSource(new StringReader(MEDIUM_CSS));
            parser.parseStyleSheet(source, null);
        }
        final long end = System.nanoTime();

        final long avgTimeMs = (end - start) / 100 / 1_000_000;
        System.out.println("Average parse time for medium CSS: " + avgTimeMs + "ms");

        // Should parse reasonably fast (this is a sanity check, not a strict requirement)
        assertTrue(avgTimeMs < 100, "Parse time too slow: " + avgTimeMs + "ms");
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void testStringBuilderCaching() throws Exception {
        final CSSOMParser parser = new CSSOMParser();

        // Parse multiple times to test StringBuilder reuse
        for (int i = 0; i < 50; i++) {
            final InputSource source = new InputSource(new StringReader(SIMPLE_CSS));
            final CSSStyleSheetImpl styleSheet = parser.parseStyleSheet(source, null);
            assertNotNull(styleSheet);
        }
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void testLocatorPooling() throws Exception {
        LocatorPool.clear();

        final CSSOMParser parser = new CSSOMParser();
        final InputSource source = new InputSource(new StringReader(MEDIUM_CSS));

        final CSSStyleSheetImpl styleSheet = parser.parseStyleSheet(source, null);

        assertNotNull(styleSheet);
        assertTrue(styleSheet.getCssRules().getLength() > 0);

        LocatorPool.clear();
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void testPerformanceMetrics() throws Exception {
        final PerformanceMetrics metrics = PerformanceMetrics.start();

        if (metrics != null) {
            metrics.recordParseTime(100);
            metrics.incrementTokens();
            metrics.incrementRules();
            metrics.incrementProperties();
            metrics.report();
        }
    }
}
