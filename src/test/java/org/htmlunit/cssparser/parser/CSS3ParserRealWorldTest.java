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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.TreeSet;

import org.htmlunit.cssparser.dom.AbstractCSSRuleImpl;
import org.htmlunit.cssparser.dom.CSSMediaRuleImpl;
import org.htmlunit.cssparser.dom.CSSRuleListImpl;
import org.htmlunit.cssparser.dom.CSSStyleRuleImpl;
import org.htmlunit.cssparser.dom.CSSStyleSheetImpl;
import org.htmlunit.cssparser.dom.MediaListImpl;
import org.htmlunit.cssparser.parser.media.MediaQuery;
import org.junit.jupiter.api.Test;

/**
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class CSS3ParserRealWorldTest extends AbstractCSSParserTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void microsoft() throws Exception {
        realWorld("realworld/style.csx.css", 701, 1391,
                "screen and (-webkit-min-device-pixel-ratio: 0);"
                + "screen and (max-width: 480px);"
                + "screen and (max-width: 539px);"
                + "screen and (max-width: 667px) and (min-width: 485px);"
                + "screen and (max-width: 679px);"
                + "screen and (max-width: 680px);"
                + "screen and (max-width: 900px);"
                + "screen and (min-width: 0) and (max-width: 899px);"
                + "screen and (min-width: 1024px);"
                + "screen and (min-width: 1025px);"
                + "screen and (min-width: 1025px) and (min-height: 900px);"
                + "screen and (min-width: 33.75em);"
                + "screen and (min-width: 42.5em);"
                + "screen and (min-width: 53.5em);"
                + "screen and (min-width: 540px);"
                + "screen and (min-width: 540px) and (max-width: 679px);"
                + "screen and (min-width: 560px);"
                + "screen and (min-width: 600px);"
                + "screen and (min-width: 64.0625em);"
                + "screen and (min-width: 64.0625em) and (min-height: 768px);"
                + "screen and (min-width: 64.0625em) and (min-height: 900px);"
                + "screen and (min-width: 668px);"
                + "screen and (min-width: 668px) and (max-width: 1024px);"
                + "screen and (min-width: 680px);"
                + "screen and (min-width: 680px) and (max-width: 899px);"
                + "screen and (min-width: 70em);"
                + "screen and (min-width: 70em) and (min-height: 768px);"
                + "screen and (min-width: 70em) and (min-height: 900px);"
                + "screen and (min-width: 900px);", 145, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void oracle() throws Exception {
        realWorld("realworld/compass-homestyle.css", 735, 2160,
                "(max-height: 600px);"
                + "(max-width: 600px);"
                + "(max-width: 770px);"
                + "(min-width: 0) and (max-width: 1012px);"
                + "(min-width: 0) and (max-width: 1018px);"
                + "(min-width: 0) and (max-width: 1111px);"
                + "(min-width: 0) and (max-width: 1312px);"
                + "(min-width: 0) and (max-width: 390px);"
                + "(min-width: 0) and (max-width: 400px);"
                + "(min-width: 0) and (max-width: 410px);"
                + "(min-width: 0) and (max-width: 450px);"
                + "(min-width: 0) and (max-width: 600px);"
                + "(min-width: 0) and (max-width: 640px);"
                + "(min-width: 0) and (max-width: 680px);"
                + "(min-width: 0) and (max-width: 720px);"
                + "(min-width: 0) and (max-width: 770px);"
                + "(min-width: 0) and (max-width: 870px);"
                + "(min-width: 0) and (max-width: 974px);"
                + "(min-width: 601px);"
                + "(min-width: 771px) and (max-width: 990px);"
                + "only screen and (max-width: 974px);"
                + "only screen and (min-width: 0) and (max-width: 1024px);"
                + "only screen and (min-width: 0) and (max-width: 320px);"
                + "only screen and (min-width: 0) and (max-width: 500px);"
                + "only screen and (min-width: 0) and (max-width: 600px);"
                + "only screen and (min-width: 0) and (max-width: 770px);"
                + "only screen and (min-width: 0) and (max-width: 880px);"
                + "only screen and (min-width: 0) and (max-width: 974px);"
                + "only screen and (min-width: 1024px) and (max-width: 1360px);"
                + "only screen and (min-width: 1360px);"
                + "only screen and (min-width: 601px) and (max-width: 974px);"
                + "only screen and (min-width: 771px) and (max-width: 974px);"
                + "only screen and (min-width: 880px);"
                + "only screen and (min-width: 974px);"
                + "only screen and (min-width: 975px) and (max-width: 1040px);"
                + "\ufffdscreen,screen\t;", 27, 1);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void ibm() throws Exception {
        realWorld("realworld/www.css", 493, 983,
                "only screen and (min-device-width: 768px) and (max-device-width: 1024px);print;screen;", 14, 1);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void apple() throws Exception {
        realWorld("realworld/home.built.css", 675, 1027,
                "only screen and (-webkit-min-device-pixel-ratio: 1.5);"
                + "only screen and (max-device-width: 767px);"
                + "only screen and (max-height: 970px);"
                + "only screen and (max-width: 1023px);"
                + "only screen and (max-width: 1024px);"
                + "only screen and (max-width: 1024px) and (-webkit-min-device-pixel-ratio: 1.5);"
                + "only screen and (max-width: 1024px) and (min-resolution: 144dpi);"
                + "only screen and (max-width: 1024px) and (min-resolution: 144dppx);"
                + "only screen and (max-width: 28em) and (max-device-width: 735px);"
                + "only screen and (max-width: 320px);"
                + "only screen and (max-width: 735px) and (max-device-width: 768px);"
                + "only screen and (max-width: 735px) and (max-device-width: 768px) "
                        + "and (-webkit-min-device-pixel-ratio: 1.5);"
                + "only screen and (max-width: 735px) and (max-device-width: 768px) and (min-resolution: 144dpi);"
                + "only screen and (max-width: 735px) and (max-device-width: 768px) and (min-resolution: 144dppx);"
                + "only screen and (max-width: 735px) and (max-device-width: 768px) and (orientation: portrait);"
                + "only screen and (min-device-width: 768px);"
                + "only screen and (min-width: 1442px);"
                + "only screen and (min-width: 1442px) and (-webkit-min-device-pixel-ratio: 1.5);"
                + "only screen and (min-width: 1442px) and (min-height: 1251px);"
                + "only screen and (min-width: 1442px) and (min-resolution: 144dpi);"
                + "only screen and (min-width: 1442px) and (min-resolution: 144dppx);"
                + "print;"
                + "screen and (min-resolution: 144dpi);"
                + "screen and (min-resolution: 144dppx);", 1, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void wikipedia() throws Exception {
        realWorld("realworld/load.php.css", 90, 227, "print;screen;screen and (min-width: 982px);", 59, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void normalize_3_0_2() throws Exception {
        realWorld("realworld/normalize_3_0_2.css", 40, 64, "", 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void normalize_8_0_1() throws Exception {
        realWorld("realworld/normalize_8_0_1.css", 34, 57, "", 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void cargo() throws Exception {
        realWorld("realworld/cargo.css", 123, 330, "", 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void blueprint() throws Exception {
        realWorld("realworld/blueprint/screen.css", 245, 341, "", 0, 0);
        realWorld("realworld/blueprint/print.css", 15, 33, "", 0, 0);
        realWorld("realworld/blueprint/ie.css", 22, 30, "", 1, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void bootstrap_3_3_7() throws Exception {
        final String media = "(-webkit-transform-3d);"
                + "(max-device-width: 480px) and (orientation: landscape);"
                + "(max-width: 767px);"
                + "(min-width: 1200px);"
                + "(min-width: 768px);"
                + "(min-width: 768px) and (max-width: 991px);(min-width: 992px);"
                + "(min-width: 992px) and (max-width: 1199px);"
                + "all and (transform-3d);print;"
                + "screen and (-webkit-min-device-pixel-ratio: 0);"
                + "screen and (max-width: 767px);"
                + "screen and (min-width: 768px);";
        realWorld("realworld/bootstrap_3_3_7_min.css", 1193, 2306, media, 1, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void bootstrap_3_4_1() throws Exception {
        String media = "(-webkit-transform-3d);"
                + "(max-device-width: 480px) and (orientation: landscape);"
                + "(max-width: 767px);"
                + "(min-width: 1200px);"
                + "(min-width: 768px);"
                + "(min-width: 768px) and (max-width: 991px);(min-width: 992px);"
                + "(min-width: 992px) and (max-width: 1199px);"
                + "all and (transform-3d);print;"
                + "screen and (-webkit-min-device-pixel-ratio: 0);"
                + "screen and (max-width: 767px);"
                + "screen and (min-width: 768px);";
        realWorld("realworld/bootstrap_3_4_1.css", 1188, 2320, media, 0, 0);
        realWorld("realworld/bootstrap_3_4_1.min.css", 1188, 2319, media, 1, 0);

        media = "(max-width: 767px);";
        realWorld("realworld/bootstrap-theme_3_4_1.css", 63, 277, media, 0, 0);
        realWorld("realworld/bootstrap-theme_3_4_1.min.css", 63, 277, media, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void bootstrap_4_0_0() throws Exception {
        final String media = "(max-width: 1199.98px);"
                + "(max-width: 575.98px);"
                + "(max-width: 767.98px);"
                + "(max-width: 991.98px);"
                + "(min-width: 1200px);"
                + "(min-width: 576px);"
                + "(min-width: 768px);"
                + "(min-width: 992px);"
                + "print;";
        realWorld("realworld/bootstrap_4_0_0.css", 1033, 2470, media, 0, 0);
        realWorld("realworld/bootstrap_4_0_0_min.css", 1033, 2470, media, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void bootstrap_5_2_0() throws Exception {
        final String media = "(max-width: 1199.98px);"
                + "(max-width: 1199.98px) and (prefers-reduced-motion: reduce);"
                + "(max-width: 1399.98px);(max-width: 1399.98px) and (prefers-reduced-motion: reduce);"
                + "(max-width: 575.98px);"
                + "(max-width: 575.98px) and (prefers-reduced-motion: reduce);"
                + "(max-width: 767.98px);"
                + "(max-width: 767.98px) and (prefers-reduced-motion: reduce);"
                + "(max-width: 991.98px);"
                + "(max-width: 991.98px) and (prefers-reduced-motion: reduce);"
                + "(min-width: 1200px);"
                + "(min-width: 1400px);"
                + "(min-width: 576px);"
                + "(min-width: 768px);"
                + "(min-width: 992px);"
                + "(prefers-reduced-motion: no-preference);"
                + "(prefers-reduced-motion: reduce);"
                + "print;";
        realWorld("realworld/bootstrap_5_2_0.css", 1198, 3041, media, 32, 0);
        realWorld("realworld/bootstrap_5_2_0.min.css", 1198, 3041, media, 32, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void foundation_6_9_0() throws Exception {
        String media = "(-ms-high-contrast: active);"
                + "all and (-ms-high-contrast: none);"
                + "print;"
                + "screen and (max-width: 0em);"
                + "screen and (max-width: 39.99875em);"
                + "screen and (max-width: 63.99875em);"
                + "screen and (max-width: 74.99875em);"
                + "screen and (min-width: 0\\0 );"
                + "screen and (min-width: 40em);"
                + "screen and (min-width: 40em) and (max-width: 63.99875em);"
                + "screen and (min-width: 64em);"
                + "screen and (min-width: 64em) and (max-width: 74.99875em);"
                + "screen and (min-width: 75em);"
                + "screen and (orientation: landscape);"
                + "screen and (orientation: portrait);"
                + "screen and (prefers-color-scheme: dark);";
        realWorld("realworld/foundation_6_9_0.css", 1048, 2759, media, 0, 0);

        media = media.replace("screen and (min-width: 0\\0 );", "screen and (min-width: 0\\0);");
        realWorld("realworld/foundation_6_9_0.min.css", 1048, 2759, media, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void materialize_1_0_0() throws Exception {
        final String media = "only screen and (max-width: 360px);"
                + "only screen and (max-width: 600px);"
                + "only screen and (max-width: 992px);"
                + "only screen and (min-width: 0);"
                + "only screen and (min-width: 1200px);"
                + "only screen and (min-width: 1201px);"
                + "only screen and (min-width: 360px);"
                + "only screen and (min-width: 390px);"
                + "only screen and (min-width: 420px);"
                + "only screen and (min-width: 450px);"
                + "only screen and (min-width: 480px);"
                + "only screen and (min-width: 510px);"
                + "only screen and (min-width: 540px);"
                + "only screen and (min-width: 570px);"
                + "only screen and (min-width: 600px);"
                + "only screen and (min-width: 600px) and (max-width: 992px);"
                + "only screen and (min-width: 601px);"
                + "only screen and (min-width: 601px) and (max-width: 992px);"
                + "only screen and (min-width: 630px);"
                + "only screen and (min-width: 660px);"
                + "only screen and (min-width: 690px);"
                + "only screen and (min-width: 720px);"
                + "only screen and (min-width: 750px);"
                + "only screen and (min-width: 780px);"
                + "only screen and (min-width: 810px);"
                + "only screen and (min-width: 840px);"
                + "only screen and (min-width: 870px);"
                + "only screen and (min-width: 900px);"
                + "only screen and (min-width: 930px);"
                + "only screen and (min-width: 960px);"
                + "only screen and (min-width: 992px);"
                + "only screen and (min-width: 993px);";
        realWorld("realworld/materialize_1_0_0.css", 1368, 2986, media, 0, 0);
        realWorld("realworld/materialize_1_0_0.min.css", 1367, 2986, media, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void pure_3_0_0() throws Exception {
        final String media = "only screen and (max-width: 480px);";
        realWorld("realworld/pure_3_0_0.css", 165, 334, media, 0, 0);
        realWorld("realworld/pure_3_0_0.min.css", 165, 334, media, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void semantic_2_5_0() throws Exception {
        final String media = "all and (-ms-high-contrast: none);"
                + "only screen and (max-width: 767px);"
                + "only screen and (max-width: 991px);"
                + "only screen and (min-width: 1200px);"
                + "only screen and (min-width: 1200px) and (max-width: 1919px);"
                + "only screen and (min-width: 1920px);"
                + "only screen and (min-width: 320px) and (max-width: 767px);"
                + "only screen and (min-width: 768px);"
                + "only screen and (min-width: 768px) and (max-width: 991px);"
                + "only screen and (min-width: 992px);"
                + "only screen and (min-width: 992px) and (max-width: 1199px);";
        realWorld("realworld/semantic_2_5_0.css", 5490, 10072, media, 3, 0);
        realWorld("realworld/semantic_2_5_0.min.css", 5490, 10072, media, 3, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void bulma_1_0_2() throws Exception {
        final String media = "(prefers-color-scheme: dark);"
                + "(prefers-color-scheme: light);"
                + "print;"
                + "screen and (max-width: 1023px);"
                + "screen and (max-width: 1215px);"
                + "screen and (max-width: 1407px);"
                + "screen and (max-width: 768px);"
                + "screen and (min-width: 1024px);"
                + "screen and (min-width: 1024px) and (max-width: 1215px);"
                + "screen and (min-width: 1216px);"
                + "screen and (min-width: 1216px) and (max-width: 1407px);"
                + "screen and (min-width: 1408px);"
                + "screen and (min-width: 769px);"
                + "screen and (min-width: 769px) and (max-width: 1023px);";
        realWorld("realworld/bulma_1_0_2.css", 3036, 7226, media, 13, 13);
        realWorld("realworld/bulma_1_0_2.min.css", 3011, 7180, media, 13, 13);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void picnic() throws Exception {
        final String media = "(max-width: 60em);"
                + "all and (max-width: 60em);"
                + "all and (min-width: 1000px);"
                + "all and (min-width: 1100px);"
                + "all and (min-width: 1200px);"
                + "all and (min-width: 1300px);"
                + "all and (min-width: 1400px);"
                + "all and (min-width: 1500px);"
                + "all and (min-width: 1600px);"
                + "all and (min-width: 1700px);"
                + "all and (min-width: 1800px);"
                + "all and (min-width: 1900px);"
                + "all and (min-width: 2000px);"
                + "all and (min-width: 500px);"
                + "all and (min-width: 600px);"
                + "all and (min-width: 700px);"
                + "all and (min-width: 800px);"
                + "all and (min-width: 900px);";
        realWorld("realworld/picnic.css", 292, 524, media, 0, 0);
        realWorld("realworld/picnic.min.css", 291, 524, media, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void spiegel() throws Exception {
        realWorld("realworld/style-V5-11.css", 2088, 6028, "screen and (min-width: 1030px);", 47, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void stackoverflow() throws Exception {
        final String media = "not all;"
                + "only screen and (min--moz-device-pixel-ratio: 1.5);"
                + "print;screen and (max-height: 740px);"
                + "screen and (max-height: 750px);"
                + "screen and (max-width: 1090px);"
                + "screen and (max-width: 920px);";
        realWorld("realworld/all.css", 5237, 12403, media, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void mui() throws Exception {
        final String media = "(-ms-high-contrast: active);"
                + "(max-width: 543px);"
                + "(min-width: 1200px);"
                + "(min-width: 480px);"
                + "(min-width: 544px);"
                + "(min-width: 544px) and (max-width: 767px);"
                + "(min-width: 768px);"
                + "(min-width: 768px) and (max-width: 991px);"
                + "(min-width: 992px);"
                + "(min-width: 992px) and (max-width: 1199px);"
                + "(orientation: landscape) and (max-height: 480px);"
                + "all and (-ms-high-contrast: none);";
        realWorld("realworld/mui.css", 342, 752, media, 0, 0);
    }

    /**
     * Test case for https://github.com/jenkinsci/jenkins/pull/10078.
     * @throws Exception if any error occurs
     */
    @Test
    public void jenkins10078() throws Exception {
        final String media = "(hover: hover);"
                + "(hover: hover) and (hover: hover);"
                + "(hover: none);(max-width: 1200px);"
                + "(max-width: 767px);(max-width: 900px);"
                + "(min-width: 1170px);(min-width: 1200px);"
                + "(min-width: 767px);"
                + "(min-width: 768px) and (max-width: 991px);"
                + "(min-width: 900px);"
                + "(min-width: 992px) and (max-width: 1199px);"
                + "(prefers-contrast: more);"
                + "(prefers-reduced-motion);"
                + "print;"
                + "screen and (max-width: 1200px);"
                + "screen and (max-width: 900px);"
                + "screen and (min-width: 1300px);"
                + "screen and (min-width: 1800px);"
                + "screen and (min-width: 800px);";
        realWorld("realworld/jenkins_10078.css", 1423, 4032, media, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void filterscontainer() throws Exception {
        final String media = "(max-width: 767px);(min-width: 640px);(min-width: 768px);";
        realWorld("realworld/filterscontainer.css", 43, 74, media, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void alibabaHugeIndex() throws Exception {
        final String media = "(max-width: 767px);(max-width: 768px);"
                + "(max-width: 991px);"
                + "(min-width: 1024px);"
                + "(min-width: 1200px);"
                + "(min-width: 1280px);"
                + "(min-width: 360px);"
                + "(min-width: 640px);"
                + "(min-width: 768px);"
                + "(min-width: 992px);"
                + "(prefers-reduced-motion: no-preference);"
                + "screen and (max-width: 1200px);"
                + "screen and (max-width: 480px);"
                + "screen and (max-width: 767px);"
                + "screen and (max-width: 768px);";
        realWorld("realworld/alibaba-huge-index.css", 3201, 6863, media, 12, 6);
    }

    private void realWorld(final String resourceName, final int rules, final int properties,
                final String media,
                final int err, final int warn) throws Exception {
        final InputStream is = getClass().getClassLoader().getResourceAsStream(resourceName);
        assertNotNull(is);

        final InputSource css = new InputSource(new InputStreamReader(is, StandardCharsets.UTF_8));
        final CSSStyleSheetImpl sheet = parse(css, err, 0, warn);

        final CSSRuleListImpl foundRules = sheet.getCssRules();
        assertEquals(rules, foundRules.getLength());

        int foundProperties = 0;
        for (int i = 0; i < foundRules.getLength(); i++) {
            final AbstractCSSRuleImpl rule = foundRules.getRules().get(i);
            if (rule instanceof CSSStyleRuleImpl) {
                foundProperties += ((CSSStyleRuleImpl) rule).getStyle().getLength();
            }
        }
        assertEquals(properties, foundProperties);

        final Set<String> mediaQ = new TreeSet<>();
        for (int i = 0; i < sheet.getCssRules().getLength(); i++) {
            final AbstractCSSRuleImpl cssRule = sheet.getCssRules().getRules().get(i);
            if (cssRule instanceof CSSMediaRuleImpl) {
                final MediaListImpl mediaList = ((CSSMediaRuleImpl) cssRule).getMediaList();
                for (int j = 0; j < mediaList.getLength(); j++) {
                    final MediaQuery mediaQuery = mediaList.mediaQuery(j);
                    assertEquals(mediaQuery.toString(), mediaQuery.toString());
                    mediaQ.add(mediaQuery.toString());
                }
            }
        }
        final StringBuilder queries = new StringBuilder();
        for (final String string : mediaQ) {
            queries.append(string);
            queries.append(";");
        }
        assertEquals(media, queries.toString());
    }
}
