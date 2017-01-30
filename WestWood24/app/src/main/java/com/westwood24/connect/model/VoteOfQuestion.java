package com.westwood24.connect.model;

import java.util.ArrayList;

/**
 * Created by dharmesh on 1/9/16.
 */
public class VoteOfQuestion {
    String questId;
    String questiontitle;
    String articalId;
    String answer;
    String votetype;
    String answerflag;
    String votable;
    ArrayList<String> OptionCount = new ArrayList<>();
    ArrayList<String> voteoptions = new ArrayList<>();

    public String getQuestId() {
        return questId;
    }

    public void setQuestId(String questId) {
        this.questId = questId;
    }

    public String getQuestiontitle() {
        return questiontitle;
    }

    public void setQuestiontitle(String questiontitle) {
        this.questiontitle = questiontitle;
    }

    public String getArticalId() {
        return articalId;
    }

    public void setArticalId(String articalId) {
        this.articalId = articalId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getVotetype() {
        return votetype;
    }

    public void setVotetype(String votetype) {
        this.votetype = votetype;
    }

    public String getVotable() {
        return votable;
    }

    public void setVotable(String votable) {
        this.votable = votable;
    }

    public ArrayList<String> getVoteoptions() {
        return voteoptions;
    }

    public void setVoteoptions(ArrayList<String> voteoptions) {
        this.voteoptions = voteoptions;
    }

    public ArrayList<String> getOptionCount() {
        return OptionCount;
    }

    public void setOptionCount(ArrayList<String> optionCount) {
        OptionCount = optionCount;
    }

    public String getAnswerflag() {
        return answerflag;
    }

    public void setAnswerflag(String answerflag) {
        this.answerflag = answerflag;
    }

}