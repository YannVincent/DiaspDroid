package fr.android.scaron.diaspdroid.model;

import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

/**
 * Created by Sébastien on 19/02/2015.
 */
public class Pod implements Comparable<Pod>{

    /*
    "id": "114",
String domain": "podmob.net",
String status": "up",
String secure": "true",
String score": "20",
String userrating": "0",
String adminrating": "0",
String city": null,
String state": "",
String country": "DE",
String lat": "51.8",
String long": "9.7",
String ip": "78.46.173.153",
String ipv6": "no",
String hgitdate": "2014-10-30 21:30:03 +0100",
String hgitref": "5cabd6924bc77a1cad06a139459c592b239b9e39",
String pingdomurl": "http:\/\/stats.pingdom.com\/7cu63v1lou3y\/492821",
String pingdomlast": null,
String monthsmonitored": "37",
String uptimelast7": "99.78",
String responsetimelast7": "1,084 ms",
String hruntime": "0.295354",
String hencoding": null,
String datecreated": "2012-02-25 01:32:11.515394",
String dateupdated": "2015-02-19 13:11:12",
String datelaststats": "2015-02-19 13:11:12",
String hidden": "no"
    */
    String id;
    String domain;
    String status;
    String secure;
    String score;
    String userrating;
    String adminrating;
    String city;
    String state;
    String country;
    String lat;
    @SerializedName("long")
    String longvalue;
    String ip;
    String ipv6;
    String hgitdate;
    String hgitref;
    String pingdomurl;
    String pingdomlast;
    String monthsmonitored;
    String uptimelast7;
    String responsetimelast7;
    String hruntime;
    String hencoding;
    String datecreated;
    String dateupdated;
    String datelaststats;
    String hidden;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(final boolean selected) {
        this.selected = selected;
    }

    boolean selected;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(final String domain) {
        this.domain = domain;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getSecure() {
        return secure;
    }

    public void setSecure(final String secure) {
        this.secure = secure;
    }

    public String getScore() {
        return score;
    }

    public void setScore(final String score) {
        this.score = score;
    }

    public String getUserrating() {
        return userrating;
    }

    public void setUserrating(final String userrating) {
        this.userrating = userrating;
    }

    public String getAdminrating() {
        return adminrating;
    }

    public void setAdminrating(final String adminrating) {
        this.adminrating = adminrating;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(final String lat) {
        this.lat = lat;
    }

    public String getLongvalue() {
        return longvalue;
    }

    public void setLongvalue(final String longvalue) {
        this.longvalue = longvalue;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(final String ip) {
        this.ip = ip;
    }

    public String getIpv6() {
        return ipv6;
    }

    public void setIpv6(final String ipv6) {
        this.ipv6 = ipv6;
    }

    public String getHgitdate() {
        return hgitdate;
    }

    public void setHgitdate(final String hgitdate) {
        this.hgitdate = hgitdate;
    }

    public String getHgitref() {
        return hgitref;
    }

    public void setHgitref(final String hgitref) {
        this.hgitref = hgitref;
    }

    public String getPingdomurl() {
        return pingdomurl;
    }

    public void setPingdomurl(final String pingdomurl) {
        this.pingdomurl = pingdomurl;
    }

    public String getPingdomlast() {
        return pingdomlast;
    }

    public void setPingdomlast(final String pingdomlast) {
        this.pingdomlast = pingdomlast;
    }

    public String getMonthsmonitored() {
        return monthsmonitored;
    }

    public void setMonthsmonitored(final String monthsmonitored) {
        this.monthsmonitored = monthsmonitored;
    }

    public String getUptimelast7() {
        return uptimelast7;
    }

    public void setUptimelast7(final String uptimelast7) {
        this.uptimelast7 = uptimelast7;
    }

    public String getResponsetimelast7() {
        return responsetimelast7;
    }

    public void setResponsetimelast7(final String responsetimelast7) {
        this.responsetimelast7 = responsetimelast7;
    }

    public String getHruntime() {
        return hruntime;
    }

    public void setHruntime(final String hruntime) {
        this.hruntime = hruntime;
    }

    public String getHencoding() {
        return hencoding;
    }

    public void setHencoding(final String hencoding) {
        this.hencoding = hencoding;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(final String datecreated) {
        this.datecreated = datecreated;
    }

    public String getDateupdated() {
        return dateupdated;
    }

    public void setDateupdated(final String dateupdated) {
        this.dateupdated = dateupdated;
    }

    public String getDatelaststats() {
        return datelaststats;
    }

    public void setDatelaststats(final String datelaststats) {
        this.datelaststats = datelaststats;
    }

    public String getHidden() {
        return hidden;
    }

    public void setHidden(final String hidden) {
        this.hidden = hidden;
    }

//    @Override
//    public int compare(final Pod lhs, final Pod rhs) {
//        return lhs.getDomain().toLowerCase().compareTo(rhs.getDomain().toLowerCase());
//    }

    @Override
    public int compareTo(final Pod another) {
        return getDomain().toLowerCase().compareTo(another.getDomain().toLowerCase());
    }
}
