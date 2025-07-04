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

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.htmlunit.cssparser.parser.AbstractLocatable;
import org.htmlunit.cssparser.parser.CSSOMParser;
import org.htmlunit.cssparser.parser.CSSParseException;
import org.htmlunit.cssparser.parser.media.MediaQuery;
import org.htmlunit.cssparser.parser.media.MediaQueryList;
import org.htmlunit.cssparser.util.ParserUtils;
import org.htmlunit.cssparser.util.ThrowCssExceptionErrorHandler;
import org.w3c.dom.DOMException;

/**
 * Implementation of MediaList.
 *
 * @author Ronald Brill
 */
public class MediaListImpl extends AbstractLocatable implements Serializable {

    private final List<MediaQuery> mediaQueries_;

    /**
     * Creates new MediaList.
     * @param mediaList the media list
     */
    public MediaListImpl(final MediaQueryList mediaList) {
        mediaQueries_ = new ArrayList<>(10);

        setMediaList(mediaList);
        if (mediaList != null) {
            setLocator(mediaList.getLocator());
        }
    }

    /**
     * <p>getMediaText.</p>
     *
     * @return the media text
     */
    public String getMediaText() {
        final StringBuilder sb = new StringBuilder();
        boolean isNotFirst = false;
        for (final MediaQuery mediaQuery : mediaQueries_) {
            if (isNotFirst) {
                sb.append(", ");
            }
            else {
                isNotFirst = true;
            }
            sb.append(mediaQuery.toString());
        }
        return sb.toString();
    }

    /**
     * Parses the given media text.
     * @param mediaText text to be parsed
     * @throws DOMException in case of error
     */
    public void setMediaText(final String mediaText) throws DOMException {
        try {
            final CSSOMParser parser = new CSSOMParser();
            parser.setErrorHandler(ThrowCssExceptionErrorHandler.INSTANCE);
            final MediaQueryList sml = parser.parseMedia(mediaText);
            setMediaList(sml);
        }
        catch (final CSSParseException e) {
            throw new DOMException(DOMException.SYNTAX_ERR, e.getLocalizedMessage());
        }
        catch (final IOException e) {
            throw new DOMException(DOMException.NOT_FOUND_ERR, e.getLocalizedMessage());
        }
    }

    /**
     * <p>getLength.</p>
     *
     * @return the media query count
     */
    public int getLength() {
        return mediaQueries_.size();
    }

    /**
     * <p>mediaQuery.</p>
     *
     * @param index the position of the media query
     * @return the media query at the given pos
     */
    public MediaQuery mediaQuery(final int index) {
        if (index < 0 || (index >= mediaQueries_.size())) {
            return null;
        }
        return mediaQueries_.get(index);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return getMediaText();
    }

    /**
     * Resets the list of media queries.
     * @param media the media queries string to be parsed
     */
    public void setMedia(final List<String> media) {
        mediaQueries_.clear();
        for (final String medium : media) {
            mediaQueries_.add(new MediaQuery(medium));
        }
    }

    private void setMediaList(final MediaQueryList mediaList) {
        if (mediaList != null) {
            mediaQueries_.addAll(mediaList.getMediaQueries());
        }
    }

    private boolean equalsMedia(final MediaListImpl ml) {
        if ((ml == null) || (getLength() != ml.getLength())) {
            return false;
        }

        int i = 0;
        for (final MediaQuery mediaQuery : mediaQueries_) {
            final String m1 = mediaQuery.getMedia();
            final String m2 = ml.mediaQuery(i).getMedia();
            if (!ParserUtils.equals(m1, m2)) {
                return false;
            }
            i++;
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MediaListImpl)) {
            return false;
        }
        final MediaListImpl ml = (MediaListImpl) obj;
        return super.equals(obj) && equalsMedia(ml);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = ParserUtils.hashCode(hash, mediaQueries_);
        return hash;
    }
}
