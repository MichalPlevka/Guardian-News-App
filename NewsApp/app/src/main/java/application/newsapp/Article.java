package application.newsapp;

import java.util.Date;

public class Article {
    private String article_title;
    private String section_name;
    private String date;
    private String articleUrl;

    private boolean isClicked;
    private boolean isGettingClosed;

    public Article(String article_title, String section_name, String date, String articleUrl, boolean isClicked, boolean isGettingClosed) {
        this.article_title = article_title;
        this.section_name = section_name;
        this.date = date;
        this.articleUrl = articleUrl;
        this.isClicked = isClicked;
        this.isGettingClosed = isGettingClosed;
    }

    public String getArticle_title() {
        return article_title;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    public boolean isGettingClosed() {
        return isGettingClosed;
    }

    public void setGettingClosed(boolean gettingClosed) {
        isGettingClosed = gettingClosed;
    }
}
