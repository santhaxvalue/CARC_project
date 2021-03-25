package com.sd.carc;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Map;

/**
 * Created by ramsrini on 04/10/17.
 */

@IgnoreExtraProperties
public class User {

    public String name;
    public String email;
    public Map<String, String> timeStamp;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String name, String email, Map<String, String> timeStamp) {
        this.name = name;
        this.email = email;
        this.timeStamp = timeStamp;
    }
}