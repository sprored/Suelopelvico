package com.jamc68.suelopelvico;

public class ExerciseSession {
    // Fields
    int totalMinutes = 10;
    int turnCounter = 0;

    // States
    final int RELAXATION = 0;
    final int CONTRACTION = 1;

    // Timer constants
    final int RELAXATION_SECONDS = 12;
    final int CONTRACTION_SECONDS = 6;
    final int TOTAL_TURNS = (totalMinutes * 60) / (CONTRACTION_SECONDS + RELAXATION_SECONDS);

    // Getters and Setters
    public int getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(int totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public int getTurnCounter() {
        return turnCounter;
    }

    public void setTurnCounter(int turnCounter) {
        this.turnCounter = turnCounter;
    }

    public int getRELAXATION() {
        return RELAXATION;
    }

    public int getCONTRACTION() {
        return CONTRACTION;
    }

    public int getRELAXATION_SECONDS() {
        return RELAXATION_SECONDS;
    }

    public int getCONTRACTION_SECONDS() {
        return CONTRACTION_SECONDS;
    }

    public int getTOTAL_TURNS() {
        return TOTAL_TURNS;
    }
}
