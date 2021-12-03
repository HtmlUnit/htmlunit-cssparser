/*
 * Copyright (c) 2019-2021 Ronald Brill.
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
package com.gargoylesoftware.css;

import com.gargoylesoftware.css.parser.CSSException;
import com.gargoylesoftware.css.parser.CSSParseException;

/**
 * @author Ronald Brill
 */
public class ErrorHandler implements com.gargoylesoftware.css.parser.CSSErrorHandler {

    private int errorCount_;
    private final StringBuilder errorMsg_ = new StringBuilder();
    private final StringBuilder errorLines_ = new StringBuilder();
    private final StringBuilder errorColumns_ = new StringBuilder();

    private int fatalErrorCount_;
    private final StringBuilder fatalErrorMsg_ = new StringBuilder();
    private final StringBuilder fatalErrorLines_ = new StringBuilder();
    private final StringBuilder fatalErrorColumns_ = new StringBuilder();

    private int warningCount_;
    private final StringBuilder warningMsg_ = new StringBuilder();
    private final StringBuilder warningLines_ = new StringBuilder();
    private final StringBuilder warningColumns_ = new StringBuilder();

    @Override
    public void error(final CSSParseException e) throws CSSException {
        errorCount_++;
        errorMsg_.append(e.getMessage()).append(" ");
        errorLines_.append(e.getLineNumber()).append(" ");
        errorColumns_.append(e.getColumnNumber()).append(" ");
    }

    @Override
    public void fatalError(final CSSParseException e) throws CSSException {
        fatalErrorCount_++;
        fatalErrorMsg_.append(e.getMessage()).append(" ");
        fatalErrorLines_.append(e.getLineNumber()).append(" ");
        fatalErrorColumns_.append(e.getColumnNumber()).append(" ");
    }

    @Override
    public void warning(final CSSParseException e) throws CSSException {
        warningCount_++;
        warningMsg_.append(e.getMessage()).append(" ");
        warningLines_.append(e.getLineNumber()).append(" ");
        warningColumns_.append(e.getColumnNumber()).append(" ");
    }

    /**
     * @return the error count
     */
    public int getErrorCount() {
        return errorCount_;
    }

    /**
     * @return the error message
     */
    public String getErrorMessage() {
        return errorMsg_.toString().trim();
    }

    /**
     * @return the error lines
     */
    public String getErrorLines() {
        return errorLines_.toString().trim();
    }

    /**
     * @return the error columns
     */
    public String getErrorColumns() {
        return errorColumns_.toString().trim();
    }

    /**
     * @return the fatal error count
     */
    public int getFatalErrorCount() {
        return fatalErrorCount_;
    }

    /**
     * @return the fatal error message
     */
    public String getFatalErrorMessage() {
        return fatalErrorMsg_.toString().trim();
    }

    /**
     * @return the fatal error lines
     */
    public String getFatalErrorLines() {
        return fatalErrorLines_.toString().trim();
    }

    /**
     * @return the fatal error columns
     */
    public String getFatalErrorColumns() {
        return fatalErrorColumns_.toString().trim();
    }

    /**
     * @return the warning count
     */
    public int getWarningCount() {
        return warningCount_;
    }

    /**
     * @return the warning message
     */
    public String getWarningMessage() {
        return warningMsg_.toString().trim();
    }

    /**
     * @return the warning lines
     */
    public String getWarningLines() {
        return warningLines_.toString().trim();
    }

    /**
     * @return the warning columns
     */
    public String getWarningColumns() {
        return warningColumns_.toString().trim();
    }
}
