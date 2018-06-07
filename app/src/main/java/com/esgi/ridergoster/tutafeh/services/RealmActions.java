package com.esgi.ridergoster.tutafeh.services;

import android.content.Context;
import android.util.Log;

import com.esgi.ridergoster.tutafeh.models.Occurencies;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import io.realm.Realm;

public class RealmActions {

    static public boolean checkOccurencies() {
        Occurencies occurency = Realm.getDefaultInstance().where(Occurencies.class).findFirst();

        return occurency != null;
    }
    static public void initCSV(Context context) {
        if (!checkOccurencies()) {
            Log.d("REALM", "CREATE DATABASE FROM CSV");
            importCSV(context);
        } else {
            Log.d("REALM", "DATABASE ALREADY EXIST");
            Occurencies occurencies = Realm.getDefaultInstance().where(Occurencies.class).findFirst();
            Log.d("OCCURENCIES", occurencies.toString());
        }
    }

    private static void importCSV(final Context context) {
        final List<Occurencies> occurencies = new ArrayList<>();
        try {
            InputStream stream = context.getAssets().open("occurencies.csv");

            // BeanListProcessor converts each parsed row to an instance of a given class, then stores each instance into a list.
            BeanListProcessor<Occurencies> rowProcessor = new BeanListProcessor<Occurencies>(Occurencies.class);

            CsvParserSettings parserSettings = new CsvParserSettings();
            parserSettings.setProcessor(rowProcessor);
            parserSettings.setHeaderExtractionEnabled(true);
            parserSettings.getFormat().setDelimiter(';');
            CsvParser parser = new CsvParser(parserSettings);

            parser.beginParsing(stream);
            String[] row;
            while ((row = parser.parseNext()) != null) {
                Occurencies occurency = new Occurencies();
                occurency.setWords(row[0]);
                occurency.setResult(row[1]);
                occurency.setOccurencies(Integer.parseInt(row[2]));
                occurency.setLanguage(row[3]);
                occurency.setCreatedat(row[4]);
                occurency.setUpdatedat(row[5]);

                occurencies.add(occurency);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(occurencies);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("Realm", "SUCCESS");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d("Realm", "ERROR");
                Log.d("Realm", error.getMessage());
                Log.d("Realm", error.getStackTrace().toString());

            }
        });
    }
}
