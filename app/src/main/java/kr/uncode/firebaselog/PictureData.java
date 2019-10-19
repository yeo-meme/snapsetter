package kr.uncode.firebaselog;



import io.realm.RealmObject;

// 각 아이템을 위한 데이터 클래스
public class PictureData extends RealmObject {

    private String name;
    private String image_url;


    /**내보관함 데이터와 연관된 하트
     * 1 = 색깔이 채워진 하트 = 내보관함에 찜한 사진
     * 0 = 빈 하트 = 내 보괌함에 찜하지 않은사진
     */


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getImage_url() { return image_url; }


    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
