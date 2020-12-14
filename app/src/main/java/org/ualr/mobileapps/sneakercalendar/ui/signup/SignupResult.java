package org.ualr.mobileapps.sneakercalendar.ui.signup;

public class SignupResult {
    private int errorMessage;
    private boolean successful;

    public SignupResult(int errorMessage, boolean successful) {
        this.errorMessage = errorMessage;
        this.successful = successful;
    }

    public int getErrorMessage() {
        return errorMessage;
    }

    public boolean isSuccessful() {
        return successful;
    }
}
