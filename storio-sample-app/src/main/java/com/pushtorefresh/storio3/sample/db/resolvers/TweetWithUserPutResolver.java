package com.pushtorefresh.storio3.sample.db.resolvers;

import androidx.annotation.NonNull;

import com.pushtorefresh.storio3.sample.db.entities.TweetWithUser;
import com.pushtorefresh.storio3.sample.db.tables.TweetsTable;
import com.pushtorefresh.storio3.sample.db.tables.UsersTable;
import com.pushtorefresh.storio3.sqlite.StorIOSQLite;
import com.pushtorefresh.storio3.sqlite.operations.put.PutResolver;
import com.pushtorefresh.storio3.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio3.sqlite.operations.put.PutResults;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

public class TweetWithUserPutResolver extends PutResolver<TweetWithUser> {

    @NonNull
    @Override
    public PutResult performPut(@NonNull StorIOSQLite storIOSQLite, @NonNull TweetWithUser tweetWithUser) {
        // We can even reuse StorIO methods
        final PutResults<Object> putResults = storIOSQLite
                .put()
                .objects(asList(tweetWithUser.tweet(), tweetWithUser.user()))
                .prepare() // BTW: it will use transaction!
                .executeAsBlocking();

        final Set<String> affectedTables = new HashSet<String>(2);

        affectedTables.add(TweetsTable.TABLE);
        affectedTables.add(UsersTable.TABLE);

        // Actually, it's not very clear what PutResult should we return here…
        // Because there is no table for this pair of tweet and user
        // So, let's just return Update Result
        return PutResult.newUpdateResult(putResults.numberOfUpdates(), affectedTables);
    }
}
