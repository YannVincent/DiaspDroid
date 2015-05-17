package fr.android.scaron.diaspdroid.model;

import java.util.Date;

/**
 * Created by Sebastien on 03/05/2015.
 */
public class NotificationContent {
    /*
    {
"created_at": "2015-04-30T18:23:28Z",
"id": 307471,
"recipient_id": 2322,
"target_id": 627749,
"target_type": "Post",
"unread": false,
"updated_at": "2015-04-30T23:01:53Z",
"note_html": "<div class='notification_element read' data-guid='307471' data-type='also_commented'>\n<div class='pull-right unread-toggle'>\n<i class='entypo eye' title='Marquer comme non lu'></i>\n</div>\n<img alt=\"Globulle Cdw\" class=\"avatar\" data-person_id=\"7199\" src=\"https://framasphere.org/uploads/images/thumb_small_8c9069c2049385d5af81.JPG\" title=\"Globulle Cdw\" />\n<div class='notification_message'>\n<a data-hovercard='/people/a90ee7102fb90132196e2a0000053625' href='/people/a90ee7102fb90132196e2a0000053625' class='hovercardable hovercardable' >Globulle Cdw</a> a egalement commente sur <a href=\"/posts/627749\" class=\"hard_object_link\" data-ref=\"627749\">Il semblerait qu&#x27;...</a> de Globulle Cdw.\n<div><time class=\"timeago\" data-time-ago=\"2015-04-30T18:23:28Z\" datetime=\"2015-04-30T18:23:28Z\" title=\"2015-04-30T18:23:28Z\">30/04/2015</time></div>\n</div>\n</div>\n"
}
     */
    Date created_at;
    Integer id;
    Integer recipient_id;
    Integer target_id;
    String target_type;
    Boolean unread;
    Date updated_at;
    String note_html;

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(final Date created_at) {
        this.created_at = created_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getRecipient_id() {
        return recipient_id;
    }

    public void setRecipient_id(final Integer recipient_id) {
        this.recipient_id = recipient_id;
    }

    public Integer getTarget_id() {
        return target_id;
    }

    public void setTarget_id(final Integer target_id) {
        this.target_id = target_id;
    }

    public String getTarget_type() {
        return target_type;
    }

    public void setTarget_type(final String target_type) {
        this.target_type = target_type;
    }

    public Boolean isUnread() {
        return unread;
    }

    public void setUnread(final Boolean unread) {
        this.unread = unread;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(final Date updated_at) {
        this.updated_at = updated_at;
    }

    public String getNote_html() {
        return note_html;
    }

    public void setNote_html(final String note_html) {
        this.note_html = note_html;
    }
}
