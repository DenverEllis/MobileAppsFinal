package org.ualr.mobileapps.sneakercalendar.ui.product_view;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.ualr.mobileapps.sneakercalendar.R;
import org.ualr.mobileapps.sneakercalendar.models.Product;
import org.ualr.mobileapps.sneakercalendar.services.FavoritesRepo;

import java.util.List;

import static org.ualr.mobileapps.sneakercalendar.Helper.formatDate;

public class ProductFragment extends Fragment {

    private FirebaseAuth mAuth;
    private ProductViewModel mViewModel;
    private FavoritesRepo mFavoritesRepo;
    private static final String SHARE_MESSAGE = "Did you know %s is being released on %s?";
    private Chip mFavoriteChip;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mFavoritesRepo = new FavoritesRepo(FirebaseFirestore.getInstance(),
                FirebaseAuth.getInstance());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_product, container, false);
        mViewModel = new ViewModelProvider(getParentFragment())
                .get(ProductViewModel.class);

        Toolbar toolbar = root.findViewById(R.id.product_toolbar);

        if (getActivity() != null) {
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity)getActivity()).getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity)getActivity()).getSupportActionBar()
                    .setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(view -> mViewModel.selectProduct(null));

        mViewModel.getSelectedProduct().observe(getViewLifecycleOwner(), product -> {
            updateUi(root, product);
        });

        Product product = mViewModel.getSelectedProduct().getValue();
        updateUi(root, product);

        return root;
    }

    private void updateUi(View root, Product product) {
        TextView mProductName = root.findViewById(R.id.product_name);
        ImageView mProductImage = root.findViewById(R.id.product_image);
        TextView mProductReleaseDate = root.findViewById(R.id.product_release_date);
        TextView mProductPrice = root.findViewById(R.id.product_price);
        TextView mProductDescription = root.findViewById(R.id.product_description);

        Chip mAddToCalendarChip = root.findViewById(R.id.calendar_add_Chip);
        mFavoriteChip = root.findViewById(R.id.add_favorite_chip);
        Chip shareChip = root.findViewById(R.id.share_chip);


        if (product == null) {
            mProductDescription.setText("");
            mProductImage.setImageDrawable(null);
            mProductName.setText("");
            mProductPrice.setText("");
            mProductReleaseDate.setText("");
        } else {
            mFavoritesRepo.findFavoriteByName(product.getName())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();

                            if (documents.size() > 0) {
                                mFavoriteChip.setText(R.string.remove_favorite_action);
                                mFavoriteChip.setOnClickListener(view ->
                                        removeFavorite(documents.get(0).getId()));
                            } else {
                                mFavoriteChip.setText(R.string.favorite_action);
                                mFavoriteChip.setOnClickListener(view -> favoriteProduct(product));
                            }
                        }
                    });
            mAddToCalendarChip.setOnClickListener(view -> addToCalendar(product));
            shareChip.setOnClickListener(view -> shareProduct(product));

            Picasso.with(getContext()).load(product.getImageUrl()).into(mProductImage);
            mProductName.setText(product.getName());
            mProductReleaseDate.setText(formatDate(product.getReleaseDate()));
            mProductDescription.setText(product.getDescription());
            mProductPrice.setText(String.format("$%.2f", product.getPrice()));
        }
    }

    private void addToCalendar(@NonNull Product product) {
       Intent intent = new Intent(Intent.ACTION_INSERT)
               .setData(CalendarContract.Events.CONTENT_URI)
               .putExtra(CalendarContract.Events.TITLE, String
                       .format("%s Release", product.getName()))
               .putExtra(CalendarContract.Events.ALL_DAY, true)
               .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, product.getReleaseDate()
                       .getTime());

       startActivity(intent);
    }

    private void favoriteProduct(@NonNull Product product) {
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(getContext(), R.string.sign_in_warning, Toast.LENGTH_LONG).show();
            return;
        }

        mFavoritesRepo.addFavorite(mAuth.getCurrentUser().getUid(), product)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mFavoriteChip.setText(R.string.remove_favorite_action);
                        mFavoriteChip.setOnClickListener(view ->
                                removeFavorite(task.getResult().getId()));
                    } else {
                        Toast.makeText(getContext(), R.string.favorite_failed,
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(getContext(), R.string.favorite_failed, Toast.LENGTH_SHORT)
                            .show();
                });
    }

    private void removeFavorite(@NonNull String favoriteId) {
       mFavoritesRepo.removeFavorite(favoriteId).addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               Product product = mViewModel.getSelectedProduct().getValue();
               mFavoriteChip.setText(R.string.favorite_action);
               mFavoriteChip.setOnClickListener(view -> favoriteProduct(product));
           } else {
               Toast.makeText(getContext(), R.string.favorite_remove_failed, Toast.LENGTH_SHORT)
                       .show();
           }
       });
    }

    private void shareProduct(@NonNull Product product) {
        String message = String.format(SHARE_MESSAGE, product.getName(),
                formatDate(product.getReleaseDate()));

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }
}