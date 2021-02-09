package com.pushtorefresh.storio3.contentresolver.design;

import androidx.annotation.NonNull;

import com.pushtorefresh.storio3.contentresolver.StorIOContentResolver;

abstract class OperationDesignTest {

    @NonNull
    private final StorIOContentResolver storIOContentResolver = new DesignTestStorIOContentResolver();

    @NonNull
    protected StorIOContentResolver storIOContentResolver() {
        return storIOContentResolver;
    }
}
