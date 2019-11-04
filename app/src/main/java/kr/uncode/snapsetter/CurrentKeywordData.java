package kr.uncode.snapsetter;

import io.realm.RealmObject;

public class CurrentKeywordData extends RealmObject {
    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
