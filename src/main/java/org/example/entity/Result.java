package org.example.entity;

public class Result {

    private int scoredPoints;
    private String category;

    public Result(int scoredPoints, String category) {
        this.scoredPoints = scoredPoints;
        this.category = category;
    }

    public int getScoredPoints() {
        return scoredPoints;
    }

    public void setScoredPoints(int scoredPoints) {
        this.scoredPoints = scoredPoints;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
