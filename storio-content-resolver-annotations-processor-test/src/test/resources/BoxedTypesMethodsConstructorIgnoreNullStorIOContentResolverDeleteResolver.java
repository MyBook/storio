package com.pushtorefresh.storio3.contentresolver.annotations;

import androidx.annotation.NonNull;
import com.pushtorefresh.storio3.contentresolver.operations.delete.DefaultDeleteResolver;
import com.pushtorefresh.storio3.contentresolver.queries.DeleteQuery;
import java.lang.Override;

/**
 * Generated resolver for Delete Operation.
 */
public class BoxedTypesMethodsConstructorIgnoreNullStorIOContentResolverDeleteResolver extends DefaultDeleteResolver<BoxedTypesMethodsConstructorIgnoreNull> {
    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public DeleteQuery mapToDeleteQuery(@NonNull BoxedTypesMethodsConstructorIgnoreNull object) {
        return DeleteQuery.builder()
            .uri("content://uri")
            .where("field4 = ?")
            .whereArgs(object.getField4())
            .build();
    }
}