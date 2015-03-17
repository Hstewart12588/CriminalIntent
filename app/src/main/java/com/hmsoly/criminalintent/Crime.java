package com.hmsoly.criminalintent;

import java.util.UUID;

/**
 * Created by Harlan on 3/15/15.
 */
public class Crime {
    private UUID mId;
    private String mTitle;

    public Crime(){
        //Generates unique identifier
        mId = UUID.randomUUID();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}
