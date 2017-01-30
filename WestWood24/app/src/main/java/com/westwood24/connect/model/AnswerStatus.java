package com.westwood24.connect.model;

/**
 * Created by aipxperts on 2/1/17.
 */
public class AnswerStatus {

    String article_ida;
    String question_ida;
    String answer_flaga;

    public String getArticle_ida() {
        return article_ida;
    }

    public void setArticle_ida(String article_ida) {
        this.article_ida = article_ida;
    }

    public String getAnswer_flaga() {
        return answer_flaga;
    }

    public void setAnswer_flaga(String answer_flaga) {
        this.answer_flaga = answer_flaga;
    }

    public String getQuestion_ida() {
        return question_ida;
    }

    public void setQuestion_ida(String question_ida) {
        this.question_ida = question_ida;
    }
}
