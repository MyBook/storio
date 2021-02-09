package com.pushtorefresh.storio3.contentresolver.operations.delete;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class TestItem {

    @Nullable
    private final String data;

    private TestItem(@Nullable String data) {
        this.data = data;
    }

    @NonNull
    public static TestItem newInstance() {
        return new TestItem(null);
    }

    @NonNull
    public static TestItem newInstance(@Nullable String data) {
        return new TestItem(data);
    }

    @Override
    public String toString() {
        return "TestItem{" +
                "data='" + data + '\'' +
                '}';
    }
}
