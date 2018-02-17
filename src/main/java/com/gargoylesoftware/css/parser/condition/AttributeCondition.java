/*
 * Copyright (c) 2018 Ronald Brill.
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
package com.gargoylesoftware.css.parser.condition;

/**
 * @author Ronald Brill
 */
public interface AttributeCondition extends Condition {

    /**
     * Returns the
     * <a href="http://www.w3.org/TR/REC-xml-names/#NT-LocalPart">local part</a>
     * of the
     * <a href="http://www.w3.org/TR/REC-xml-names/#ns-qualnames">qualified
     * name</a> of this attribute.
     * <p><code>NULL</code> if :
     * <ul>
     * <li><p>this attribute condition can match any attribute.
     * <li><p>this attribute is a class attribute.
     * <li><p>this attribute is an id attribute.
     * <li><p>this attribute is a pseudo-class attribute.
     * </ul>
     */
    String getLocalName();

    /**
     * Returns the value of the attribute.
     * If this attribute is a class or a pseudo class attribute, you'll get
     * the class name (or psedo class name) without the '.' or ':'.
     */
    String getValue();
}
