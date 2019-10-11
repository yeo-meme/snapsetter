package kr.uncode.firebaselog;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class PicActivity extends AppCompatActivity {

    private String name ="";
    private String image_url="";
    private Realm mRealm;


    public String userEmail ="";
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

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
                PictureData picD = new PictureData();
////                                //관리 객체를 직접만들떄 사용
//////                                PictureData mPic = realm.createObject(PictureData.class);
                                picD.setName(userEmail);
                                picD.setImage_url(image_url);
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