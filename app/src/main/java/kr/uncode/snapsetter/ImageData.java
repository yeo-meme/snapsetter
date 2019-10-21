package kr.uncode.snapsetter;

import io.realm.RealmObject;

public class ImageData extends RealmObject {

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    private String image_url;


}
