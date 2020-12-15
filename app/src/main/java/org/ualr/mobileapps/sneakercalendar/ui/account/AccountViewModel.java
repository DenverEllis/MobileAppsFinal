package org.ualr.mobileapps.sneakercalendar.ui.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccountViewModel extends ViewModel {

    private MutableLiveData<Boolean> mIsSigningUp;
    private MutableLiveData<Boolean> mIsSignedIn;

    public AccountViewModel() {
        mIsSigningUp = new MutableLiveData<>(false);
        mIsSignedIn = new MutableLiveData<>(false);
    }

    public LiveData<Boolean> isSigningUp() {
        return mIsSigningUp;
    }

    public LiveData<Boolean> isSignedIn() {
        return mIsSignedIn;
    }

    public void setIsSignedIn(Boolean isSignedIn) {
        mIsSignedIn.setValue(isSignedIn);
    }

    public void setIsSigningUp(Boolean isSigningUp) {
        mIsSigningUp.setValue(isSigningUp);
    }
}