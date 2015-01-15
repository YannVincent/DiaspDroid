package fr.android.scaron.diaspdroid.model;


import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FluxPost implements Serializable{

    private static String DATETIME_PATTERN = "d MMM yyyy HH:mm";
    // put in memory
    private Integer id;
    private String idStr;

    // put in flux_list_item_post_avatar
    private byte[] avatar;
    private String avatarPath;

    // put in flux_list_item_post_user
    private String username;
    private String userprofilePath;

    // put in flux_list_item_post_datetime
    private String datetimeStr;
    private Date datetime;

    // put in flux_list_item_post_detail
    // convert into webview like this
    /*
    details_www = (WebView) t411_detail.findViewById(R.id.prez);
    details_www.getSettings().setUseWideViewPort(true);
    details_www.getSettings().setLoadWithOverviewMode(true);
    details_www.getSettings().setJavaScriptEnabled(true);

    final String mimeType = "text/html";
    final String encoding = "utf-8";
    details_www.loadDataWithBaseURL(null, detail, mimeType, encoding, "");
     */
    private String detail;

    // put in flux_list_item_post_detail_picture
    private String detailPicturePath;

    // put in flux_list_item_like
    private Boolean liked;

    // action on flux_list_item_comment
    private ArrayList<FluxPost> comments;

    // put in flux_list_item_publish
    private Boolean published;

    public FluxPost(Integer id){
        this.id = id;
    }

    public FluxPost(Integer id, String username, String detail, Date datetime){
        this.id = id;
        this.username = username;
        this.detail = detail;
        setDatetime(datetime);
    }

    public FluxPost(Integer id, String username, String detail, String datetimeStr){
        this.id = id;
        this.username = username;
        this.detail = detail;
        setDatetimeStr(datetimeStr);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserprofilePath() {
        return userprofilePath;
    }

    public void setUserprofilePath(String userprofilePath) {
        this.userprofilePath = userprofilePath;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
        DateFormat dateFormat = new SimpleDateFormat(DATETIME_PATTERN);
        this.datetimeStr = dateFormat.format(datetime);
    }

    public String getDatetimeStr() {
        return datetimeStr;
    }

    public void setDatetimeStr(String datetimeStr) {
        this.datetimeStr = datetimeStr;
        DateFormat dateFormat = new SimpleDateFormat(DATETIME_PATTERN);
        try {
            this.datetime = dateFormat.parse(datetimeStr);
        } catch (ParseException e) {
            this.datetime = new Date();
            e.printStackTrace();
        }
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDetailPicturePath() {
        return detailPicturePath;
    }

    public void setDetailPicturePath(String detailPicturePath) {
        this.detailPicturePath = detailPicturePath;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public ArrayList<FluxPost> getComments() {
        return comments;
    }

    public void setComments(ArrayList<FluxPost> comments) {
        this.comments = comments;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }
}
