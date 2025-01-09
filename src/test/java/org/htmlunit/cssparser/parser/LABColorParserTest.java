/*
 * Copyright (c) 2019-2024 Ronald Brill.
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
package org.htmlunit.cssparser.parser;

import org.junit.jupiter.api.Test;

/**
 * @author Ronald Brill
 */
public class LABColorParserTest extends AbstractCSSParserTest {

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void labBlank() throws Exception {
        color("foreground: lab(255 0 153)", "foreground: lab(255 0 153)");
        color("foreground: lab(255 0 153)", "foreground: lab(255  0  153.0)");
        color("foreground: lab(100% 0% 60%)", "foreground: lab(100% 0% 60%)");
        color("foreground: lab(100% 0% 60%)", "foreground: lab(100%  0%  60%)");

        color("foreground: lab(none 0% 60%)", "foreground: lab(none 0% 60%)");
        color("foreground: lab(100% none 60%)", "foreground: lab(100% none 60%)");
        color("foreground: lab(100% 0% none)", "foreground: lab(100%  0%  none)");

        color("foreground: lab(255 0 153)", "foreground: lab(2.55e2 0e0 1.53e2)");

        // alpha
        color("foreground: lab(10 20 30 / 0.1)", "foreground: lab(10 20 30/0.1)");
        color("foreground: lab(10 20 30 / 0.1)", "foreground: lab( 10  20 30 / 0.1 )");
        color("foreground: lab(10 20 30 / 0.7)", "foreground: lab( 10  20 30 / .7 )");
        color("foreground: lab(10 20 30 / 10%)", "foreground: lab(10 20 30 / 10%)");

        color("foreground: lab(10% 20% 30% / 7%)", "foreground: lab(10% 20% 30% / 7%)");
        color("foreground: lab(10% 20% 30% / 0.13%)", "foreground: lab(10% 20% 30% / 1.3e-1%)");
        color("foreground: lab(10% 20% 30% / 0.5)", "foreground: lab(10% 20% 30% / 0.5)");

        color("foreground: lab(10% 20% 30% / none)", "foreground: lab(10% 20% 30% / none)");

        // var
        color("foreground: lab(var(--v-l) var(--v-a) var(--v-b) / var(--v-alpha))", "foreground: lab(var(--v-l) var(--v-a) var(--v-b) / var(--v-alpha))");

        // calc
        color("foreground: lab(calc(270) calc(60%) calc(70%) / calc(0.1))", "foreground: lab(calc(270) calc(60%) calc(70%) / calc(0.1))");
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void labMixed() throws Exception {
        color("foreground: lab(42 128 255)", "foreground: lab(42 128 255)");
        color("foreground: lab(42% 128 255)", "foreground: lab(42% 128 255)");
        color("foreground: lab(42 128% 255)", "foreground: lab(42 128% 255)");
        color("foreground: lab(42 128 255%)", "foreground: lab(42 128 255%)");
        color("foreground: lab(42% 128% 255)", "foreground: lab(42% 128% 255)");
        color("foreground: lab(42 128% 255%)", "foreground: lab(42 128% 255%)");
        color("foreground: lab(42% 128% 255%)", "foreground: lab(42% 128% 255%)");
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void labRelative() throws Exception {
        color("foreground: lab(from red l a b)", "foreground: lab(from red l a b)");
        color("foreground: lab(from red l a b / alpha)", "foreground: lab(from red l a b / alpha)");

        color("foreground: lab(from red 7 11% 13%)", "foreground: lab(from red 7 11% 13%)");
        color("foreground: lab(from red 7 11% 13% / 0.7)", "foreground: lab(from red 7 11% 13% / 0.7)");

        color("foreground: lab(from rgb(200 170 0) 7 11% 13%)", "foreground: lab(from rgb(200 170 0) 7 11% 13%)");
        color("foreground: lab(from var(--base-color) 7 11% 13%)", "foreground: lab(from var(--base-color) 7 11% 13%)");

        color("foreground: lab(from red calc(l + 7) calc(a - 11%) calc(b * 13%))", "foreground: lab(from red calc(l + 7) calc(a - 11%) calc(b * 13%))");
        color("foreground: lab(from red calc(l + 7) calc(a - 11%) calc(b * 13%) / calc(alpha / 2))", "foreground: lab(from red calc(l + 7) calc(a - 11%) calc(b * 13%) / calc(alpha / 2))");
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void labVariousErrors() throws Exception {
        // like browsers we ignore many errors during parsing

        color(1, "Error in expression. (Invalid token \",\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: lab(10, 20 30)");

        color("foreground: lab(10 20 30 40)", "foreground: lab(10 20 30 40)");

        color(1, "Error in expression. (Invalid token \"-none\". Was expecting one of: <S>, <NUMBER>, \"none\", \"from\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: lab(-none 20 30)");
        color(1, "Error in expression. (Invalid token \"-none\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: lab(10 -none 30)");
        color(1, "Error in expression. (Invalid token \"-none\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: lab(10 20 -none)");
        color(1, "Error in expression. (Invalid token \"-none\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: lab(10 20 30 / -none)");

        color(1, "Error in expression. (Invalid token \")\". Was expecting one of: <S>, <NUMBER>, \"none\", \"from\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: lab()");
        color(1, "Error in expression. (Invalid token \")\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: lab(10)");
        color(1, "Error in expression. (Invalid token \")\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: lab(10 20)");

        color(1, "Error in expression. (Invalid token \")\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: lab(10 20 30/)");

        color(1, "Error in expression. (Invalid token \"20\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: lab(10 20px 30)");

        color(1, "Error in expression. (Invalid token \"10\". Was expecting one of: <S>, <NUMBER>, \"none\", \"from\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: lab('10' 20 30)");
    }
}
