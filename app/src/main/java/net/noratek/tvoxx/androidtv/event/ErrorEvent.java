package net.noratek.tvoxx.androidtv.event;

/**
 * Created by eloudsa on 06/09/16.
 */
public class ErrorEvent {

    private String errorMessage;


    public ErrorEvent(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ErrorEvent() {
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
