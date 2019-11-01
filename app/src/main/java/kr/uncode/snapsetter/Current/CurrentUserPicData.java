package kr.uncode.snapsetter.Current;

import io.realm.RealmObject;

public class CurrentUserPicData extends RealmObject {

    private String current_url;

    public String getCurrent_url() {
        return current_url;
    }

    public void setCurrent_url(String current_url) {
        this.current_url = current_url;
    }
}
