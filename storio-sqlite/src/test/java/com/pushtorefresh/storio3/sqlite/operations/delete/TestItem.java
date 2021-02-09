package com.pushtorefresh.storio3.sqlite.operations.delete;

import androidx.annotation.NonNull;

class TestItem {

    static final String TABLE = "test_items";

    static final String NOTIFICATION_TAG = "test_tag";

    private TestItem() {

    }

    @NonNull
    static TestItem newInstance() {
        return new TestItem();
    }
}
