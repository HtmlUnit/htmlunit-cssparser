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
package org.htmlunit.css;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Reader;

import org.htmlunit.css.dom.AbstractCSSRuleImpl;
import org.htmlunit.css.dom.CSSRuleListImpl;
import org.htmlunit.css.dom.CSSStyleSheetImpl;
import org.htmlunit.css.parser.CSSOMParser;
import org.htmlunit.css.parser.InputSource;
import org.junit.jupiter.api.Test;

/**
 * @author Ronald Brill
 */
public class SerializableTest {

    /**
     * @throws Exception on failure
     */
    @Test
    public void test() throws Exception {
        final InputStream is = getClass().getClassLoader().getResourceAsStream("html40.css");
        assertNotNull(is);

        final CSSOMParser parser = new CSSOMParser();

        final Reader r = new InputStreamReader(is);
        final InputSource source = new InputSource(r);

        final CSSStyleSheetImpl stylesheet = parser.parseStyleSheet(source, null);

        // Serialize the style sheet
        final File temp = File.createTempFile("temp", "css");
        temp.deleteOnExit();
        final FileOutputStream fo = new FileOutputStream(temp);
        final ObjectOutput oo = new ObjectOutputStream(fo);
        oo.writeObject(stylesheet);
        oo.close();

        // Read it back in
        final FileInputStream fi = new FileInputStream(temp);
        final ObjectInput oi = new ObjectInputStream(fi);
        final CSSStyleSheetImpl stylesheet2 = (CSSStyleSheetImpl) oi.readObject();
        oi.close();

        final CSSRuleListImpl rules = stylesheet2.getCssRules();

        // TODO
        for (int i = 0; i < rules.getLength(); i++) {
            final AbstractCSSRuleImpl rule = rules.getRules().get(i);
            System.out.println(rule.getCssText());
        }
    }

}
