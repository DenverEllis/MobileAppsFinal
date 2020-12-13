package org.ualr.mobileapps.sneakercalendar.ui.product_view;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.ualr.mobileapps.sneakercalendar.models.Product;

public class ProductViewModel extends ViewModel {
    private MutableLiveData<Product> mSelectedProduct;

    public ProductViewModel() {
        mSelectedProduct = new MutableLiveData<>();
    }

    public LiveData<Product> getSelectedProduct() {
        return mSelectedProduct;
    }

    public void selectProduct(Product product) {
        mSelectedProduct.setValue(product);
    }
}