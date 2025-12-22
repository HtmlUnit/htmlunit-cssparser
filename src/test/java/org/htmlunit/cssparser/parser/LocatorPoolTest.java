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
package org.htmlunit.cssparser.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Testcases for {@link LocatorPool}.
 *
 * @author Ronald Brill
 */
public class LocatorPoolTest {

    @AfterEach
    public void tearDown() {
        LocatorPool.clear();
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void acquireCreatesNewLocator() throws Exception {
        final Locator locator = LocatorPool.acquire("test.css", 10, 20);

        assertNotNull(locator);
        assertEquals("test.css", locator.getUri());
        assertEquals(10, locator.getLineNumber());
        assertEquals(20, locator.getColumnNumber());
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void releaseAndReacquireReusesLocator() throws Exception {
        final Locator locator1 = LocatorPool.acquire("test.css", 10, 20);
        LocatorPool.release(locator1);

        final Locator locator2 = LocatorPool.acquire("test2.css", 30, 40);

        assertSame(locator1, locator2);
        assertEquals("test2.css", locator2.getUri());
        assertEquals(30, locator2.getLineNumber());
        assertEquals(40, locator2.getColumnNumber());
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void clearResetsFields() throws Exception {
        final Locator locator = LocatorPool.acquire("test.css", 10, 20);
        locator.clear();

        assertNull(locator.getUri());
        assertEquals(0, locator.getLineNumber());
        assertEquals(0, locator.getColumnNumber());
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void poolSizeLimit() throws Exception {
        // Fill the pool beyond its capacity
        for (int i = 0; i < 40; i++) {
            final Locator locator = LocatorPool.acquire("test" + i + ".css", i, i);
            LocatorPool.release(locator);
        }

        // Clear and verify pool is empty
        LocatorPool.clear();

        // Acquire should create new locators since pool was cleared
        final Locator locator1 = LocatorPool.acquire("test.css", 1, 1);
        final Locator locator2 = LocatorPool.acquire("test.css", 2, 2);

        assertNotSame(locator1, locator2);
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void releaseNullDoesNotThrow() throws Exception {
        LocatorPool.release(null);
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void threadLocalIsolation() throws Exception {
        final Locator locator1 = LocatorPool.acquire("main.css", 1, 1);
        LocatorPool.release(locator1);

        final Thread thread = new Thread(() -> {
            final Locator locator2 = LocatorPool.acquire("thread.css", 2, 2);
            // In a different thread, should get a different locator
            assertNotSame(locator1, locator2);
            LocatorPool.release(locator2);
        });

        thread.start();
        thread.join();

        // Back in main thread, should get the original locator
        final Locator locator3 = LocatorPool.acquire("main2.css", 3, 3);
        assertSame(locator1, locator3);
    }
}
