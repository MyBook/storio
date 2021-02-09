package com.pushtorefresh.storio3.contentresolver.operations.put;

import android.net.Uri;
import androidx.annotation.NonNull;

import static org.mockito.Mockito.mock;

class TestItem {
    static final Uri CONTENT_URI = mock(Uri.class);

    private TestItem() {

    }

    @NonNull
    static TestItem newInstance() {
        return new TestItem();
    }
}
