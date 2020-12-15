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

import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.ualr.mobileapps.sneakercalendar.R;
import org.ualr.mobileapps.sneakercalendar.models.Product;
import org.ualr.mobileapps.sneakercalendar.services.FavoritesRepo;

import java.util.List;

import static org.ualr.mobileapps.sneakercalendar.Helper.formatDate;

public class ProductFragment extends Fragment {

    private static final String SHARE_MESSAGE = "Did you know %s is being released on %s?";
    private FirebaseAuth mAuth;
    private ProductViewModel mViewModel;
    private FavoritesRepo mFavoritesRepo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mFavoritesRepo = new FavoritesRepo(FirebaseFirestore.getInstance(),
                FirebaseAuth.getInstance());
        mViewModel = new ViewModelProvider(getActivity())
                .get(ProductViewModel.class);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View root = getView();

        Toolbar toolbar = root.findViewById(R.id.product_toolbar);

        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar()
                    .setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> mViewModel.selectProduct(null));

        Product product = mViewModel.getSelectedProduct().getValue();
        updateUi(product);
    }

    private void updateUi(Product product) {
        View root = getView();

        final TextView mProductName = root.findViewById(R.id.product_name);
        final ImageView mProductImage = root.findViewById(R.id.product_image);
        final TextView mProductReleaseDate = root.findViewById(R.id.product_release_date);
        final TextView mProductPrice = root.findViewById(R.id.product_price);
        final TextView mProductDescription = root.findViewById(R.id.product_description);
        final Chip mAddToCalendarChip = root.findViewById(R.id.calendar_add_Chip);
        final Chip shareChip = root.findViewById(R.id.share_chip);

        if (mAuth.getCurrentUser() == null) {
            setFavoriteMode(root, product);
        } else {
            mFavoritesRepo.findFavoriteByName(product.getName())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> documents = task.getResult()
                                    .getDocuments();

                            if (documents.size() > 0) {
                                setRemoveFavoriteMode(root, documents.get(0).getId());
                            } else {
                                setFavoriteMode(root, product);
                            }
                        }
                    });
        }

        mAddToCalendarChip.setOnClickListener(view -> addToCalendar(product));
        shareChip.setOnClickListener(view -> shareProduct(product));

        Picasso.with(getContext()).load(product.getImageUrl()).into(mProductImage);
        mProductName.setText(product.getName());
        mProductReleaseDate.setText(formatDate(product.getReleaseDate()));
        mProductDescription.setText(product.getDescription());
        mProductPrice.setText(String.format("$%.2f", product.getPrice()));
    }

    private void setRemoveFavoriteMode(View view, String favoriteId) {
        final Chip favoriteChip = view.findViewById(R.id.add_favorite_chip);

        favoriteChip.setText(R.string.remove_favorite_action);
        favoriteChip.setOnClickListener(v -> removeFavorite(favoriteId));
    }

    private void setFavoriteMode(View view, Product product) {
        final Chip favoriteChip = view.findViewById(R.id.add_favorite_chip);

        favoriteChip.setText(R.string.favorite_action);
        favoriteChip.setOnClickListener(v ->
                favoriteProduct(product));
    }

    private void addToCalendar(@NonNull Product product) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, String
                        .format("%s Release", product.getName()))
                .putExtra(CalendarContract.Events.DESCRIPTION, product.getDescription())
                .putExtra(CalendarContract.Events.ALL_DAY, true)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, product.getReleaseDate()
                        .getTime());

        startActivity(intent);
    }

    private void favoriteProduct(@NonNull Product product) {
        final Chip favoriteChip = getView().findViewById(R.id.add_favorite_chip);

        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(getContext(), R.string.sign_in_warning, Toast.LENGTH_LONG).show();
            return;
        }

        mFavoritesRepo.addFavorite(mAuth.getCurrentUser().getUid(), product)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        favoriteChip.setText(R.string.remove_favorite_action);
                        favoriteChip.setOnClickListener(view ->
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
        final Chip favoriteChip = getView().findViewById(R.id.add_favorite_chip);

        mFavoritesRepo.removeFavorite(favoriteId).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Product product = mViewModel.getSelectedProduct().getValue();
                favoriteChip.setText(R.string.favorite_action);
                favoriteChip.setOnClickListener(view -> favoriteProduct(product));
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