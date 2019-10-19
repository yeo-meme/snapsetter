package kr.uncode.firebaselog;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class PicActivity extends AppCompatActivity {

    private String name ="";
    private String image_url="";
    private Realm mRealm;


    private String rr = "";
    private ArrayList<PictureData> pictureData = new ArrayList<>();


    public String userEmail ="";
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private int not_drawer = 0;
    private int in_drawer = 1;

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


    public  void savePic(String image_url) {
        final  Realm realm = Realm.getDefaultInstance();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            currentUser = mAuth.getCurrentUser();
            userEmail= currentUser.getEmail();
            Log.d("email",userEmail);

        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                Log.d("aaaaaaaaaaaaaaa",userEmail);
                Log.d("aaaaaaaaaaaaaaa",image_url);
                PictureData pictureData = realm.createObject(PictureData.class);

////                                //관리 객체를 직접만들떄 사용
//////                                PictureData mPic = realm.createObject(PictureData.class);
                pictureData.setName(userEmail);
                pictureData.setImage_url(image_url);
//                pictureData.setHeart_state(in_drawer);
//                                realm.commitTransaction();
            }
        });
//        getPic();

    }


    public void delete(String url) {
        Realm realm = Realm.getDefaultInstance();

        PictureData gg = realm.where(PictureData.class).equalTo("image_url",url).findFirst();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                    if (gg.isValid()) {
                        gg.deleteFromRealm();
                    }
            }
        });
    }

    public String  getPic() {
        final  Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.d("zzzzzzzzzzzzzzz",userEmail);
                RealmResults<PictureData> pic = realm.where(PictureData.class).equalTo("name",userEmail)
                        .findAll();


                if (pic.size() != 0) {

                    String image = pic.get(0).getImage_url();
                    Log.d("99",image);
                } else {
                    Log.d("11","데이터없음");

                }

            }
        });

        return rr;
    }
}
