package org.monplan.exceptions;

public class MonPlanAssertion {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    public static void assertTrue(boolean condition, String errorMessage) {
        if (!condition) {
            System.out.println(ANSI_RED + "AssertionWarning: " + errorMessage + ANSI_RESET);
        }
    }
}

