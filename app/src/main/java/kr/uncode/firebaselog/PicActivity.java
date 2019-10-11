package kr.uncode.firebaselog;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class PicActivity extends AppCompatActivity {

    private String name ="";
    private String image_url="";
    private Realm mRealm;

//    public PicActivity(String name, String image_url, Realm realm) {
//        this.name = name;
//        this.image_url = image_url;
//        this.mRealm = realm;
//    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        init();
    }



    private  void  init() {

        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);
        mRealm.getDefaultInstance();

    }


    public  void savePic() {
        final  Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                Log.d("aaaaaaaaaaaaaaa","aaaaaaaaaaaaaaaaaaa");
                PictureData picD = new PictureData();
////                                //관리 객체를 직접만들떄 사용
//////                                PictureData mPic = realm.createObject(PictureData.class);
                                picD.setName("동구");
                                picD.setImage_url("dddddd");
//                                realm.commitTransaction();
            }
        });
    }


    public void  getPic() {
        final  Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.d("zzzzzzzzzzzzzzz","zzzzzzzzzzzzzzzzzz");
                RealmResults<PictureData> pic = realm.where(PictureData.class).equalTo("name","동구")
                .findAll();

                if (pic.size() != 0) {
                    Log.d("11","데이타있음");
                } else {
                    Log.d("11","데이터없음");

                }
            }
        });
    }
}
