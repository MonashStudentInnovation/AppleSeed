/*
 * This file is a component of thundr, a software library from 3wks.
 * Read more: http://3wks.github.io/thundr/
 * Copyright (C) 2015 3wks, <thundr@3wks.com.au>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.threewks.gaetools.logger;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.logging.Level;

public class Logger {
    public static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger("thundr");

    /**
     * Logs the given content at debug level.
     *
     * @param content String content to log
     * @see #willDebug()
     */
    public static void debug(String content) {
        if (willDebug()) {
            logger.fine(content);
        }
    }

    /**
     * Logs the given arguments formatted using the given string format at debug level.
     *
     * @param format String format, see {@link String#format(String, Object...)}
     * @param args   the arguments to format
     * @see String#format(String, Object...)
     * @see #willDebug()
     */
    public static void debug(String format, Object... args) {
        if (willDebug()) {
            logger.fine(format(format, args));
        }
    }

    /**
     * Logs the given content at info level
     *
     * @param content String content to log
     * @see #willInfo()
     */
    public static void info(String content) {
        if (willInfo()) {
            logger.info(content);
        }
    }

    /**
     * Logs the given arguments formatted using the given string format at info level
     *
     * @param format String format, see {@link String#format(String, Object...)}
     * @param args   the arguments to format
     * @see String#format(String, Object...)
     * @see #willInfo()
     */
    public static void info(String format, Object... args) {
        if (willInfo()) {
            logger.info(format(format, args));
        }
    }

    /**
     * Logs the content at warn level
     *
     * @param content String content to log
     * @see #willWarn()
     */
    public static void warn(String content) {
        if (willWarn()) {
            logger.warning(content);
        }
    }

    /**
     * Logs the given arguments formatted using the given string format at warn level
     *
     * @param format String format, see {@link String#format(String, Object...)}
     * @param args   the arguments to format
     * @see String#format(String, Object...)
     * @see #willWarn()
     */
    public static void warn(String format, Object... args) {
        if (willWarn()) {
            logger.warning(format(format, args));
        }
    }

    /**
     * Logs the content at error level
     *
     * @param content String content to log
     * @see #willError()
     */
    public static void error(String content) {
        if (willError()) {
            logger.severe(content);
        }
    }

    /**
     * Logs the given arguments formatted using the given string format at error level, followed by the
     * stack trace of the given {@link Throwable}
     *
     * @param throwable the stacktrace for this throwable will be included in full
     * @param format    String format, see {@link String#format(String, Object...)}
     * @param args      the arguments to format
     * @see String#format(String, Object...)
     * @see #willError()
     */
    public static void error(Throwable throwable, String format, Object... args) {
        if (willError()) {
            String stacktrace = throwable == null ? "" : "\n" + ExceptionUtils.getStackTrace(throwable);
            logger.severe(format(format, args) + stacktrace);
        }
    }

    /**
     * Logs the given arguments formatted using the given string format at error level
     *
     * @param format String format, see {@link String#format(String, Object...)}
     * @param args   the arguments to format
     * @see String#format(String, Object...)
     * @see #willError()
     */
    public static void error(String format, Object... args) {
        if (willError()) {
            logger.severe(format(format, args));
        }
    }

    /**
     * Returns true if the logger will log at debug level or above, false if not
     */
    public static boolean willDebug() {
        return logger.isLoggable(Level.FINE);
    }

    /**
     * Returns true if the logger will log at info level or above, false if not
     */
    public static boolean willInfo() {
        return logger.isLoggable(Level.INFO);
    }

    /**
     * Returns true if the logger will log at warn level or above, false if not
     */
    public static boolean willWarn() {
        return logger.isLoggable(Level.WARNING);
    }

    /**
     * Returns true if the logger will log at error level or above, false if not
     */
    public static boolean willError() {
        return logger.isLoggable(Level.SEVERE);
    }

    private static String format(String format, Object... args) {
        return args == null || args.length == 0 ? format : String.format(format, args);
    }
}
