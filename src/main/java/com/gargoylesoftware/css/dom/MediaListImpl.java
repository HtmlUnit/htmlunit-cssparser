/*
 * Copyright (c) 2019 Ronald Brill.
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

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.DOMException;

import com.gargoylesoftware.css.parser.AbstractLocatable;
import com.gargoylesoftware.css.parser.CSSOMParser;
import com.gargoylesoftware.css.parser.CSSParseException;
import com.gargoylesoftware.css.parser.InputSource;
import com.gargoylesoftware.css.parser.media.MediaQuery;
import com.gargoylesoftware.css.parser.media.MediaQueryList;
import com.gargoylesoftware.css.util.LangUtils;
import com.gargoylesoftware.css.util.ThrowCssExceptionErrorHandler;

/**
 * Implementation of MediaList.
 *
 * @author Ronald Brill
 */
public class MediaListImpl extends AbstractLocatable implements Serializable {

    private List<MediaQuery> mediaQueries_;

    /**
     * Creates new MediaList.
     * @param mediaList the media list
     */
    public MediaListImpl(final MediaQueryList mediaList) {
        mediaQueries_ = new ArrayList<MediaQuery>(10);

        setMediaList(mediaList);
        if (mediaList != null) {
            setLocator(mediaList.getLocator());
        }
    }

    public String getMediaText() {
        final StringBuilder sb = new StringBuilder("");
        boolean isNotFirst = false;
        for (MediaQuery mediaQuery : mediaQueries_) {
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

    public void setMediaText(final String mediaText) throws DOMException {
        final InputSource source = new InputSource(new StringReader(mediaText));
        try {
            final CSSOMParser parser = new CSSOMParser();
            parser.setErrorHandler(ThrowCssExceptionErrorHandler.INSTANCE);
            final MediaQueryList sml = parser.parseMedia(source);
            setMediaList(sml);
        }
        catch (final CSSParseException e) {
            throw new DOMException(DOMException.SYNTAX_ERR, e.getLocalizedMessage());
        }
        catch (final IOException e) {
            throw new DOMException(DOMException.NOT_FOUND_ERR, e.getLocalizedMessage());
        }
    }

    public int getLength() {
        return mediaQueries_.size();
    }

    /**
     * @param index the position of the media query
     * @return the media query at the given pos
     */
    public MediaQuery mediaQuery(final int index) {
        if (index < 0 || (index >= mediaQueries_.size())) {
            return null;
        }
        return mediaQueries_.get(index);
    }

    public void deleteMedium(final String oldMedium) throws DOMException {
        for (MediaQuery mediaQuery : mediaQueries_) {
            final String str = mediaQuery.getMedia();
            if (str.equalsIgnoreCase(oldMedium)) {
                mediaQueries_.remove(mediaQuery);
                return;
            }
        }
        throw new DOMExceptionImpl(DOMException.NOT_FOUND_ERR, DOMExceptionImpl.NOT_FOUND);
    }

    public void appendMedium(final String newMedium) throws DOMException {
        mediaQueries_.add(new MediaQuery(newMedium));
    }

    @Override
    public String toString() {
        return getMediaText();
    }

    public void setMedia(final List<String> media) {
        mediaQueries_.clear();
        for (String medium : media) {
            mediaQueries_.add(new MediaQuery(medium));
        }
    }

    private void setMediaList(final MediaQueryList mediaList) {
        if (mediaList != null) {
            for (int i = 0; i < mediaList.getLength(); i++) {
                mediaQueries_.add(mediaList.mediaQuery(i));
            }
        }
    }

    private boolean equalsMedia(final MediaListImpl ml) {
        if ((ml == null) || (getLength() != ml.getLength())) {
            return false;
        }

        int i = 0;
        for (MediaQuery mediaQuery : mediaQueries_) {
            final String m1 = mediaQuery.getMedia();
            final String m2 = ml.mediaQuery(i).getMedia();
            if (!LangUtils.equals(m1, m2)) {
                return false;
            }
            i++;
        }
        return true;
    }

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

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = LangUtils.hashCode(hash, mediaQueries_);
        return hash;
    }
}
