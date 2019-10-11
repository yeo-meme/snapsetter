package kr.uncode.firebaselog;


import android.widget.RelativeLayout;

import io.realm.RealmObject;

// 각 아이템을 위한 데이터 클래스
public class PictureData extends RealmObject {

    private String name;
    private String image_url;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
