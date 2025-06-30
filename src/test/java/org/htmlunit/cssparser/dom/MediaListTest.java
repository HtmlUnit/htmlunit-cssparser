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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link MediaListImpl}.
 *
 * @author Ronald Brill
 */
public class MediaListTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void defaultConstructor() throws Exception {
        final MediaListImpl ml = new MediaListImpl(null);
        assertEquals("", ml.toString());
        assertEquals("", ml.getMediaText());
        assertEquals(0, ml.getLength());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getMediaText() throws Exception {
        final MediaListImpl ml = new MediaListImpl(null);
        assertEquals("", ml.getMediaText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getLength() throws Exception {
        final MediaListImpl ml = new MediaListImpl(null);
        assertEquals(0, ml.getLength());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setMedia() throws Exception {
        final MediaListImpl ml = new MediaListImpl(null);
        assertEquals(0, ml.getLength());
        assertEquals("", ml.getMediaText());

        ml.setMedia(Arrays.asList("newMedium", "anotherMedium", "lastMedium"));
        assertEquals(3, ml.getLength());
        assertEquals("newMedium, anotherMedium, lastMedium", ml.getMediaText());

        ml.setMedia(Collections.singletonList("somethingElse"));
        assertEquals(1, ml.getLength());
        assertEquals("somethingElse", ml.getMediaText());

        ml.setMedia(new ArrayList<>());
        assertEquals(0, ml.getLength());
        assertEquals("", ml.getMediaText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setMediaText() throws Exception {
        final MediaListImpl ml = new MediaListImpl(null);

        ml.setMediaText("MyMediaText");
        assertEquals("MyMediaText", ml.toString());
        assertEquals("MyMediaText", ml.getMediaText());
        assertEquals(1, ml.getLength());
        assertEquals("MyMediaText", ml.mediaQuery(0).getMedia());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getMediaTextFormated() throws Exception {
        final MediaListImpl ml = new MediaListImpl(null);
        ml.setMedia(Collections.singletonList("newMedium"));

        assertEquals("newMedium", ml.toString());
        assertEquals("newMedium", ml.getMediaText());
    }
}
