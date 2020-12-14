package org.ualr.mobileapps.sneakercalendar.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import org.ualr.mobileapps.sneakercalendar.R;
import org.ualr.mobileapps.sneakercalendar.models.Product;
import org.ualr.mobileapps.sneakercalendar.ui.product_view.ProductFragment;
import org.ualr.mobileapps.sneakercalendar.ui.product_view.ProductViewModel;
import org.ualr.mobileapps.sneakercalendar.ui.releases.ReleasesFragment;

public class HomeFragment extends Fragment {

    private static final String RELEASES_TAG = "releases_fragment";
    private static final String PRODUCT_VIEW_TAG = "product_fragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ProductViewModel mViewModel = new ViewModelProvider(this)
                .get(ProductViewModel.class);

        mViewModel.getSelectedProduct().observe(getViewLifecycleOwner(), this::updateUi);
        getChildFragmentManager().beginTransaction()
                .add(R.id.home_fragment_holder, ReleasesFragment.class, null, RELEASES_TAG)
                .commit();

        return root;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    private void updateUi(Product product) {
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment productViewFragment = manager.findFragmentByTag(PRODUCT_VIEW_TAG);
        Fragment releaseFragment = manager.findFragmentByTag(RELEASES_TAG);

        if (releaseFragment == null) {
            return;
        }

        if (productViewFragment == null && product != null) {
            transaction.add(R.id.home_fragment_holder, ProductFragment.class, null, PRODUCT_VIEW_TAG);
            transaction.hide(releaseFragment);
        } else if (productViewFragment != null && product != null) {
            transaction.hide(releaseFragment);
            transaction.show(productViewFragment);
        } else if (productViewFragment != null) {
            transaction.hide(productViewFragment);
            transaction.show(releaseFragment);
        }

        transaction.commit();
    }


}