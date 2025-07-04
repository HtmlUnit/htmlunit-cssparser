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
package org.htmlunit.cssparser.parser.media;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.htmlunit.cssparser.dom.Property;
import org.htmlunit.cssparser.parser.AbstractLocatable;

/**
 * <p>MediaQuery class.</p>
 *
 * @author Ronald Brill
 */
public class MediaQuery extends AbstractLocatable implements Serializable {

    private String media_;
    private final List<Property> properties_;
    private boolean implicitAll_;
    private final boolean isOnly_;
    private final boolean isNot_;

    /**
     * Ctor.
     * @param media the media string
     */
    public MediaQuery(final String media) {
        this(media, false, false);
    }

    /**
     * Ctor.
     * @param media the media string
     * @param isOnly is only flag
     * @param isNot is not flag
     */
    public MediaQuery(final String media, final boolean isOnly, final boolean isNot) {
        media_ = media;
        if (media == null) {
            implicitAll_ = true;
            media_ = "all";
        }
        properties_ = new ArrayList<>(10);
        isOnly_ = isOnly;
        isNot_ = isNot;
    }

    /**
     * <p>getMedia.</p>
     *
     * @return the media
     */
    public String getMedia() {
        return media_;
    }

    /**
     * <p>getProperties.</p>
     *
     * @return the list of properties
     */
    public List<Property> getProperties() {
        return properties_;
    }

    /**
     * Adds a property to the list.
     *
     * @param mp the property to add
     */
    public void addMediaProperty(final Property mp) {
        properties_.add(mp);
    }

    /**
     * <p>isOnly.</p>
     *
     * @return the is only flag
     */
    public boolean isOnly() {
        return isOnly_;
    }

    /**
     * <p>isNot.</p>
     *
     * @return the is not flag
     */
    public boolean isNot() {
        return isNot_;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        boolean hasMedia = false;
        if (isOnly_) {
            sb.append("only ");
            sb.append(getMedia());
            hasMedia = true;
        }
        else if (isNot_) {
            sb.append("not ");
            sb.append(getMedia());
            hasMedia = true;
        }
        else {
            if (!implicitAll_) {
                sb.append(getMedia());
                hasMedia = true;
            }
        }

        for (final Property prop : properties_) {
            if (hasMedia) {
                sb.append(" and ");
            }
            else {
                hasMedia = true;
            }
            sb.append("(");
            sb.append(prop.toString());
            sb.append(')');
        }
        return sb.toString();
    }
}
