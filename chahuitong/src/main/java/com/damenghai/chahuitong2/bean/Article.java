package com.damenghai.chahuitong2.bean;

/**
 * Created by Sgun on 15/8/17.
 */
public class Article {
    private String article_comment_flag;
    private int article_verify_time;
    private int article_click;
    private Image article_image;
    private String article_title;
    private int article_end_time;
    private int article_publisher_id;
    private String article_modify_time;
    private String article_abstract;
    private String article_id;
    private String article_publish_time;

    private String article_content;

    public Image getImage() {
        return article_image;
    }

    public void setImage(Image article_image) {
        this.article_image = article_image;
    }

    public String getTitle() {
        return article_title;
    }

    public void setTitle(String article_title) {
        this.article_title = article_title;
    }

    public String getAbstract() {
        return article_abstract;
    }

    public void setAbstract(String article_abstract) {
        this.article_abstract = article_abstract;
    }

    public String getId() {
        return article_id;
    }

    public void setId(String article_id) {
        this.article_id = article_id;
    }

    public String getTime() {
        return article_publish_time;
    }

    public void setTime(String article_publish_time) {
        this.article_publish_time = article_publish_time;
    }

    public String getArticle_comment_flag() {
        return article_comment_flag;
    }

    public void setArticle_comment_flag(String article_comment_flag) {
        this.article_comment_flag = article_comment_flag;
    }

    public int getArticle_verify_time() {
        return article_verify_time;
    }

    public void setArticle_verify_time(int article_verify_time) {
        this.article_verify_time = article_verify_time;
    }

    public int getArticle_click() {
        return article_click;
    }

    public void setArticle_click(int article_click) {
        this.article_click = article_click;
    }

    public int getArticle_end_time() {
        return article_end_time;
    }

    public void setArticle_end_time(int article_end_time) {
        this.article_end_time = article_end_time;
    }

    public int getArticle_publisher_id() {
        return article_publisher_id;
    }

    public void setArticle_publisher_id(int article_publisher_id) {
        this.article_publisher_id = article_publisher_id;
    }

    public String getArticle_modify_time() {
        return article_modify_time;
    }

    public void setArticle_modify_time(String article_modify_time) {
        this.article_modify_time = article_modify_time;
    }

    public String getArticle_content() {
        return article_content;
    }

    public void setArticle_content(String article_content) {
        this.article_content = article_content;
    }

    public class Image {
        private String path;
        private String height;
        private String width;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

    }
}
