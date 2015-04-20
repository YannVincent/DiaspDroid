package fr.android.scaron.diaspdroid.model;

import java.util.Date;

/**
 * Created by Sébastien on 06/04/2015.
 */
public class LikeResult {

    /**
     * {
     id=772524.0,
     guid=803a4620be8401320b2d2a0000053625,
     author={
     id=7738.0,
     guid=1f9bd9b030fc013219722a0000053625,
     name=SebastienCaron,
     diaspora_id=tilucifer@framasphere.org,
     avatar={
     small=https: //framasphere.org/uploads/images/thumb_small_93779d6fd285331e91dd.jpg,
     medium=https: //framasphere.org/uploads/images/thumb_medium_93779d6fd285331e91dd.jpg,
     large=https: //framasphere.org/uploads/images/thumb_large_93779d6fd285331e91dd.jpg
     }
     },
     created_at=2015-04-06T12: 15: 13Z
     }
     */

    Integer id;
    String guid;
    People author;
    Date created_at;

}
