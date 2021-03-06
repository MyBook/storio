package com.pushtorefresh.storio3.sample.many_to_many_sample.resolvers;

import android.database.Cursor;
import androidx.annotation.NonNull;

import com.pushtorefresh.storio3.sample.many_to_many_sample.entities.Car;
import com.pushtorefresh.storio3.sample.many_to_many_sample.entities.CarStorIOSQLiteGetResolver;
import com.pushtorefresh.storio3.sample.many_to_many_sample.entities.CarTable;
import com.pushtorefresh.storio3.sample.many_to_many_sample.entities.Person;
import com.pushtorefresh.storio3.sample.many_to_many_sample.entities.PersonCarRelationTable;
import com.pushtorefresh.storio3.sample.many_to_many_sample.entities.PersonStorIOSQLiteGetResolver;
import com.pushtorefresh.storio3.sqlite.StorIOSQLite;
import com.pushtorefresh.storio3.sqlite.queries.RawQuery;

import java.util.List;

public class PersonRelationsGetResolver extends PersonStorIOSQLiteGetResolver {

    @NonNull
    private final CarStorIOSQLiteGetResolver carStorIOSQLiteGetResolver;

    public PersonRelationsGetResolver(@NonNull CarStorIOSQLiteGetResolver carStorIOSQLiteGetResolver) {
        this.carStorIOSQLiteGetResolver = carStorIOSQLiteGetResolver;
    }

    @Override
    @NonNull
    public Person mapFromCursor(@NonNull StorIOSQLite storIOSQLite, @NonNull Cursor cursor) {
        final Person person = super.mapFromCursor(storIOSQLite, cursor);

        final List<Car> cars = storIOSQLite
                .get()
                .listOfObjects(Car.class)
                .withQuery(RawQuery.builder()
                        .query("SELECT "
                                + CarTable.NAME + ".*"
                                + " FROM " + CarTable.NAME
                                + " JOIN " + PersonCarRelationTable.TABLE
                                + " ON " + CarTable.ID_COLUMN + " = " + PersonCarRelationTable.COLUMN_CAR_ID
                                + " AND " + PersonCarRelationTable.COLUMN_PERSON_ID + " = ?")
                        .args(person.id())
                        .build())
                .withGetResolver(carStorIOSQLiteGetResolver)
                .prepare()
                .executeAsBlocking();

        return new Person(person.id(), person.name(), cars);
    }
}