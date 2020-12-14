package org.ualr.mobileapps.sneakercalendar.ui.signup;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.ualr.mobileapps.sneakercalendar.R;

import java.util.concurrent.Executor;

public class SignupFragment extends Fragment{
    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    private SignupViewModel signupViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        signupViewModel = new ViewModelProvider(this).get(SignupViewModel.class);
        mAuth = FirebaseAuth.getInstance();

        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText usernameEditText = view.findViewById(R.id.username);
        final EditText passwordEditText = view.findViewById(R.id.password);
        final Button signupButton = view.findViewById(R.id.signup);
        final ProgressBar loadingProgressBar = view.findViewById(R.id.loading);

        signupViewModel.getSignupFormState().observe(getViewLifecycleOwner(), signupFormState -> {
            if (signupFormState == null) {
                return;
            }
            signupButton.setEnabled(signupFormState.isDataValid());
            if (signupFormState.getUsernameError() != null) {
                usernameEditText.setError(getString(signupFormState.getUsernameError()));
            }
            if (signupFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(signupFormState.getPasswordError()));
            }
        });

        signupViewModel.getSignupResult().observe(getViewLifecycleOwner(), signupResult -> {
            if (!signupResult.isSuccessful()) {
                showSignupFailed(signupResult.getErrorMessage());
            } else {
                if (mAuth.getCurrentUser() != null) {
                    updateUiWithUser(mAuth.getCurrentUser());
                    usernameEditText.setText("");
                    passwordEditText.setText("");
                }
            }
            loadingProgressBar.setVisibility(View.GONE);
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                signupViewModel.signupDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };

        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                signupViewModel.signup(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
            return false;
        });

        signupButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            signupViewModel.signup(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());
        });
    }
    private void updateUiWithUser(FirebaseUser user) {
        String welcome = getString(R.string.welcome) + user.getDisplayName();
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(getContext().getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        }
    }

    private void showSignupFailed(@StringRes Integer errorString) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    errorString,
                    Toast.LENGTH_LONG).show();

        }
    }
}
