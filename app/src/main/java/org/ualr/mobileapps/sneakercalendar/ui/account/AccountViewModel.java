package org.ualr.mobileapps.sneakercalendar.ui.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

public class AccountViewModel extends ViewModel {

    private MutableLiveData<FirebaseUser> mUser;

    public AccountViewModel() {
        mUser = new MutableLiveData<>();
    }

    public LiveData<FirebaseUser> getUser() {
        return mUser;
    }
}