package com.esgi.ridergoster.tutafeh.models;

import java.util.Comparator;
import java.util.Date;

import io.realm.RealmObject;

public class Occurencies extends RealmObject implements Comparator<Occurencies> {
    private String words;
    private String result;
    private String language;
    private int occurencies;
    private String createdat;
    private String updatedat;

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getOccurencies() {
        return occurencies;
    }

    public void setOccurencies(int occurencies) {
        this.occurencies = occurencies;
    }

    public String getCreatedat() {
        return createdat;
    }

    public void setCreatedat(String createdat) {
        this.createdat = createdat;
    }

    public String getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(String updatedat) {
        this.updatedat = updatedat;
    }

    public Occurencies() {
    }

    @Override
    public String toString() {
        return "Occurencies{" +
                "words='" + words + '\'' +
                ", result='" + result + '\'' +
                ", language='" + language + '\'' +
                ", occurencies=" + occurencies +
                ", createdat='" + createdat + '\'' +
                ", updatedat='" + updatedat + '\'' +
                '}';
    }

    @Override
    public int compare(Occurencies o1, Occurencies o2) {
        if (o1.occurencies > o2.occurencies) {
            return 1;
        } else {
            return 0;
        }
    }
}
