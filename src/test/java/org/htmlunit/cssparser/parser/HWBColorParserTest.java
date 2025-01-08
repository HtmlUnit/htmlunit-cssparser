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
public class HWBColorParserTest extends AbstractCSSParserTest {

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void hwbBlank() throws Exception {
        color("foreground: hwb(270 60% 70%)", "foreground: hwb(270 60% 70%)");
        color("foreground: hwb(-270 60% 70%)", "foreground: hwb(-270  60%  70%)");
        color("foreground: hwb(270deg 60% 70%)", "foreground: hwb(270deg  60%  70%)");
        color("foreground: hwb(270rad 60% 70%)", "foreground: hwb(270rad 60% 70%)");
        color("foreground: hwb(270grad 60% 70%)", "foreground: hwb(270grad 60% 70%)");
        color("foreground: hwb(2.1turn 60% 70%)", "foreground: hwb(2.1turn 60% 70%)");

        color("foreground: hwb(none 0% 60%)", "foreground: hwb(none 0% 60%)");
        color("foreground: hwb(100turn none 60%)", "foreground: hwb(100turn none 60%)");
        color("foreground: hwb(100turn 0% none)", "foreground: hwb(100turn  0%  none)");

        color("foreground: hwb(255 0% 15.37%)", "foreground: hwb(2.55e2 0e0% 1537e-2%)");
        color("foreground: hwb(255deg 0% 15.37%)", "foreground: hwb(2.55e2deg 0e0% 1537e-2%)");
        color("foreground: hwb(255rad 0% 15.37%)", "foreground: hwb(2.55e2rad 0e0% 1537e-2%)");
        color("foreground: hwb(255grad 0% 15.37%)", "foreground: hwb(2.55e2grad 0e0% 1537e-2%)");
        color("foreground: hwb(255turn 0% 15.37%)", "foreground: hwb(2.55e2turn 0e0% 1537e-2%)");

        // alpha
        color("foreground: hwb(270 60% 70% / 0.1)", "foreground: hwb(270 60% 70%/0.1)");
        color("foreground: hwb(-270 60% 70% / 0.1)", "foreground: hwb(-270  60%  70%  / 0.1)");
        color("foreground: hwb(-270 60% 70% / 0.1)", "foreground: hwb(-270 60% 70% / .1)");
        color("foreground: hwb(-270 60% 70% / 10%)", "foreground: hwb(-270 60% 70% / 10%)");

        color("foreground: hwb(-270 60% 70% / none)", "foreground: hwb(-270 60% 70% / none)");
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void hwbRelative() throws Exception {
        color("foreground: hwb(from red h w b)", "foreground: hwb(from red h w b)");
        color("foreground: hwb(from red 7 11% 13%)", "foreground: hwb(from red 7 11% 13%)");
        color("foreground: hwb(from rgb(200 170 0) 7 11% 13%)", "foreground: hwb(from rgb(200 170 0) 7 11% 13%)");
        color("foreground: hwb(from var(--base-color) 7 11% 13%)", "foreground: hwb(from var(--base-color) 7 11% 13%)");

        color("foreground: hwb(from red calc(h + 7) calc(w - 11%) calc(b * 13%))", "foreground: hwb(from red calc(h + 7) calc(w - 11%) calc(b * 13%))");
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void hwbVariousErrors() throws Exception {
        // like browsers we ignore many errors during parsing

        color(1, "Error in expression. (Invalid token \",\". Was expecting one of: <S>, \"none\", \"-\", \"+\", <PERCENTAGE>.)",
                "foreground: hwb(10, 20% 30%)");
        color(1, "Error in expression. (Invalid token \",\". Was expecting one of: <S>, \"none\", \"-\", \"+\", <PERCENTAGE>.)", "foreground: hwb(10 20%, 30%)");

        color("foreground: hwb(10 20% 30% 40)", "foreground: hwb(10 20% 30% 40)");

        color(1, "Error in expression. (Invalid token \"-none\". Was expecting one of: <S>, <NUMBER>, \"none\", \"from\", \"-\", \"+\", <ANGLE_DEG>, <ANGLE_RAD>, <ANGLE_GRAD>, <ANGLE_TURN>.)",
                "foreground: hwb(-none 20% 30%)");
        color(1, "Error in expression. (Invalid token \"-none\". Was expecting one of: <S>, \"none\", \"-\", \"+\", <PERCENTAGE>.)",
                "foreground: hwb(10 -none 30%)");
        color(1, "Error in expression. (Invalid token \"-none\". Was expecting one of: <S>, \"none\", \"-\", \"+\", <PERCENTAGE>.)",
                "foreground: hwb(10 20% -none)");
        color(1, "Error in expression. (Invalid token \"-none\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>.)",
                "foreground: hwb(10 20% 30% / -none)");

        color(1, "Error in expression. (Invalid token \")\". Was expecting one of: <S>, <NUMBER>, \"none\", \"from\", \"-\", \"+\", <ANGLE_DEG>, <ANGLE_RAD>, <ANGLE_GRAD>, <ANGLE_TURN>.)",
                "foreground: hwb()");
        color(1, "Error in expression. (Invalid token \")\". Was expecting one of: <S>, \"none\", \"-\", \"+\", <PERCENTAGE>.)",
                "foreground: hwb(10)");
        color(1, "Error in expression. (Invalid token \")\". Was expecting one of: <S>, \"none\", \"-\", \"+\", <PERCENTAGE>.)",
                "foreground: hwb(10 20%)");

        color(1, "Error in expression. (Invalid token \")\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>.)",
                "foreground: hwb(10 20% 30%/)");

        color(1, "Error in expression. (Invalid token \"20\". Was expecting one of: <S>, \"none\", \"-\", \"+\", <PERCENTAGE>.)",
                "foreground: hwb(10 20px 30)");

        color(1, "Error in expression. (Invalid token \"10\". Was expecting one of: <S>, <NUMBER>, \"none\", \"from\", \"-\", \"+\", <ANGLE_DEG>, <ANGLE_RAD>, <ANGLE_GRAD>, <ANGLE_TURN>.)",
                "foreground: hwb('10' 20 30)");
    }
}
