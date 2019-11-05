package kr.uncode.snapsetter;

import io.realm.RealmObject;

public class CurrentKeywordData extends RealmObject {
    private String query;

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
