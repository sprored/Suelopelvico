package com.jamc68.suelopelvico;

import android.os.Handler;

public class ExerciseData {
    // Fields
    private static int totalMinutes = 10;

    // States
    private static final boolean STATE_RELAXED = true;
    private static final boolean CONTRACTION = !STATE_RELAXED;

    // Timer constants
    private static final int RELAXATION_SECONDS = 12;
    private static final int CONTRACTION_SECONDS = 6;
    private static final int TOTAL_TURNS = (totalMinutes * 60) / (CONTRACTION_SECONDS + RELAXATION_SECONDS);

    // Getters and Setters
    public static int getTotalMinutes() {
        return totalMinutes;
    }

    public static void setTotalMinutes(int totalMinutes) {
        ExerciseData.totalMinutes = totalMinutes;
    }

    public static boolean isRELAXED() {
        return STATE_RELAXED;
    }

    public static boolean isCONTRACTED() {
        return CONTRACTION;
    }

    public static int getRELAXATION_SECONDS() {
        return RELAXATION_SECONDS;
    }

    public static int getCONTRACTION_SECONDS() {
        return CONTRACTION_SECONDS;
    }

    public static int getTOTAL_TURNS() {
        return TOTAL_TURNS;
    }


}
