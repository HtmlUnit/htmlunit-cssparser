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
package org.htmlunit.cssparser.parser.validator;

import org.htmlunit.cssparser.parser.Locator;

/**
 * Represents a validation warning or error.
 *
 * @author Ronald Brill
 */
public class ValidationWarning {

    /**
     * Warning level.
     */
    public enum Level {
        /** INFO. */
        INFO,
        /** WARNING. */
        WARNING,
        /** ERROR. */
        ERROR
    }

    private final String message_;
    private final Locator locator_;
    private final Level level_;

    /**
     * Creates new ValidationWarning.
     * @param message the message
     * @param locator the locator
     * @param level the level
     */
    public ValidationWarning(final String message, final Locator locator, final Level level) {
        message_ = message;
        locator_ = locator;
        level_ = level;
    }

    /**
     * <p>getMessage.</p>
     *
     * @return the message
     */
    public String getMessage() {
        return message_;
    }

    /**
     * <p>getLocator.</p>
     *
     * @return the locator
     */
    public Locator getLocator() {
        return locator_;
    }

    /**
     * <p>getLevel.</p>
     *
     * @return the level
     */
    public Level getLevel() {
        return level_;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[").append(level_).append("] ");
        if (locator_ != null) {
            sb.append("Line ").append(locator_.getLineNumber());
            sb.append(", Column ").append(locator_.getColumnNumber());
            sb.append(": ");
        }
        sb.append(message_);
        return sb.toString();
    }
}
