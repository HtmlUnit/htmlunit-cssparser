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

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Simple object pool for Locator instances to reduce allocations.
 * Uses ThreadLocal to avoid synchronization overhead.
 *
 * @author Ronald Brill
 */
public final class LocatorPool {

    private static final int MAX_POOL_SIZE = 32;

    private static final ThreadLocal<Deque<Locator>> POOL =
        ThreadLocal.withInitial(() -> new ArrayDeque<>(MAX_POOL_SIZE));

    private LocatorPool() {
    }

    /**
     * Acquires a Locator from the pool or creates a new one.
     *
     * @param uri The URI
     * @param line The line number
     * @param column The column number
     * @return A Locator instance
     */
    public static Locator acquire(final String uri, final int line, final int column) {
        final Deque<Locator> pool = POOL.get();
        Locator locator = pool.poll();

        if (locator == null) {
            locator = new Locator(uri, line, column);
        }
        else {
            locator.setUri(uri);
            locator.setLineNumber(line);
            locator.setColumnNumber(column);
        }

        return locator;
    }

    /**
     * Returns a Locator to the pool for reuse.
     * Note: This method is provided for completeness but typically
     * Locator objects are not explicitly released in the parser.
     *
     * @param locator The locator to return
     */
    public static void release(final Locator locator) {
        if (locator != null) {
            final Deque<Locator> pool = POOL.get();
            if (pool.size() < MAX_POOL_SIZE) {
                locator.clear();
                pool.offer(locator);
            }
        }
    }

    /**
     * Clears the pool (useful for testing).
     */
    public static void clear() {
        POOL.get().clear();
    }
}
