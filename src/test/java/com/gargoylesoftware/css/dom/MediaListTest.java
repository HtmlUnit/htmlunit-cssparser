/*
 * Copyright (c) 2019-2020 Ronald Brill.
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

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

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
        Assert.assertEquals("", ml.toString());
        Assert.assertEquals("", ml.getMediaText());
        Assert.assertEquals(0, ml.getLength());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getMediaText() throws Exception {
        final MediaListImpl ml = new MediaListImpl(null);
        Assert.assertEquals("", ml.getMediaText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getLength() throws Exception {
        final MediaListImpl ml = new MediaListImpl(null);
        Assert.assertEquals(0, ml.getLength());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setMedia() throws Exception {
        final MediaListImpl ml = new MediaListImpl(null);
        Assert.assertEquals(0, ml.getLength());
        Assert.assertEquals("", ml.getMediaText());

        ml.setMedia(Arrays.asList("newMedium", "anotherMedium", "lastMedium"));
        Assert.assertEquals(3, ml.getLength());
        Assert.assertEquals("newMedium, anotherMedium, lastMedium", ml.getMediaText());

        ml.setMedia(Arrays.asList("somethingElse"));
        Assert.assertEquals(1, ml.getLength());
        Assert.assertEquals("somethingElse", ml.getMediaText());

        ml.setMedia(new ArrayList<String>());
        Assert.assertEquals(0, ml.getLength());
        Assert.assertEquals("", ml.getMediaText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setMediaText() throws Exception {
        final MediaListImpl ml = new MediaListImpl(null);

        ml.setMediaText("MyMediaText");
        Assert.assertEquals("MyMediaText", ml.toString());
        Assert.assertEquals("MyMediaText", ml.getMediaText());
        Assert.assertEquals(1, ml.getLength());
        Assert.assertEquals("MyMediaText", ml.mediaQuery(0).getMedia());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getMediaTextFormated() throws Exception {
        final MediaListImpl ml = new MediaListImpl(null);
        ml.setMedia(Arrays.asList("newMedium"));

        Assert.assertEquals("newMedium", ml.toString());
        Assert.assertEquals("newMedium", ml.getMediaText());
    }
}
