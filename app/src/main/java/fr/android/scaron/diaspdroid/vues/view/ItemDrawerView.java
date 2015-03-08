package fr.android.scaron.diaspdroid.vues.view;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.model.ItemDrawer;

/**
 * Created by Sébastien on 06/03/2015.
 */
@EViewGroup(R.layout.item_drawer)
public class ItemDrawerView extends LinearLayout {

    @ViewById(R.id.itemNameView)
    TextView itemNameView;

    public ItemDrawerView(Context context) {
        super(context);
    }

    public void bind(ItemDrawer itemDrawer) {
        itemNameView.setText(itemDrawer.itemName);
    }
}
