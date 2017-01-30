package com.westwood24.connect.model;

/**
 * Created by dharmesh on 29/8/16.
 */
public class AllArticles {
    String articleId;
    String articleType;
    String articleVoteType;
    String articalDesc;
    String articleTitle;
    String articleImage;
    String articleVideo;
    String articalViewers;
    String articalComment;

    public String getArticleVideo() {
        return articleVideo;
    }

    public void setArticleVideo(String articleVideo) {
        this.articleVideo = articleVideo;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleType() {
        return articleType;
    }

    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }

    public String getArticleVoteType() {
        return articleVoteType;
    }

    public void setArticleVoteType(String articleVoteType) {
        this.articleVoteType = articleVoteType;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleImage() {
        return articleImage;
    }

    public void setArticleImage(String articleImage) {
        this.articleImage = articleImage;
    }


    public String getArticalViewers() {
        return articalViewers;
    }

    public void setArticalViewers(String articalViewers) {
        this.articalViewers = articalViewers;
    }

    public String getArticalComment() {
        return articalComment;
    }

    public void setArticalComment(String articalComment) {
        this.articalComment = articalComment;
    }

    public String getArticalDesc() {
        return articalDesc;
    }

    public void setArticalDesc(String articalDesc) {
        this.articalDesc = articalDesc;
    }
}
