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
package org.htmlunit.cssparser.util;

import java.util.Objects;

/**
 * Util methods.
 *
 * @author Ronald Brill
 */
public final class ParserUtils {

    /** HASH_SEED = 17. */
    public static final int HASH_SEED = 17;

    /** HASH_OFFSET = 37. */
    public static final int HASH_OFFSET = 37;

    private ParserUtils() {
    }

    /**
     * @param seed the seed to be used
     * @param hashcode the hashcode to be used as input
     * @return a hash code calculated based on a given one.
     */
    public static int hashCode(final int seed, final int hashcode) {
        return seed * HASH_OFFSET + hashcode;
    }

    /**
     * @param seed the seed to be used
     * @param b the boolean to be used as input
     * @return a hash code calculated based on a given boolean.
     */
    public static int hashCode(final int seed, final boolean b) {
        return hashCode(seed, b ? 1 : 0);
    }

    /**
     * @param seed the seed to be used
     * @param obj the object to be used as input
     * @return a hash code calculated based on a given object.
     */
    public static int hashCode(final int seed, final Object obj) {
        return hashCode(seed, obj != null ? obj.hashCode() : 0);
    }

    /**
     * @param obj1 the first object
     * @param obj2 the second object
     * @return true if the both objects are equals
     */
    public static boolean equals(final Object obj1, final Object obj2) {
        return Objects.equals(obj1, obj2);
    }

    /**
     * Remove the given number of chars from start and end.
     * There is no parameter checking, the caller has to take care of this.
     *
     * @param s the StringBuilder
     * @param left no of chars to be removed from start
     * @param right no of chars to be removed from end
     * @return the trimmed string
     */
    public static String trimBy(final StringBuilder s, final int left, final int right) {
        return s.substring(left, s.length() - right);
    }

    /**
     * Remove the given number of chars from start and end.
     * Optimized version with bounds checking for String input.
     *
     * @param s the String
     * @param left no of chars to be removed from start
     * @param right no of chars to be removed from end
     * @return the trimmed string
     */
    public static String trimBy(final String s, final int left, final int right) {
        if (s == null) {
            return null;
        }

        final int length = s.length();

        if (left < 0 || right < 0 || left + right >= length) {
            return s;
        }

        if (left == 0 && right == 0) {
            return s;
        }

        return s.substring(left, length - right);
    }

    /**
     * Helper that removes the leading "url(", the trailing ")"
     * and surrounding quotes from the given string builder.
     * @param s the StringBuilder
     * @return the trimmed string
     */
    public static String trimUrl(final StringBuilder s) {
        final String s1 = trimBy(s, 4, 1).trim();
        if (s1.length() == 0) {
            return s1;
        }

        final int end = s1.length() - 1;
        final char c0 = s1.charAt(0);
        if ((c0 == '"' && s1.charAt(end) == '"')
            || (c0 == '\'' && s1.charAt(end) == '\'')) {
            return s1.substring(1, end);
        }

        return s1;
    }

    /**
     * Compare CharSequence without creating String objects.
     * Case-insensitive comparison.
     *
     * @param cs1 the first CharSequence
     * @param cs2 the second CharSequence
     * @return true if the CharSequences are equal ignoring case
     */
    public static boolean equalsIgnoreCase(final CharSequence cs1, final CharSequence cs2) {
        if (cs1 == cs2) {
            return true;
        }
        if (cs1 == null || cs2 == null) {
            return false;
        }
        if (cs1.length() != cs2.length()) {
            return false;
        }

        for (int i = 0; i < cs1.length(); i++) {
            final char c1 = cs1.charAt(i);
            final char c2 = cs2.charAt(i);
            if (c1 != c2 && Character.toLowerCase(c1) != Character.toLowerCase(c2)) {
                return false;
            }
        }

        return true;
    }

}
