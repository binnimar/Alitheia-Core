/*
 * This file is part of the Alitheia system, developed by the SQO-OSS
 * consortium as part of the IST FP6 SQO-OSS project, number 033331.
 *
 * Copyright 2008 - 2010 - Organization for Free and Open Source Software,  
 *                Athens, Greece.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials provided
 *       with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package eu.sqooss.impl.service.logging;

import eu.sqooss.service.logging.Logger;

public class LoggerImpl implements Logger {

    // We use an object for synchronising between threads.
    private Object lockObject = new Object();

    // The name of this logger.
    private String name;

    // The actual backend logger being used.
    private org.apache.log4j.Logger theLogger;

    // Reference count this logger. Used by LogManager.
    private int takingsNumber;

    public LoggerImpl(String name) {
        this.name = name;
        takingsNumber = 0;
        theLogger = org.apache.log4j.Logger.getLogger(name);
    }

    public String getName() {
        return name;
    }

    public void debug(String message) {
        synchronized (lockObject) {
            theLogger.debug(message);
        }
    }

    public void info(String message) {
        synchronized (lockObject) {
            theLogger.info(message);
        }
    }

    public void warn(String message) {
        synchronized (lockObject) {
            theLogger.warn(message);
        }
    }

    public void warn(String message, Exception e) {
        synchronized (lockObject) {
            theLogger.warn(message);
            theLogger.warn(renderStackTrace(e));
        }
    }

    public void error(String message) {
        synchronized (lockObject) {
            theLogger.error(message);
        }
    }

    public void error(String message, Exception e) {
        synchronized (lockObject) {
            theLogger.error(message);
            theLogger.error(renderStackTrace(e));
        }
    }

    protected void get() {
        takingsNumber++;
    }

    protected int unget() {
        return --takingsNumber;
    }

    private static String renderStackTrace(Exception e) {
        if (e==null) {
            return "";
        }

        StringBuilder b = new StringBuilder();
        b.append(e.getMessage());
        b.append(":\n");
        StackTraceElement stack[] = e.getStackTrace();
        if ((b==null) || (stack==null)) {
            return "";
        }
        for (StackTraceElement s : stack) {
            if (s!=null) {
                b.append("    ");
                b.append(s.toString());
                b.append("\n");
            }
        }
        return b.toString();
    }
}

// vi: ai nosi sw=4 ts=4 expandtab

