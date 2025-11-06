package com.example;

public class Question {
    private int imageResId;
    private String questionText;
    private boolean correctAnswer;

    public Question(int imageResId, String questionText, boolean correctAnswer) {
        this.imageResId = imageResId;
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public boolean isCorrectAnswer() {
        return correctAnswer;
    }
}

