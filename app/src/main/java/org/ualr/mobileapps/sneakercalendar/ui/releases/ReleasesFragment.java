package org.ualr.mobileapps.sneakercalendar.ui.releases;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private FirebaseFirestore mFirestore;
    private ProductRepo productRepo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFirestore = FirebaseFirestore.getInstance();
        productRepo = new ProductRepo(mFirestore);


        View root = inflater.inflate(R.layout.fragment_releases, container, false);
        RecyclerView productList = root.findViewById(R.id.product_list_view);

        ProductViewModel mViewModel = new ViewModelProvider(getParentFragment())
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

        return root;
    }
}