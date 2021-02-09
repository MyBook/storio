package com.pushtorefresh.storio3.sqlite.design;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Test class that represents an object stored in Db
 */
class User {

    private final String email;
    private Long id;

    User(@Nullable Long id, @NonNull String email) {
        this.id = id;
        this.email = email;
    }

    @NonNull
    String email() {
        return email;
    }
}
