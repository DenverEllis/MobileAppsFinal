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

    private ProductViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(getActivity())
                .get(ProductViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel.getSelectedProduct().observe(getViewLifecycleOwner(), this::updateUi);
        updateUi(mViewModel.getSelectedProduct().getValue());
    }

    private void updateUi(Product product) {
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if (product != null) {
            transaction.replace(R.id.home_fragment_holder, ProductFragment.class, null)
                    .commit();
        } else {
            transaction.replace(R.id.home_fragment_holder, ReleasesFragment.class, null)
                    .commit();
        }
    }


}