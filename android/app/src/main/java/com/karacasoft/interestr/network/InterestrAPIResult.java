package com.karacasoft.interestr.network;

/**
 * Created by karacasoft on 30.10.2017.
 */

public class InterestrAPIResult<T extends Object> {

    private T result;

    public InterestrAPIResult(T result) {
        this.result = result;
    }

    public T get() {
        return result;
    }

}
