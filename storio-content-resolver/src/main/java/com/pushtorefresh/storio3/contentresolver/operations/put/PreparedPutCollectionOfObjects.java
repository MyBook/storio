package com.pushtorefresh.storio3.contentresolver.operations.put;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pushtorefresh.storio3.Interceptor;
import com.pushtorefresh.storio3.StorIOException;
import com.pushtorefresh.storio3.contentresolver.ContentResolverTypeMapping;
import com.pushtorefresh.storio3.contentresolver.StorIOContentResolver;
import com.pushtorefresh.storio3.contentresolver.operations.internal.RxJavaUtils;
import com.pushtorefresh.storio3.operations.PreparedOperation;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Prepared Put Operation for collection of objects.
 *
 * @param <T> type of objects.
 */
public class PreparedPutCollectionOfObjects<T> extends PreparedPut<PutResults<T>, Collection<T>> {

    @NonNull
    private final Collection<T> objects;

    @Nullable
    private final PutResolver<T> explicitPutResolver;

    PreparedPutCollectionOfObjects(@NonNull StorIOContentResolver storIOContentResolver,
                                   @NonNull Collection<T> objects,
                                   @Nullable PutResolver<T> explicitPutResolver) {
        super(storIOContentResolver);
        this.objects = objects;
        this.explicitPutResolver = explicitPutResolver;
    }

    @NonNull
    @Override
    protected Interceptor getRealCallInterceptor() {
        return new RealCallInterceptor();
    }

    private class RealCallInterceptor implements Interceptor {
        @NonNull
        @Override
        public <Result, WrappedResult, Data> Result intercept(@NonNull PreparedOperation<Result, WrappedResult, Data> operation, @NonNull Chain chain) {
            try {
                final StorIOContentResolver.LowLevel lowLevel = storIOContentResolver.lowLevel();

                // Nullable
                final List<SimpleImmutableEntry<T, PutResolver<T>>> objectsAndPutResolvers;

                if (explicitPutResolver != null) {
                    objectsAndPutResolvers = null;
                } else {
                    objectsAndPutResolvers = new ArrayList<SimpleImmutableEntry<T, PutResolver<T>>>(objects.size());

                    for (final T object : objects) {
                        //noinspection unchecked
                        final ContentResolverTypeMapping<T> typeMapping
                                = (ContentResolverTypeMapping<T>) lowLevel.typeMapping(object.getClass());

                        if (typeMapping == null) {
                            throw new IllegalStateException("One of the objects from the collection does not have type mapping: " +
                                    "object = " + object + ", object.class = " + object.getClass() + "," +
                                    "ContentProvider was not affected by this operation, please add type mapping for this type");
                        }

                        objectsAndPutResolvers.add(new SimpleImmutableEntry<T, PutResolver<T>>(
                                object,
                                typeMapping.putResolver()
                        ));
                    }
                }

                final Map<T, PutResult> results = new HashMap<T, PutResult>(objects.size());

                if (explicitPutResolver != null) {
                    for (final T object : objects) {
                        final PutResult putResult = explicitPutResolver.performPut(storIOContentResolver, object);
                        results.put(object, putResult);
                    }
                } else {
                    for (final SimpleImmutableEntry<T, PutResolver<T>> objectAndPutResolver : objectsAndPutResolvers) {
                        final T object = objectAndPutResolver.getKey();
                        final PutResolver<T> putResolver = objectAndPutResolver.getValue();

                        final PutResult putResult = putResolver.performPut(storIOContentResolver, object);
                        results.put(object, putResult);
                    }
                }

                //noinspection unchecked
                return (Result) PutResults.newInstance(results);

            } catch (Exception exception) {
                throw new StorIOException("Error has occurred during Put operation. objects = " + objects, exception);
            }
        }
    }

    /**
     * Creates {@link Flowable} which will perform Put Operation and send result to observer.
     * <p>
     * Returned {@link Flowable} will be "Cold Flowable", which means that it performs
     * put only after subscribing to it. Also, it emits the result once.
     * <p>
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>Operates on {@link StorIOContentResolver#defaultRxScheduler()} if not {@code null}.</dd>
     * </dl>
     *
     * @return non-null {@link Flowable} which will perform Put Operation.
     * and send result to observer.
     */
    @NonNull
    @CheckResult
    @Override
    public Flowable<PutResults<T>> asRxFlowable(@NonNull BackpressureStrategy backpressureStrategy) {
        return RxJavaUtils.createFlowable(storIOContentResolver, this, backpressureStrategy);
    }

    /**
     * Creates {@link Single} which will perform Put Operation lazily when somebody subscribes to it and send result to observer.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>Operates on {@link StorIOContentResolver#defaultRxScheduler()} if not {@code null}.</dd>
     * </dl>
     *
     * @return non-null {@link Single} which will perform Put Operation.
     * And send result to observer.
     */
    @NonNull
    @CheckResult
    @Override
    public Single<PutResults<T>> asRxSingle() {
        return RxJavaUtils.createSingle(storIOContentResolver, this);
    }

    /**
     * Creates {@link Completable} which will perform Put Operation lazily when somebody subscribes to it.
     * <dl>
     * <dt><b>Scheduler:</b></dt>
     * <dd>Operates on {@link StorIOContentResolver#defaultRxScheduler()} if not {@code null}.</dd>
     * </dl>
     *
     * @return non-null {@link Completable} which will perform Put Operation.
     */
    @NonNull
    @CheckResult
    @Override
    public Completable asRxCompletable() {
        return RxJavaUtils.createCompletable(storIOContentResolver, this);
    }

    @NonNull
    @Override
    public Collection<T> getData() {
        return objects;
    }

    /**
     * Builder for {@link PreparedPutCollectionOfObjects}.
     *
     * @param <T> type of objects to put.
     */
    public static class Builder<T> {

        @NonNull
        private final StorIOContentResolver storIOContentResolver;

        @NonNull
        private final Collection<T> objects;

        @Nullable
        private PutResolver<T> putResolver;

        public Builder(@NonNull StorIOContentResolver storIOContentResolver, @NonNull Collection<T> objects) {
            this.storIOContentResolver = storIOContentResolver;
            this.objects = objects;
        }

        /**
         * Optional: Specifies resolver for Put Operation
         * that should define behavior of Put Operation: insert or update
         * of the objects.
         * <p>
         * Can be set via {@link ContentResolverTypeMapping},
         * If value is not set via {@link ContentResolverTypeMapping}
         * or explicitly — exception will be thrown.
         *
         * @param putResolver nullable resolver for Put Operation.
         * @return builder.
         */
        @NonNull
        public Builder<T> withPutResolver(@NonNull PutResolver<T> putResolver) {
            this.putResolver = putResolver;
            return this;
        }

        /**
         * Builds new instance of {@link PreparedPutCollectionOfObjects}.
         *
         * @return new instance of {@link PreparedPutCollectionOfObjects}.
         */
        @NonNull
        public PreparedPutCollectionOfObjects<T> prepare() {
            return new PreparedPutCollectionOfObjects<T>(
                    storIOContentResolver,
                    objects,
                    putResolver
            );
        }
    }
}
