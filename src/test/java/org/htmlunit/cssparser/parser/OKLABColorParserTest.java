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

import org.junit.jupiter.api.Test;

/**
 * @author Ronald Brill
 */
public class OKLABColorParserTest extends AbstractCSSParserTest {

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void oklabBlank() throws Exception {
        color("foreground: oklab(255 0 153)", "foreground: oklab(255 0 153)");
        color("foreground: oklab(255 0 153)", "foreground: oklab(255  0  153.0)");
        color("foreground: oklab(100% 0% 60%)", "foreground: oklab(100% 0% 60%)");
        color("foreground: oklab(100% 0% 60%)", "foreground: oklab(100%  0%  60%)");

        color("foreground: oklab(none 0% 60%)", "foreground: oklab(none 0% 60%)");
        color("foreground: oklab(100% none 60%)", "foreground: oklab(100% none 60%)");
        color("foreground: oklab(100% 0% none)", "foreground: oklab(100%  0%  none)");

        color("foreground: oklab(255 0 153)", "foreground: oklab(2.55e2 0e0 1.53e2)");

        // alpha
        color("foreground: oklab(10 20 30 / 0.1)", "foreground: oklab(10 20 30/0.1)");
        color("foreground: oklab(10 20 30 / 0.1)", "foreground: oklab( 10  20 30 / 0.1 )");
        color("foreground: oklab(10 20 30 / 0.7)", "foreground: oklab( 10  20 30 / .7 )");
        color("foreground: oklab(10 20 30 / 10%)", "foreground: oklab(10 20 30 / 10%)");

        color("foreground: oklab(10% 20% 30% / 7%)", "foreground: oklab(10% 20% 30% / 7%)");
        color("foreground: oklab(10% 20% 30% / 0.13%)", "foreground: oklab(10% 20% 30% / 1.3e-1%)");
        color("foreground: oklab(10% 20% 30% / 0.5)", "foreground: oklab(10% 20% 30% / 0.5)");

        color("foreground: oklab(10% 20% 30% / none)", "foreground: oklab(10% 20% 30% / none)");

        // var
        color("foreground: oklab(var(--v-l) var(--v-a) var(--v-b) / var(--v-alpha))", "foreground: oklab(var(--v-l) var(--v-a) var(--v-b) / var(--v-alpha))");

        // calc
        color("foreground: oklab(calc(270) calc(60%) calc(70%) / calc(0.1))", "foreground: oklab(calc(270) calc(60%) calc(70%) / calc(0.1))");
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void oklabMixed() throws Exception {
        color("foreground: oklab(42 128 255)", "foreground: oklab(42 128 255)");
        color("foreground: oklab(42% 128 255)", "foreground: oklab(42% 128 255)");
        color("foreground: oklab(42 128% 255)", "foreground: oklab(42 128% 255)");
        color("foreground: oklab(42 128 255%)", "foreground: oklab(42 128 255%)");
        color("foreground: oklab(42% 128% 255)", "foreground: oklab(42% 128% 255)");
        color("foreground: oklab(42 128% 255%)", "foreground: oklab(42 128% 255%)");
        color("foreground: oklab(42% 128% 255%)", "foreground: oklab(42% 128% 255%)");
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void oklabRelative() throws Exception {
        color("foreground: oklab(from red l a b)", "foreground: oklab(from red l a b)");
        color("foreground: oklab(from red l a b / alpha)", "foreground: oklab(from red l a b / alpha)");

        color("foreground: oklab(from red 7 11% 13%)", "foreground: oklab(from red 7 11% 13%)");
        color("foreground: oklab(from red 7 11% 13% / 0.7)", "foreground: oklab(from red 7 11% 13% / 0.7)");

        color("foreground: oklab(from rgb(200 170 0) 7 11% 13%)", "foreground: oklab(from rgb(200 170 0) 7 11% 13%)");
        color("foreground: oklab(from var(--base-color) 7 11% 13%)", "foreground: oklab(from var(--base-color) 7 11% 13%)");

        color("foreground: oklab(from red calc(l + 7) calc(a - 11%) calc(b * 13%))", "foreground: oklab(from red calc(l + 7) calc(a - 11%) calc(b * 13%))");
        color("foreground: oklab(from red calc(l + 7) calc(a - 11%) calc(b * 13%) / calc(alpha / 2))", "foreground: oklab(from red calc(l + 7) calc(a - 11%) calc(b * 13%) / calc(alpha / 2))");
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void oklabVariousErrors() throws Exception {
        // like browsers we ignore many errors during parsing

        color(1, "Error in expression. (Invalid token \",\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: oklab(10, 20 30)");

        color("foreground: oklab(10 20 30 40)", "foreground: oklab(10 20 30 40)");

        color(1, "Error in expression. (Invalid token \"-none\". Was expecting one of: <S>, <NUMBER>, \"none\", \"from\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: oklab(-none 20 30)");
        color(1, "Error in expression. (Invalid token \"-none\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: oklab(10 -none 30)");
        color(1, "Error in expression. (Invalid token \"-none\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: oklab(10 20 -none)");
        color(1, "Error in expression. (Invalid token \"-none\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: oklab(10 20 30 / -none)");

        color(1, "Error in expression. (Invalid token \")\". Was expecting one of: <S>, <NUMBER>, \"none\", \"from\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: oklab()");
        color(1, "Error in expression. (Invalid token \")\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: oklab(10)");
        color(1, "Error in expression. (Invalid token \")\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: oklab(10 20)");

        color(1, "Error in expression. (Invalid token \")\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: oklab(10 20 30/)");

        color(1, "Error in expression. (Invalid token \"20\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: oklab(10 20px 30)");

        color(1, "Error in expression. (Invalid token \"10\". Was expecting one of: <S>, <NUMBER>, \"none\", \"from\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: oklab('10' 20 30)");
    }
}
