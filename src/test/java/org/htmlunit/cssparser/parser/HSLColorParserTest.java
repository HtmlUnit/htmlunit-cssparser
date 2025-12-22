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
public class HSLColorParserTest extends AbstractCSSParserTest {

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void hslComma() throws Exception {
        color("foreground: hsl(270, 60%, 70%)", "foreground: hsl(270,60%,70%)");
        color("foreground: hsl(-270, 60%, 70%)", "foreground: hsl(-270, 60%, 70%)");
        color("foreground: hsl(270deg, 60%, 70%)", "foreground: hsl(270deg, 60%, 70%)");
        color("foreground: hsl(270rad, 60%, 70%)", "foreground: hsl(270rad, 60%, 70%)");
        color("foreground: hsl(270grad, 60%, 70%)", "foreground: hsl(270grad, 60%, 70%)");
        color("foreground: hsl(2.1turn, 60%, 70%)", "foreground: hsl(2.1turn, 60%, 70%)");

        color("foreground: hsl(255, 0%, 15.37%)", "foreground: hsl(2.55e2, 0e0%, 1537e-2%)");
        color("foreground: hsl(255deg, 0%, 15.37%)", "foreground: hsl(2.55e2deg, 0e0%, 1537e-2%)");
        color("foreground: hsl(255rad, 0%, 15.37%)", "foreground: hsl(2.55e2rad, 0e0%, 1537e-2%)");
        color("foreground: hsl(255grad, 0%, 15.37%)", "foreground: hsl(2.55e2grad, 0e0%, 1537e-2%)");
        color("foreground: hsl(255turn, 0%, 15.37%)", "foreground: hsl(2.55e2turn, 0e0%, 1537e-2%)");

        // alpha
        color("foreground: hsl(270, 60%, 70%, 0.1)", "foreground: hsl(270,60%,70%,0.1)");
        color("foreground: hsl(-270, 60%, 70%, 0.1)", "foreground: hsl(-270, 60%, 70%, 0.1)");
        color("foreground: hsl(-270, 60%, 70%, 0.1)", "foreground: hsl(-270, 60%, 70%, .1)");
        color("foreground: hsl(-270, 60%, 70%, 10%)", "foreground: hsl(-270, 60%, 70%, 10%)");

        // var
        color("foreground: hsl(var(--v-h), var(--v-s), var(--v-l), var(--v-alpha))", "foreground: hsl(var(--v-h), var(--v-s), var(--v-l), var(--v-alpha))");

        // calc
        color("foreground: hsl(calc(270), calc(60%), calc(70%), calc(0.1))", "foreground: hsl(calc(270), calc(60%), calc(70%), calc(0.1))");
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void hslBlank() throws Exception {
        color("foreground: hsl(270 60% 70%)", "foreground: hsl(270 60% 70%)");
        color("foreground: hsl(-270 60% 70%)", "foreground: hsl(-270  60%  70%)");
        color("foreground: hsl(270deg 60% 70%)", "foreground: hsl(270deg  60%  70%)");
        color("foreground: hsl(270rad 60% 70%)", "foreground: hsl(270rad 60% 70%)");
        color("foreground: hsl(270grad 60% 70%)", "foreground: hsl(270grad 60% 70%)");
        color("foreground: hsl(2.1turn 60% 70%)", "foreground: hsl(2.1turn 60% 70%)");

        color("foreground: hsl(none 0% 60%)", "foreground: hsl(none 0% 60%)");
        color("foreground: hsl(100grad none 60%)", "foreground: hsl(100grad none 60%)");
        color("foreground: hsl(100grad 0% none)", "foreground: hsl(100grad  0%  none)");

        color("foreground: hsl(255 0% 15.37%)", "foreground: hsl(2.55e2 0e0% 1537e-2%)");
        color("foreground: hsl(255deg 0% 15.37%)", "foreground: hsl(2.55e2deg 0e0% 1537e-2%)");
        color("foreground: hsl(255rad 0% 15.37%)", "foreground: hsl(2.55e2rad 0e0% 1537e-2%)");
        color("foreground: hsl(255grad 0% 15.37%)", "foreground: hsl(2.55e2grad 0e0% 1537e-2%)");
        color("foreground: hsl(255turn 0% 15.37%)", "foreground: hsl(2.55e2turn 0e0% 1537e-2%)");

        // alpha
        color("foreground: hsl(270 60% 70% / 0.1)", "foreground: hsl(270 60% 70%/0.1)");
        color("foreground: hsl(-270 60% 70% / 0.1)", "foreground: hsl(-270  60%  70%  / 0.1)");
        color("foreground: hsl(-270 60% 70% / 0.1)", "foreground: hsl(-270 60% 70% / .1)");
        color("foreground: hsl(-270 60% 70% / 10%)", "foreground: hsl(-270 60% 70% / 10%)");

        color("foreground: hsl(-270 60% 70% / none)", "foreground: hsl(-270 60% 70% / none)");

        // var
        color("foreground: hsl(var(--v-h) var(--v-s) var(--v-l) / var(--v-alpha))", "foreground: hsl(var(--v-h) var(--v-s) var(--v-l) / var(--v-alpha))");

        // calc
        color("foreground: hsl(calc(270) calc(60%) calc(70%) / calc(0.1))", "foreground: hsl(calc(270) calc(60%) calc(70%) / calc(0.1))");
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void hslMixed() throws Exception {
        color("foreground: hsl(42, 128%, 255%)", "foreground: hsl(42, 128%, 255%)");
        color("foreground: hsl(42deg, 128%, 255%)", "foreground: hsl(42deg, 128%, 255%)");

        color("foreground: hsl(42 128% 255%)", "foreground: hsl(42 128% 255%)");
        color("foreground: hsl(42rad 128% 255%)", "foreground: hsl(42rad 128% 255%)");
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void hslRelative() throws Exception {
        color("foreground: hsl(from red h s l)", "foreground: hsl(from red h s l)");
        color("foreground: hsl(from red h s l / alpha)", "foreground: hsl(from red h s l / alpha)");

        color("foreground: hsl(from red 7 11% 13%)", "foreground: hsl(from red 7 11% 13%)");
        color("foreground: hsl(from red 7 11% 13% / 0.7)", "foreground: hsl(from red 7 11% 13% / 0.7)");

        color("foreground: hsl(from rgb(200 170 0) 7 11% 13%)", "foreground: hsl(from rgb(200 170 0) 7 11% 13%)");
        color("foreground: hsl(from var(--base-color) 7 11% 13%)", "foreground: hsl(from var(--base-color) 7 11% 13%)");

        color("foreground: hsl(from red calc(h + 7) calc(s - 11%) calc(l * 13%))", "foreground: hsl(from red calc(h + 7) calc(s - 11%) calc(l * 13%))");
        color("foreground: hsl(from red calc(h + 7) calc(s - 11%) calc(l * 13%) / calc(alpha / 2))", "foreground: hsl(from red calc(h + 7) calc(s - 11%) calc(l * 13%) / calc(alpha / 2))");
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void hslaComma() throws Exception {
        color("foreground: hsla(270, 60%, 70%)", "foreground: hsla(270,60%,70%)");
        color("foreground: hsla(-270, 60%, 70%)", "foreground: hsla(-270, 60%, 70%)");
        color("foreground: hsla(270deg, 60%, 70%)", "foreground: hsla(270deg, 60%, 70%)");
        color("foreground: hsla(270rad, 60%, 70%)", "foreground: hsla(270rad, 60%, 70%)");
        color("foreground: hsla(270grad, 60%, 70%)", "foreground: hsla(270grad, 60%, 70%)");
        color("foreground: hsla(2.1turn, 60%, 70%)", "foreground: hsla(2.1turn, 60%, 70%)");

        color("foreground: hsla(255, 0%, 15.37%)", "foreground: hsla(2.55e2, 0e0%, 1537e-2%)");
        color("foreground: hsla(255deg, 0%, 15.37%)", "foreground: hsla(2.55e2deg, 0e0%, 1537e-2%)");
        color("foreground: hsla(255rad, 0%, 15.37%)", "foreground: hsla(2.55e2rad, 0e0%, 1537e-2%)");
        color("foreground: hsla(255grad, 0%, 15.37%)", "foreground: hsla(2.55e2grad, 0e0%, 1537e-2%)");
        color("foreground: hsla(255turn, 0%, 15.37%)", "foreground: hsla(2.55e2turn, 0e0%, 1537e-2%)");

        // alpha
        color("foreground: hsla(270, 60%, 70%, 0.1)", "foreground: hsla(270,60%,70%,0.1)");
        color("foreground: hsla(-270, 60%, 70%, 0.1)", "foreground: hsla(-270, 60%, 70%, 0.1)");
        color("foreground: hsla(-270, 60%, 70%, 0.1)", "foreground: hsla(-270, 60%, 70%, .1)");
        color("foreground: hsla(-270, 60%, 70%, 10%)", "foreground: hsla(-270, 60%, 70%, 10%)");

        // var
        color("foreground: hsla(var(--v-h), var(--v-s), var(--v-l), var(--v-alpha))", "foreground: hsla(var(--v-h), var(--v-s), var(--v-l), var(--v-alpha))");

        // calc
        color("foreground: hsla(calc(270), calc(60%), calc(70%), calc(0.1))", "foreground: hsla(calc(270), calc(60%), calc(70%), calc(0.1))");
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void hslaBlank() throws Exception {
        color("foreground: hsla(270 60% 70%)", "foreground: hsla(270 60% 70%)");
        color("foreground: hsla(-270 60% 70%)", "foreground: hsla(-270  60%  70%)");
        color("foreground: hsla(270deg 60% 70%)", "foreground: hsla(270deg  60%  70%)");
        color("foreground: hsla(270rad 60% 70%)", "foreground: hsla(270rad 60% 70%)");
        color("foreground: hsla(270grad 60% 70%)", "foreground: hsla(270grad 60% 70%)");
        color("foreground: hsla(2.1turn 60% 70%)", "foreground: hsla(2.1turn 60% 70%)");

        color("foreground: hsla(none 0% 60%)", "foreground: hsla(none 0% 60%)");
        color("foreground: hsla(100grad none 60%)", "foreground: hsla(100grad none 60%)");
        color("foreground: hsla(100grad 0% none)", "foreground: hsla(100grad  0%  none)");

        color("foreground: hsla(255 0% 15.37%)", "foreground: hsla(2.55e2 0e0% 1537e-2%)");
        color("foreground: hsla(255deg 0% 15.37%)", "foreground: hsla(2.55e2deg 0e0% 1537e-2%)");
        color("foreground: hsla(255rad 0% 15.37%)", "foreground: hsla(2.55e2rad 0e0% 1537e-2%)");
        color("foreground: hsla(255grad 0% 15.37%)", "foreground: hsla(2.55e2grad 0e0% 1537e-2%)");
        color("foreground: hsla(255turn 0% 15.37%)", "foreground: hsla(2.55e2turn 0e0% 1537e-2%)");

        // alpha
        color("foreground: hsla(270 60% 70% / 0.1)", "foreground: hsla(270 60% 70%/0.1)");
        color("foreground: hsla(-270 60% 70% / 0.1)", "foreground: hsla(-270  60%  70%  / 0.1)");
        color("foreground: hsla(-270 60% 70% / 0.1)", "foreground: hsla(-270 60% 70% / .1)");
        color("foreground: hsla(-270 60% 70% / 10%)", "foreground: hsla(-270 60% 70% / 10%)");

        color("foreground: hsla(-270 60% 70% / none)", "foreground: hsla(-270 60% 70% / none)");

        // var
        color("foreground: hsla(var(--v-h) var(--v-s) var(--v-l) / var(--v-alpha))", "foreground: hsla(var(--v-h) var(--v-s) var(--v-l) / var(--v-alpha))");

        // calc
        color("foreground: hsla(calc(270) calc(60%) calc(70%) / calc(0.1))", "foreground: hsla(calc(270) calc(60%) calc(70%) / calc(0.1))");
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void hslaMixed() throws Exception {
        color("foreground: hsla(42, 128%, 255%)", "foreground: hsla(42, 128%, 255%)");
        color("foreground: hsla(42deg, 128%, 255%)", "foreground: hsla(42deg, 128%, 255%)");

        color("foreground: hsla(42 128% 255%)", "foreground: hsla(42 128% 255%)");
        color("foreground: hsla(42rad 128% 255%)", "foreground: hsla(42rad 128% 255%)");
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void hslVariousErrors() throws Exception {
        // like browsers we ignore many errors during parsing

        color("foreground: hsl(10, 20% 30%)", "foreground: hsl(10, 20% 30%)");
        color("foreground: hsl(10 20%, 30%)", "foreground: hsl(10 20%, 30%)");

        color("foreground: hsl(10 20% 30% 40)", "foreground: hsl(10 20% 30% 40)");

        color("foreground: hsl(none, 20%, 30%)", "foreground: hsl(none, 20%, 30%)");
        color("foreground: hsl(10, none, 30%)", "foreground: hsl(10, none, 30%)");
        color("foreground: hsl(10, 20%, none)", "foreground: hsl(10, 20%, none)");
        color("foreground: hsl(10, 20%, 30%, none)", "foreground: hsl(10, 20%, 30%, none)");

        color(1, "Error in expression. (Invalid token \"-none\". Was expecting one of: <S>, <NUMBER>, \"none\", \"from\", \"-\", \"+\", "
                + "<ANGLE_DEG>, <ANGLE_RAD>, <ANGLE_GRAD>, <ANGLE_TURN>, <FUNCTION_CALC>, <FUNCTION_VAR>.)"
                + " (in declaration) (property: foreground)",
                "foreground: hsl(-none 20% 30%)");
        color(1, "Error in expression. (Invalid token \"-none\". Was expecting one of: <S>, \"none\", \"-\", \"+\", \",\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)"
                + " (in declaration) (property: foreground)",
                "foreground: hsl(10 -none 30%)");
        color(1, "Error in expression. (Invalid token \"-none\". Was expecting one of: <S>, \"none\", \"-\", \"+\", \",\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)"
                + " (in declaration) (property: foreground)",
                "foreground: hsl(10 20% -none)");
        color(1, "Error in expression. (Invalid token \"-none\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)"
                + " (in declaration) (property: foreground)",
                "foreground: hsl(10 20% 30% / -none)");

        color(1, "Error in expression. (Invalid token \")\". Was expecting one of: <S>, <NUMBER>, \"none\", \"from\", \"-\", \"+\", <ANGLE_DEG>, <ANGLE_RAD>, <ANGLE_GRAD>, <ANGLE_TURN>, <FUNCTION_CALC>, <FUNCTION_VAR>.)"
                + " (in declaration) (property: foreground)",
                "foreground: hsl()");
        color(1, "Error in expression. (Invalid token \")\". Was expecting one of: <S>, \"none\", \"-\", \"+\", \",\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)"
                + " (in declaration) (property: foreground)",
                "foreground: hsl(10)");
        color(1, "Error in expression. (Invalid token \")\". Was expecting one of: <S>, \"none\", \"-\", \"+\", \",\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)"
                + " (in declaration) (property: foreground)",
                "foreground: hsl(10 20%)");

        color(1, "Error in expression. (Invalid token \")\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)"
                + " (in declaration) (property: foreground)",
                "foreground: hsl(10, 20%, 30%,)");
        color(1, "Error in expression. (Invalid token \")\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)"
                + " (in declaration) (property: foreground)",
                "foreground: hsl(10, 20%, 30%/)");

        color(1, "Error in expression. (Invalid token \"20\". Was expecting one of: <S>, \"none\", \"-\", \"+\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)"
                + " (in declaration) (property: foreground)",
                "foreground: hsl(10, 20px, 30)");
        color(1, "Error in expression. (Invalid token \"20\". Was expecting one of: <S>, \"none\", \"-\", \"+\", \",\", <PERCENTAGE>, <FUNCTION_CALC>, <FUNCTION_VAR>.)"
                + " (in declaration) (property: foreground)",
                "foreground: hsl(10 20px 30)");

        color(1, "Error in expression. (Invalid token \"10\". Was expecting one of: <S>, <NUMBER>, \"none\", \"from\", \"-\", \"+\", <ANGLE_DEG>, <ANGLE_RAD>, <ANGLE_GRAD>, <ANGLE_TURN>, <FUNCTION_CALC>, <FUNCTION_VAR>.)"
                + " (in declaration) (property: foreground)",
                "foreground: hsl('10', 20, 30,)");
    }
}
