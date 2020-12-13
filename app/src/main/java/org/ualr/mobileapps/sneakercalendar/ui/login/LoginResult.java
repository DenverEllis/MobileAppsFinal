package org.ualr.mobileapps.sneakercalendar.ui.login;


public class LoginResult {
    private int errorMessage;
    private boolean successful;

    public LoginResult(int errorMessage, boolean successful) {
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
