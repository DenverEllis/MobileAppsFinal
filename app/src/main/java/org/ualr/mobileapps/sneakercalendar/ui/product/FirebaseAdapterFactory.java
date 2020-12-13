package org.ualr.mobileapps.sneakercalendar.ui.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.squareup.picasso.Picasso;

import org.ualr.mobileapps.sneakercalendar.R;
import org.ualr.mobileapps.sneakercalendar.models.Product;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class FirebaseAdapterFactory {

    public static FirestorePagingAdapter<Product, ProductViewHolder> create(
            FirestorePagingOptions<Product> options,
            Context context,
            OnProductClickListener listener) {

        return new FirestorePagingAdapter<Product, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(
                    @NonNull ProductViewHolder holder,
                    int position,
                    @NonNull Product model) {

                SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy", Locale.US);

                holder.getItemName().setText(model.getName());
                Picasso.with(context).load(model.getImageUrl()).into(holder.getItemPreview());
                holder.getItemReleaseDate().setText(format.format(model.getReleaseDate()));

                holder.getContainer().setOnClickListener(view -> listener.onProductClick(model));
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_product, parent, false);

                return new ProductViewHolder(view);
            }

        };
    }

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }
}
