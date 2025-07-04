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

/**
 * Interface for CSS parser error handlers.
 * @author Ronald Brill
 */
public interface CSSErrorHandler {

    /**
     * Warning.
     *
     * @param exception the {@link CSSParseException} that is the reason for the warning.
     * @exception CSSException in case of error
     * @throws CSSException if any.
     */
    void warning(CSSParseException exception) throws CSSException;

    /**
     * Error.
     *
     * @param exception the {@link CSSParseException} that is the reason for the error.
     * @exception CSSException in case of error
     * @throws CSSException if any.
     */
    void error(CSSParseException exception) throws CSSException;

    /**
     * Fatal error.
     *
     * @param exception the {@link CSSParseException} that is the reason for the error.
     * @exception CSSException in case of error
     * @throws CSSException if any.
     */
    void fatalError(CSSParseException exception) throws CSSException;
}
