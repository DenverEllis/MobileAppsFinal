package org.ualr.mobileapps.sneakercalendar.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.ualr.mobileapps.sneakercalendar.R;
import org.ualr.mobileapps.sneakercalendar.ui.account_view.AccountViewFragment;
import org.ualr.mobileapps.sneakercalendar.ui.login.LoginFragment;
import org.ualr.mobileapps.sneakercalendar.ui.signup.SignupFragment;

public class AccountFragment extends Fragment {

    private FirebaseAuth mAuth;
    private AccountViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        mAuth.addAuthStateListener(auth -> mViewModel.setIsSignedIn(auth.getCurrentUser() != null));
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel.isSigningUp().observe(getViewLifecycleOwner(), isSigningUp -> updateUi());
        mViewModel.isSignedIn().observe(getViewLifecycleOwner(), user -> updateUi());
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void updateUi() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.account_view_fragment, AccountViewFragment.class, null)
                    .commit();
        } else if (mViewModel.isSigningUp().getValue()) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.account_view_fragment, SignupFragment.class, null)
                    .commit();
        } else {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.account_view_fragment, LoginFragment.class, null)
                    .commit();
        }
    }

}