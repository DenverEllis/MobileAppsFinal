package org.ualr.mobileapps.sneakercalendar.ui.account_view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.ualr.mobileapps.sneakercalendar.R;
import org.ualr.mobileapps.sneakercalendar.models.Product;
import org.ualr.mobileapps.sneakercalendar.services.FavoritesRepo;
import org.ualr.mobileapps.sneakercalendar.ui.product.FirebaseAdapterFactory;
import org.ualr.mobileapps.sneakercalendar.ui.product_view.ProductViewModel;

public class AccountViewFragment extends Fragment {

    private FirebaseAuth mAuth;
    private TextView textView;
    private RecyclerView mFavoritesList;
    private FavoritesRepo mFavoritesRepo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mFavoritesRepo = new FavoritesRepo(FirebaseFirestore.getInstance(),
                FirebaseAuth.getInstance());

        View root = inflater.inflate(R.layout.fragment_account_view, container, false);
        textView = root.findViewById(R.id.display_name);
        mFavoritesList = root.findViewById(R.id.favorites_list);

        ProductViewModel mViewModel = new ViewModelProvider(getParentFragment())
                .get(ProductViewModel.class);

        Toolbar toolbar = root.findViewById(R.id.account_toolbar);

        if (getActivity() != null) {
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        }

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(20)
                .build();

        FirestorePagingOptions<Product> options = new FirestorePagingOptions.Builder<Product>()
                .setLifecycleOwner(this)
                .setQuery(mFavoritesRepo.getFavorites(), config, Product.class)
                .build();

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);

        mDividerItemDecoration.setDrawable(ContextCompat.getDrawable(
                getContext(),
                R.drawable.item_divider));
        mFavoritesList.addItemDecoration(mDividerItemDecoration);

        mFavoritesList.setAdapter(FirebaseAdapterFactory.create(
                options,
                getContext(),
                mViewModel::selectProduct));
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            textView.setText(user.getEmail());
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.activity_account_view, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sign_out) {
            mAuth.signOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}