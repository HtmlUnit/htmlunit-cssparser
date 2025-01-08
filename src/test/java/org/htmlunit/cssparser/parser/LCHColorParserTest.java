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
public class LCHColorParserTest extends AbstractCSSParserTest {

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void lchBlank() throws Exception {
        color("foreground: lch(255 0 153deg)", "foreground: lch(255 0 153deg)");
        color("foreground: lch(255 0 153deg)", "foreground: lch(255  0  153.0deg)");
        color("foreground: lch(100% 0% 60deg)", "foreground: lch(100% 0% 60deg)");
        color("foreground: lch(100% 0% 60deg)", "foreground: lch(100%  0%  60deg)");

        color("foreground: lch(none 0% 60deg)", "foreground: lch(none 0% 60deg)");
        color("foreground: lch(100% none 60deg)", "foreground: lch(100% none 60deg)");
        color("foreground: lch(100% 0% none)", "foreground: lch(100%  0%  none)");

        color("foreground: lch(255 0 153deg)", "foreground: lch(2.55e2 0e0 1.53e2deg)");

        // alpha
        color("foreground: lch(10 20 30deg / 0.1)", "foreground: lch(10 20 30deg/0.1)");
        color("foreground: lch(10 20 30deg / 0.1)", "foreground: lch( 10  20 30deg / 0.1 )");
        color("foreground: lch(10 20 30deg / 0.7)", "foreground: lch( 10  20 30deg / .7 )");
        color("foreground: lch(10 20 30deg / 10%)", "foreground: lch(10 20 30deg / 10%)");

        color("foreground: lch(10% 20% 30deg / 7%)", "foreground: lch(10% 20% 30deg / 7%)");
        color("foreground: lch(10% 20% 30deg / 0.13%)", "foreground: lch(10% 20% 30deg / 1.3e-1%)");
        color("foreground: lch(10% 20% 30deg / 0.5)", "foreground: lch(10% 20% 30deg / 0.5)");

        color("foreground: lch(10% 20% 30deg / none)", "foreground: lch(10% 20% 30deg / none)");
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void lchMixed() throws Exception {
        color("foreground: lch(42 128 255deg)", "foreground: lch(42 128 255deg)");
        color("foreground: lch(42% 128 255deg)", "foreground: lch(42% 128 255deg)");
        color("foreground: lch(42 128% 255deg)", "foreground: lch(42 128% 255deg)");
        color("foreground: lch(42 128 255rad)", "foreground: lch(42 128 255rad)");
        color("foreground: lch(42% 128% 255deg)", "foreground: lch(42% 128% 255deg)");
        color("foreground: lch(42 128% 255rad)", "foreground: lch(42 128% 255rad)");
        color("foreground: lch(42% 128% 255rad)", "foreground: lch(42% 128% 255rad)");
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void lchVariousErrors() throws Exception {
//        color(1, "Error in expression. (Invalid token \",\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>.)",
//                "foreground: lch(10, 20 30deg)");
//
//        color(1, "DOM exception: ''lch' alpha value must be separated by '/'.'", "foreground: lch(10 20 30deg 40)");
//
//        color(1, "Error in expression. (Invalid token \"-none\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>.)",
//                "foreground: lch(-none 20 30deg)");
//        color(1, "Error in expression. (Invalid token \"-none\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>.)",
//                "foreground: lch(10 -none 30deg)");
//        color(1, "Error in expression. (Invalid token \"-none\". Was expecting one of: <S>, \"none\", \"-\", \"+\", <ANGLE_DEG>, <ANGLE_RAD>, <ANGLE_GRAD>, <ANGLE_TURN>.)",
//                "foreground: lch(10 20 -none)");
//        color(1, "Error in expression. (Invalid token \"-none\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>.)",
//                "foreground: lch(10 20 30deg / -none)");
//
//        color(1, "Error in expression. (Invalid token \")\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>.)",
//                "foreground: lch()");
//        color(1, "Error in expression. (Invalid token \")\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>.)",
//                "foreground: lch(10)");
//        color(1, "Error in expression. (Invalid token \")\". Was expecting one of: <S>, \"none\", \"-\", \"+\", <ANGLE_DEG>, <ANGLE_RAD>, <ANGLE_GRAD>, <ANGLE_TURN>.)",
//                "foreground: lch(10 20)");
//
//        color(1, "Error in expression. (Invalid token \")\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>.)",
//                "foreground: lch(10 20 30deg/)");
//
//        color(1, "Error in expression. (Invalid token \"20\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>.)",
//                "foreground: lch(10 20px 30deg)");
//
//        color(1, "Error in expression. (Invalid token \"10\". Was expecting one of: <S>, <NUMBER>, \"none\", \"-\", \"+\", <PERCENTAGE>.)",
//                "foreground: lch('10' 20 30deg)");
    }
}
