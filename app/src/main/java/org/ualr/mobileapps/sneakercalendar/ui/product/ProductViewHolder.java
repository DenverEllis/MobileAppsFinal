package org.ualr.mobileapps.sneakercalendar.ui.product;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.ualr.mobileapps.sneakercalendar.R;

public class ProductViewHolder extends RecyclerView.ViewHolder {

    private final ImageView mItemPreview;
    private final TextView mItemName;
    private final TextView mItemReleaseDate;
    private final ConstraintLayout container;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        this.mItemPreview = itemView.findViewById(R.id.item_preview);
        this.mItemName = itemView.findViewById(R.id.item_name);
        this.mItemReleaseDate = itemView.findViewById(R.id.item_release_date);
        this.container = itemView.findViewById(R.id.item_container);
    }

    public ImageView getItemPreview() {
        return mItemPreview;
    }

    public ConstraintLayout getContainer() {
        return container;
    }

    public TextView getItemName() {
        return mItemName;
    }

    public TextView getItemReleaseDate() {
        return mItemReleaseDate;
    }

}
