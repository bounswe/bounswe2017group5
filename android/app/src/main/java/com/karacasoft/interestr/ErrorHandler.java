package com.karacasoft.interestr;

/**
 * An interface to handle errors that happen in the application
 *
 * Created by karacasoft on 28.11.2017.
 */

public interface ErrorHandler {
    public void onError(Throwable t);
    public void onError(String errorMessage);
}
