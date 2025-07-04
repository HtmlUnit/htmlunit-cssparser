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
package org.htmlunit.cssparser.dom;

import org.htmlunit.cssparser.parser.LexicalUnit;
import org.w3c.dom.DOMException;

/**
 * Implementation of RGBColor.
 *
 * @author Ronald Brill
 */
public class RGBColorImpl extends AbstractColor {

    /**
     * Constructor that reads the values from the given
     * chain of LexicalUnits.
     * @param function the name of the function; rgb or rgba
     * @param lu the values
     * @throws DOMException in case of error
     */
    public RGBColorImpl(final String function, final LexicalUnit lu) throws DOMException {
        super(function, lu);
    }
}
