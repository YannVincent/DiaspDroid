package fr.android.scaron.diaspdroid.model;

/**
 * Created by Sébastien on 18/01/2015.
 */
public class UploadResult {

//    {
//        "success": true,
    Boolean success;

    public String getError() {
        return error;
    }

    public void setError(final String error) {
        this.error = error;
    }

    String error;
//            "data": {
//        "photo": {
//            "author_id": 7738,
//                    "comments_count": null,
//                    "created_at": "2015-01-16T16:01:18Z",
//                    "diaspora_handle": "tilucifer@framasphere.org",
//                    "guid": "d4ba17f07fc60132ce422a0000053625",
//                    "height": 512,
//                    "id": 77686,
//                    "pending": true,
//                    "processed_image": {
//                "url": null,
//                        "thumb_small": {
//                    "url": null
//                },
//                "thumb_medium": {
//                    "url": null
//                },
//                "thumb_large": {
//                    "url": null
//                },
//                "scaled_full": {
//                    "url": null
//                }
//            },
//            "public": false,
//                    "random_string": "ed80cafa10172c4230d1",
//                    "remote_photo_name": "ed80cafa10172c4230d1.png",
//                    "remote_photo_path": "https://framasphere.org/uploads/images/",
//                    "status_message_guid": null,
//                    "text": null,
//                    "tmp_old_id": null,
//                    "unprocessed_image": {
//                "url": "/uploads/images/ed80cafa10172c4230d1.png",
//                        "thumb_small": {
//                    "url": "/uploads/images/thumb_small_ed80cafa10172c4230d1.png"
//                },
//                "thumb_medium": {
//                    "url": "/uploads/images/thumb_medium_ed80cafa10172c4230d1.png"
//                },
//                "thumb_large": {
//                    "url": "/uploads/images/thumb_large_ed80cafa10172c4230d1.png"
//                },
//                "scaled_full": {
//                    "url": "/uploads/images/scaled_full_ed80cafa10172c4230d1.png"
//                }
//            },
//            "updated_at": "2015-01-16T16:01:18Z",
//                    "width": 512
//        }
//    }
//    }
    UploadData data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public UploadData getData() {
        return data;
    }

    public void setData(UploadData data) {
        this.data = data;
    }
}
