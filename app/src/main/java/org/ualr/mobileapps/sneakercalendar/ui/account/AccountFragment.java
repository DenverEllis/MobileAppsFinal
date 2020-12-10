package org.ualr.mobileapps.sneakercalendar.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.ualr.mobileapps.sneakercalendar.R;
import org.ualr.mobileapps.sneakercalendar.ui.account_view.AccountViewFragment;
import org.ualr.mobileapps.sneakercalendar.ui.login.LoginFragment;

public class AccountFragment extends Fragment {

    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        mAuth.addAuthStateListener(firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
           if (user != null) {
               getChildFragmentManager().beginTransaction()
                       .replace(R.id.account_view_fragment, AccountViewFragment.class, null)
                       .commit();
           } else {
               getChildFragmentManager().beginTransaction()
                       .replace(R.id.account_view_fragment, LoginFragment.class, null)
                       .commit();
           }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            getChildFragmentManager().beginTransaction()
                    .add(R.id.account_view_fragment, AccountViewFragment.class, null)
                    .commit();
        }
    }
}