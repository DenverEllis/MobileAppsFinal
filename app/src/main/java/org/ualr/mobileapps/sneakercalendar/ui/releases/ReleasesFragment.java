package org.ualr.mobileapps.sneakercalendar.ui.releases;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import org.ualr.mobileapps.sneakercalendar.R;
import org.ualr.mobileapps.sneakercalendar.models.Product;
import org.ualr.mobileapps.sneakercalendar.services.ProductRepo;
import org.ualr.mobileapps.sneakercalendar.ui.product.FirebaseAdapterFactory;
import org.ualr.mobileapps.sneakercalendar.ui.product_view.ProductViewModel;

public class ReleasesFragment extends Fragment {

    private ProductRepo productRepo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        productRepo = new ProductRepo(FirebaseFirestore.getInstance());
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_releases, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView productList = view.findViewById(R.id.product_list_view);

        ProductViewModel mViewModel = new ViewModelProvider(getActivity())
                .get(ProductViewModel.class);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(20)
                .build();

        FirestorePagingOptions<Product> options = new FirestorePagingOptions.Builder<Product>()
                .setLifecycleOwner(this)
                .setQuery(productRepo.getNewReleases(), config, Product.class)
                .build();

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);

        mDividerItemDecoration.setDrawable(ContextCompat.getDrawable(
                getContext(),
                R.drawable.item_divider));
        productList.addItemDecoration(mDividerItemDecoration);

        productList.setAdapter(FirebaseAdapterFactory.create(
                options,
                getContext(),
                mViewModel::selectProduct));
    }
}