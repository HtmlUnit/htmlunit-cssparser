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
public class RGBColorParserTest extends AbstractCSSParserTest {

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void rgbComma() throws Exception {
        color("foreground: rgb(255, 0, 153)", "foreground: rgb(255,0,153)");
        color("foreground: rgb(255, 0, 153)", "foreground: rgb(255, 0, 153.0)");
        color("foreground: rgb(100%, 0%, 60%)", "foreground: rgb(100%,0%,60%)");
        color("foreground: rgb(100%, 0%, 60%)", "foreground: rgb(100%, 0%, 60%)");

        color("foreground: rgb(255, 0, 153)", "foreground: rgb(2.55e2, 0e0, 1.53e2)");

        color("foreground: rgb(-255, 0, -153)", "foreground: rgb(-255, -0, -153.0)");
        color("foreground: rgb(-100%, 0%, -60%)", "foreground: rgb(-100%,-0%,-60%)");

        // alpha
        color("foreground: rgb(10, 20, 30, 0.1)", "foreground: rgb(10,20,30,0.1)");
        color("foreground: rgb(10, 20, 30, 0.1)", "foreground: rgb( 10, 20, 30, 0.1 )");
        color("foreground: rgb(10, 20, 30, 0.7)", "foreground: rgb( 10, 20, 30, .7 )");
        color("foreground: rgb(10, 20, 30, 10%)", "foreground: rgb(10, 20, 30, 10%)");

        color("foreground: rgb(10%, 20%, 30%, 7%)", "foreground: rgb(10%, 20%, 30%, 7%)");
        color("foreground: rgb(10%, 20%, 30%, 0.13%)", "foreground: rgb(10%, 20%, 30%, 1.3e-1%)");
        color("foreground: rgb(10%, 20%, 30%, 0.5)", "foreground: rgb(10%, 20%, 30%, 0.5)");

        color("foreground: rgb(-255, -10, -153, -0.5)", "foreground: rgb(-255, -10, -153.0, -0.5)");
        color("foreground: rgb(-100%, -10%, -60%, -7%)", "foreground: rgb(-100%,-10%,-60%,-7%)");

        // var
        color("foreground: rgb(var(--v-r), var(--v-g), var(--v-b), var(--v-alpha))", "foreground: rgb(var(--v-r), var(--v-g), var(--v-b), var(--v-alpha))");

        // calc
        color("foreground: rgb(calc(10), calc(20), calc(30), calc(0.1))", "foreground: rgb(calc(10), calc(20), calc(30), calc(0.1))");
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void rgbBlank() throws Exception {
        color("foreground: rgb(255 0 153)", "foreground: rgb(255 0 153)");
        color("foreground: rgb(255 0 153)", "foreground: rgb(255  0  153.0)");
        color("foreground: rgb(100% 0% 60%)", "foreground: rgb(100% 0% 60%)");
        color("foreground: rgb(100% 0% 60%)", "foreground: rgb(100%  0%  60%)");

        color("foreground: rgb(none 0% 60%)", "foreground: rgb(none 0% 60%)");
        color("foreground: rgb(100% none 60%)", "foreground: rgb(100% none 60%)");
        color("foreground: rgb(100% 0% none)", "foreground: rgb(100%  0%  none)");

        color("foreground: rgb(255 0 153)", "foreground: rgb(2.55e2 0e0 1.53e2)");

        color("foreground: rgb(-255 0 -153)", "foreground: rgb(-255 -0 -153.0)");
        color("foreground: rgb(-100% 0% -60%)", "foreground: rgb(-100% -0% -60%)");

        // alpha
        color("foreground: rgb(10 20 30 / 0.1)", "foreground: rgb(10 20 30/0.1)");
        color("foreground: rgb(10 20 30 / 0.1)", "foreground: rgb( 10  20 30 / 0.1 )");
        color("foreground: rgb(10 20 30 / 0.7)", "foreground: rgb( 10  20 30 / .7 )");
        color("foreground: rgb(10 20 30 / 10%)", "foreground: rgb(10 20 30 / 10%)");

        color("foreground: rgb(10% 20% 30% / 7%)", "foreground: rgb(10% 20% 30% / 7%)");
        color("foreground: rgb(10% 20% 30% / 0.13%)", "foreground: rgb(10% 20% 30% / 1.3e-1%)");
        color("foreground: rgb(10% 20% 30% / 0.5)", "foreground: rgb(10% 20% 30% / 0.5)");

        color("foreground: rgb(10% 20% 30% / none)", "foreground: rgb(10% 20% 30% / none)");

        color("foreground: rgb(-255 -10 -153 / -0.5)", "foreground: rgb(-255 -10 -153.0 / -0.5)");
        color("foreground: rgb(-100% -10% -60% / -7%)", "foreground: rgb(-100% -10% -60%/-7%)");

        // var
        color("foreground: rgb(var(--v-r) var(--v-g) var(--v-b) / var(--v-alpha))", "foreground: rgb(var(--v-r) var(--v-g) var(--v-b) / var(--v-alpha))");

        // calc
        color("foreground: rgb(calc(10) calc(20) calc(30) / calc(0.1))", "foreground: rgb(calc(10) calc(20) calc(30) / calc(0.1))");
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void rgbMixed() throws Exception {
        color("foreground: rgb(42, 128, 255)", "foreground: rgb(42, 128, 255)");
        color("foreground: rgb(42%, 128, 255)", "foreground: rgb(42%, 128, 255)");
        color("foreground: rgb(42, 128%, 255)", "foreground: rgb(42, 128%, 255)");
        color("foreground: rgb(42, 128, 255%)", "foreground: rgb(42, 128, 255%)");
        color("foreground: rgb(42, 128%, 255%)", "foreground: rgb(42, 128%, 255%)");
        color("foreground: rgb(42%, 128%, 255)", "foreground: rgb(42%, 128%, 255)");
        color("foreground: rgb(42%, 128%, 255%)", "foreground: rgb(42%, 128%, 255%)");

        color("foreground: rgb(42 128 255)", "foreground: rgb(42 128 255)");
        color("foreground: rgb(42% 128 255)", "foreground: rgb(42% 128 255)");
        color("foreground: rgb(42 128% 255)", "foreground: rgb(42 128% 255)");
        color("foreground: rgb(42 128 255%)", "foreground: rgb(42 128 255%)");
        color("foreground: rgb(42% 128% 255)", "foreground: rgb(42% 128% 255)");
        color("foreground: rgb(42 128% 255%)", "foreground: rgb(42 128% 255%)");
        color("foreground: rgb(42% 128% 255%)", "foreground: rgb(42% 128% 255%)");
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void rgbRelative() throws Exception {
        color("foreground: rgb(from red r g b)", "foreground: rgb(from red r g b)");
        color("foreground: rgb(from red r g b / alpha)", "foreground: rgb(from red r g b / alpha)");

        color("foreground: rgb(from red 7 11% 13%)", "foreground: rgb(from red 7 11% 13%)");
        color("foreground: rgb(from red 7 11% 13% / 0.7)", "foreground: rgb(from red 7 11% 13% / 0.7)");

        color("foreground: rgb(from rgb(200 170 0) 7 11% 13%)", "foreground: rgb(from rgb(200 170 0) 7 11% 13%)");
        color("foreground: rgb(from var(--base-color) 7 11% 13%)", "foreground: rgb(from var(--base-color) 7 11% 13%)");

        color("foreground: rgb(from red calc(r + 7) calc(g - 11%) calc(b * 13%))", "foreground: rgb(from red calc(r + 7) calc(g - 11%) calc(b * 13%))");
        color("foreground: rgb(from red calc(r + 7) calc(g - 11%) calc(b * 13%) / calc(alpha / 2))", "foreground: rgb(from red calc(r + 7) calc(g - 11%) calc(b * 13%) / calc(alpha / 2))");
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void rgbaComma() throws Exception {
        color("foreground: rgba(255, 0, 153)", "foreground: rgba(255,0,153)");
        color("foreground: rgba(255, 0, 153)", "foreground: rgba(255, 0, 153.0)");
        color("foreground: rgba(100%, 0%, 60%)", "foreground: rgba(100%,0%,60%)");
        color("foreground: rgba(100%, 0%, 60%)", "foreground: rgba(100%, 0%, 60%)");

        color("foreground: rgba(255, 0, 153)", "foreground: rgba(2.55e2, 0e0, 1.53e2)");

        color("foreground: rgba(-255, 0, -153)", "foreground: rgba(-255, -0, -153.0)");
        color("foreground: rgba(-100%, 0%, -60%)", "foreground: rgba(-100%,-0%,-60%)");

        // alpha
        color("foreground: rgba(10, 20, 30, 0.1)", "foreground: rgba(10,20,30,0.1)");
        color("foreground: rgba(10, 20, 30, 0.1)", "foreground: rgba( 10, 20, 30, 0.1 )");
        color("foreground: rgba(10, 20, 30, 0.7)", "foreground: rgba( 10, 20, 30, .7 )");
        color("foreground: rgba(10, 20, 30, 10%)", "foreground: rgba(10, 20, 30, 10%)");

        color("foreground: rgba(10%, 20%, 30%, 7%)", "foreground: rgba(10%, 20%, 30%, 7%)");
        color("foreground: rgba(10%, 20%, 30%, 0.13%)", "foreground: rgba(10%, 20%, 30%, 1.3e-1%)");
        color("foreground: rgba(10%, 20%, 30%, 0.5)", "foreground: rgba(10%, 20%, 30%, 0.5)");

        color("foreground: rgba(-255, -10, -153, -0.5)", "foreground: rgba(-255, -10, -153.0, -0.5)");
        color("foreground: rgba(-100%, -10%, -60%, -7%)", "foreground: rgba(-100%,-10%,-60%,-7%)");

        // var
        color("foreground: rgba(var(--v-r), var(--v-g), var(--v-b), var(--v-alpha))", "foreground: rgba(var(--v-r), var(--v-g), var(--v-b), var(--v-alpha))");

        // calc
        color("foreground: rgba(calc(10), calc(20), calc(30), calc(0.1))", "foreground: rgba(calc(10), calc(20), calc(30), calc(0.1))");
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void rgbaBlank() throws Exception {
        color("foreground: rgba(255 0 153)", "foreground: rgba(255 0 153)");
        color("foreground: rgba(255 0 153)", "foreground: rgba(255  0  153.0)");
        color("foreground: rgba(100% 0% 60%)", "foreground: rgba(100% 0% 60%)");
        color("foreground: rgba(100% 0% 60%)", "foreground: rgba(100%  0%  60%)");

        color("foreground: rgba(none 0% 60%)", "foreground: rgba(none 0% 60%)");
        color("foreground: rgba(100% none 60%)", "foreground: rgba(100% none 60%)");
        color("foreground: rgba(100% 0% none)", "foreground: rgba(100%  0%  none)");

        color("foreground: rgba(255 0 153)", "foreground: rgba(2.55e2 0e0 1.53e2)");

        color("foreground: rgba(-255 0 -153)", "foreground: rgba(-255 -0 -153.0)");
        color("foreground: rgba(-100% 0% -60%)", "foreground: rgba(-100% -0% -60%)");

        // alpha
        color("foreground: rgba(10 20 30 / 0.1)", "foreground: rgba(10 20 30/0.1)");
        color("foreground: rgba(10 20 30 / 0.1)", "foreground: rgba( 10  20 30 / 0.1 )");
        color("foreground: rgba(10 20 30 / 0.7)", "foreground: rgba( 10  20 30 / .7 )");
        color("foreground: rgba(10 20 30 / 10%)", "foreground: rgba(10 20 30 / 10%)");

        color("foreground: rgba(10% 20% 30% / 7%)", "foreground: rgba(10% 20% 30% / 7%)");
        color("foreground: rgba(10% 20% 30% / 0.13%)", "foreground: rgba(10% 20% 30% / 1.3e-1%)");
        color("foreground: rgba(10% 20% 30% / 0.5)", "foreground: rgba(10% 20% 30% / 0.5)");

        color("foreground: rgba(10% 20% 30% / none)", "foreground: rgba(10% 20% 30% / none)");

        color("foreground: rgba(-255 -10 -153 / -0.5)", "foreground: rgba(-255 -10 -153.0 / -0.5)");
        color("foreground: rgba(-100% -10% -60% / -7%)", "foreground: rgba(-100% -10% -60%/-7%)");

        // var
        color("foreground: rgba(var(--v-r) var(--v-g) var(--v-b) / var(--v-alpha))", "foreground: rgba(var(--v-r) var(--v-g) var(--v-b) / var(--v-alpha))");

        // calc
        color("foreground: rgba(calc(10) calc(20) calc(30) / calc(0.1))", "foreground: rgba(calc(10) calc(20) calc(30) / calc(0.1))");
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void rgbaMixed() throws Exception {
        color("foreground: rgba(42, 128, 255)", "foreground: rgba(42, 128, 255)");
        color("foreground: rgba(42%, 128, 255)", "foreground: rgba(42%, 128, 255)");
        color("foreground: rgba(42, 128%, 255)", "foreground: rgba(42, 128%, 255)");
        color("foreground: rgba(42, 128, 255%)", "foreground: rgba(42, 128, 255%)");
        color("foreground: rgba(42, 128%, 255%)", "foreground: rgba(42, 128%, 255%)");
        color("foreground: rgba(42%, 128%, 255)", "foreground: rgba(42%, 128%, 255)");
        color("foreground: rgba(42%, 128%, 255%)", "foreground: rgba(42%, 128%, 255%)");

        color("foreground: rgba(42 128 255)", "foreground: rgba(42 128 255)");
        color("foreground: rgba(42% 128 255)", "foreground: rgba(42% 128 255)");
        color("foreground: rgba(42 128% 255)", "foreground: rgba(42 128% 255)");
        color("foreground: rgba(42 128 255%)", "foreground: rgba(42 128 255%)");
        color("foreground: rgba(42% 128% 255)", "foreground: rgba(42% 128% 255)");
        color("foreground: rgba(42 128% 255%)", "foreground: rgba(42 128% 255%)");
        color("foreground: rgba(42% 128% 255%)", "foreground: rgba(42% 128% 255%)");
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void rgbVariousErrors() throws Exception {
        // like browsers we ignore many errors during parsing

        color("foreground: rgb(10, 20 30)", "foreground: rgb(10, 20 30)");
        color("foreground: rgb(10 20, 30)", "foreground: rgb(10 20, 30)");

        color("foreground: rgb(10 20 30 40)", "foreground: rgb(10 20 30 40)");

        color("foreground: rgb(none, 20, 30)", "foreground: rgb(none, 20, 30)");
        color("foreground: rgb(10, none, 30)", "foreground: rgb(10, none, 30)");
        color("foreground: rgb(10, 20, none)", "foreground: rgb(10, 20, none)");
        color("foreground: rgb(10, 20, 30, none)", "foreground: rgb(10, 20, 30, none)");

        color(1, "Error in expression. (Invalid token \"-none\". Was expecting one of: <S>, <NUMBER>, \"none\", \"from\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: rgb(-none 20 30)");
        color(1, "Error in expression. (Invalid token \"-none\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", \",\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: rgb(10 -none 30)");
        color(1, "Error in expression. (Invalid token \"-none\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", \",\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: rgb(10 20 -none)");
        color(1, "Error in expression. (Invalid token \"-none\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: rgb(10 20 30 / -none)");

        color(1, "Error in expression. (Invalid token \")\". Was expecting one of: <S>, <NUMBER>, \"none\", \"from\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: rgb()");
        color(1, "Error in expression. (Invalid token \")\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", \",\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: rgb(10)");
        color(1, "Error in expression. (Invalid token \")\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", \",\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: rgb(10 20)");

        color(1, "Error in expression. (Invalid token \")\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: rgb(10, 20, 30,)");
        color(1, "Error in expression. (Invalid token \")\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: rgb(10, 20, 30/)");

        color(1, "Error in expression. (Invalid token \"20\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: rgb(10, 20px, 30)");
        color(1, "Error in expression. (Invalid token \"20\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", \",\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: rgb(10 20px 30)");

        color(1, "Error in expression. (Invalid token \"10\". Was expecting one of: <S>, <NUMBER>, \"none\", \"from\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)",
                "foreground: rgb('10', 20, 30,)");
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void rgbInsideFunction() throws Exception {
        color("color: foo(rgb(204, 221, 68))", "color: foo(#cd4);");
    }

}
