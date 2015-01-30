package fr.android.scaron.diaspdroid.model;

/**
 * Created by Maison on 12/01/2015.
 */
public class Data {
//    "author_name": "micka5973",
    String author_name;
//    "provider_url": "http://www.youtube.com/",
    String provider_url;
//    "provider_name": "YouTube",
    String provider_name;
//    "thumbnail_height": 360,
    Integer thumbnail_height;
//    "height": 315,
    Integer height;
//    "author_url": "http://www.youtube.com/user/micka5973",
    String author_url;
//    "version": "1.0",
    String version;
//    "width": 420,
    Integer width;
//    "html": "\u003Ciframe width=\"420\" height=\"315\" src=\"https://www.youtube.com/embed/2aRakmBRukQ?feature=oembed\" frameborder=\"0\" allowfullscreen\u003E\u003C/iframe\u003E",
    String html;
//    "title": "hexagone renaud",
    String title;
//    "type": "video",
    String type;
//    "thumbnail_url": "https://i.ytimg.com/vi/2aRakmBRukQ/hqdefault.jpg",
    String thumbnail_url;
//    "thumbnail_width": 480,
    Integer thumbnail_width;
//    "trusted_endpoint_url": "http://www.youtube.com/oembed?scheme=https"
    String trusted_endpoint_url;

    //Ajout de l'adresse de la vidéo pour une lecture en tache asynchrone
    String videoUrl;
    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }


    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getProvider_url() {
        return provider_url;
    }

    public void setProvider_url(String provider_url) {
        this.provider_url = provider_url;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public Integer getThumbnail_height() {
        return thumbnail_height;
    }

    public void setThumbnail_height(Integer thumbnail_height) {
        this.thumbnail_height = thumbnail_height;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getAuthor_url() {
        return author_url;
    }

    public void setAuthor_url(String author_url) {
        this.author_url = author_url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public Integer getThumbnail_width() {
        return thumbnail_width;
    }

    public void setThumbnail_width(Integer thumbnail_width) {
        this.thumbnail_width = thumbnail_width;
    }

    public String getTrusted_endpoint_url() {
        return trusted_endpoint_url;
    }

    public void setTrusted_endpoint_url(String trusted_endpoint_url) {
        this.trusted_endpoint_url = trusted_endpoint_url;
    }
}
