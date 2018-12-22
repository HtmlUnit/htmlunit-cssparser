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
package com.gargoylesoftware.css.dom;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.DOMException;

import com.gargoylesoftware.css.parser.CSSOMParser;
import com.gargoylesoftware.css.parser.InputSource;
import com.gargoylesoftware.css.util.LangUtils;

/**
 * Implementation of CSSStyleDeclaration.
 *
 * @author Ronald Brill
 */
public class CSSStyleDeclarationImpl implements Serializable {

    private static final String PRIORITY_IMPORTANT = "important";

    private AbstractCSSRuleImpl parentRule_;
    private List<Property> properties_ = new ArrayList<Property>();

    public void setParentRule(final AbstractCSSRuleImpl parentRule) {
        parentRule_ = parentRule;
    }

    public List<Property> getProperties() {
        return properties_;
    }

    public void setProperties(final List<Property> properties) {
        properties_ = properties;
    }

    public CSSStyleDeclarationImpl(final AbstractCSSRuleImpl parentRule) {
        parentRule_ = parentRule;
    }

    /**
     * {@inheritDoc}
     */
    public String getCssText() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < properties_.size(); ++i) {
            final Property p = properties_.get(i);
            if (p != null) {
                sb.append(p.toString());
            }
            if (i < properties_.size() - 1) {
                sb.append(";");
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    public void setCssText(final String cssText) throws DOMException {
        try {
            final InputSource is = new InputSource(new StringReader(cssText));
            final CSSOMParser parser = new CSSOMParser();
            properties_.clear();
            parser.parseStyleDeclaration(this, is);
        }
        catch (final Exception e) {
            throw new DOMExceptionImpl(
                DOMException.SYNTAX_ERR,
                DOMExceptionImpl.SYNTAX_ERROR,
                e.getMessage());
        }
    }

    public String getPropertyValue(final String propertyName) {
        final Property p = getPropertyDeclaration(propertyName);
        if (p == null || p.getValue() == null) {
            return "";
        }
        return p.getValue().toString();
    }

    public CSSValueImpl getPropertyCSSValue(final String propertyName) {
        final Property p = getPropertyDeclaration(propertyName);
        return (p == null) ? null : p.getValue();
    }

    public String removeProperty(final String propertyName) throws DOMException {
        if (null == propertyName) {
            return "";
        }
        for (int i = 0; i < properties_.size(); i++) {
            final Property p = properties_.get(i);
            if (p != null && propertyName.equalsIgnoreCase(p.getName())) {
                properties_.remove(i);
                if (p.getValue() == null) {
                    return "";
                }
                return p.getValue().toString();
            }
        }
        return "";
    }

    public String getPropertyPriority(final String propertyName) {
        final Property p = getPropertyDeclaration(propertyName);
        if (p == null) {
            return "";
        }
        return p.isImportant() ? PRIORITY_IMPORTANT : "";
    }

    public void setProperty(
            final String propertyName,
            final String value,
            final String priority) throws DOMException {
        try {
            CSSValueImpl expr = null;
            if (!value.isEmpty()) {
                final CSSOMParser parser = new CSSOMParser();
                final InputSource is = new InputSource(new StringReader(value));
                expr = parser.parsePropertyValue(is);
            }
            Property p = getPropertyDeclaration(propertyName);
            final boolean important = PRIORITY_IMPORTANT.equalsIgnoreCase(priority);
            if (p == null) {
                p = new Property(propertyName, expr, important);
                addProperty(p);
            }
            else {
                p.setValue(expr);
                p.setImportant(important);
            }
        }
        catch (final Exception e) {
            throw new DOMExceptionImpl(
                    DOMException.SYNTAX_ERR,
                    DOMExceptionImpl.SYNTAX_ERROR,
                    e.getMessage());
        }
    }

    public int getLength() {
        return properties_.size();
    }

    public String item(final int index) {
        final Property p = properties_.get(index);
        return (p == null) ? "" : p.getName();
    }

    public AbstractCSSRuleImpl getParentRule() {
        return parentRule_;
    }

    public void addProperty(final Property p) {
        if (null == p) {
            return;
        }
        properties_.add(p);
    }

    public Property getPropertyDeclaration(final String propertyName) {
        if (null == propertyName) {
            return null;
        }
        for (int i = properties_.size() - 1; i > -1; i--) {
            final Property p = properties_.get(i);
            if (p != null && propertyName.equalsIgnoreCase(p.getName())) {
                return p;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return getCssText();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CSSStyleDeclarationImpl)) {
            return false;
        }
        final CSSStyleDeclarationImpl csd = (CSSStyleDeclarationImpl) obj;

        // don't use parentRule in equals()
        // recursive loop -> stack overflow!
        return equalsProperties(csd);
    }

    private boolean equalsProperties(final CSSStyleDeclarationImpl csd) {
        if ((csd == null) || (getLength() != csd.getLength())) {
            return false;
        }
        for (int i = 0; i < getLength(); i++) {
            final String propertyName = item(i);
            // CSSValue propertyCSSValue1 = getPropertyCSSValue(propertyName);
            // CSSValue propertyCSSValue2 = csd.getPropertyCSSValue(propertyName);
            final String propertyValue1 = getPropertyValue(propertyName);
            final String propertyValue2 = csd.getPropertyValue(propertyName);
            if (!LangUtils.equals(propertyValue1, propertyValue2)) {
                return false;
            }
            final String propertyPriority1 = getPropertyPriority(propertyName);
            final String propertyPriority2 = csd.getPropertyPriority(propertyName);
            if (!LangUtils.equals(propertyPriority1, propertyPriority2)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = LangUtils.HASH_SEED;
        // don't use parentRule in hashCode()
        // recursive loop -> stack overflow!
        hash = LangUtils.hashCode(hash, properties_);
        return hash;
    }
}
