/*
 * Copyright (c) 2019-2021 Ronald Brill.
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
package com.gargoylesoftware.css.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringReader;

import org.junit.jupiter.api.Test;

import com.gargoylesoftware.css.ErrorHandler;
import com.gargoylesoftware.css.parser.javacc.CSS3Parser;

/**
 * @author Ronald Brill
 */
public class XhtmlCssTest {

    private static final String CSS_CODE = "<!--/*--><![CDATA[/*><!--*/ \n"
        + "body { color: #000000; background-color: #FFFFFF; }\n"
        + "a:link { color: #0000CC; }\n"
        + "p, address {margin-left: 3em;}\n"
        + "span {font-size: smaller;}\n"
        + "/*]]>*/-->";
    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void xhtmlCssCSS3() throws Exception {
        xhtmlCss(new CSS3Parser());
    }

    private void xhtmlCss(final AbstractCSSParser cssParser) throws Exception {
        final ErrorHandler errorHandler = new ErrorHandler();
        cssParser.setErrorHandler(errorHandler);

        final InputSource source = new InputSource(new StringReader(CSS_CODE));

        cssParser.parseStyleSheet(source);

        assertEquals(0, errorHandler.getFatalErrorCount());
        assertEquals(0, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getWarningCount());
    }
}
