package com.pushtorefresh.storio3.contentresolver.design;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import androidx.annotation.NonNull;

import com.pushtorefresh.storio3.contentresolver.StorIOContentResolver;
import com.pushtorefresh.storio3.contentresolver.operations.delete.DefaultDeleteResolver;
import com.pushtorefresh.storio3.contentresolver.operations.delete.DeleteResolver;
import com.pushtorefresh.storio3.contentresolver.operations.get.DefaultGetResolver;
import com.pushtorefresh.storio3.contentresolver.operations.get.GetResolver;
import com.pushtorefresh.storio3.contentresolver.operations.put.DefaultPutResolver;
import com.pushtorefresh.storio3.contentresolver.operations.put.PutResolver;
import com.pushtorefresh.storio3.contentresolver.queries.DeleteQuery;
import com.pushtorefresh.storio3.contentresolver.queries.InsertQuery;
import com.pushtorefresh.storio3.contentresolver.queries.UpdateQuery;

import static org.mockito.Mockito.mock;

public class ArticleMeta {

    static final Uri CONTENT_URI = mock(Uri.class);

    static final PutResolver<Article> PUT_RESOLVER = new DefaultPutResolver<Article>() {
        @NonNull
        @Override
        protected InsertQuery mapToInsertQuery(@NonNull Article object) {
            return InsertQuery.builder()
                    .uri(CONTENT_URI)
                    .build();
        }

        @NonNull
        @Override
        protected UpdateQuery mapToUpdateQuery(@NonNull Article article) {
            return UpdateQuery.builder()
                    .uri(CONTENT_URI)
                    .where(BaseColumns._ID + " = ?")
                    .whereArgs(article.id())
                    .build();
        }

        @NonNull
        @Override
        protected ContentValues mapToContentValues(@NonNull Article object) {
            return mock(ContentValues.class);
        }
    };

    static final GetResolver<Article> GET_RESOLVER = new DefaultGetResolver<Article>() {
        @NonNull
        @Override
        public Article mapFromCursor(
                @NonNull StorIOContentResolver storIOContentResolver,
                @NonNull Cursor cursor
        ) {
            return Article.newInstance(null, null); // in Design tests it does not matter
        }
    };

    static final DeleteResolver<Article> DELETE_RESOLVER = new DefaultDeleteResolver<Article>() {
        @NonNull
        @Override
        protected DeleteQuery mapToDeleteQuery(@NonNull Article article) {
            return DeleteQuery.builder()
                    .uri(CONTENT_URI)
                    .where(BaseColumns._ID + " = ?")
                    .whereArgs(article.id())
                    .build();
        }
    };

    private ArticleMeta() {
        throw new IllegalStateException("No instances please");
    }
}
