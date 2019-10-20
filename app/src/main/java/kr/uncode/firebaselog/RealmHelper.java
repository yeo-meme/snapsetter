package kr.uncode.firebaselog;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.realm.Realm;

public class RealmHelper {

    public void RealmHelper() {
    }


    public void savePic(final String image_url) {
        final Realm realm = Realm.getDefaultInstance();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
             currentUser = auth.getCurrentUser();
            final String userEmail = currentUser.getEmail();
            Log.d("email", userEmail);
            //TODO 널처리해야함 사용자
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Log.d("aaa","save User Email  : "+userEmail);
                    Log.d("aaa","save User Image Url : " +  image_url);
                    PictureData pictureData = realm.createObject(PictureData.class);
                    pictureData.setName(userEmail);
                    pictureData.setImage_url(image_url);
                }
            });
        }
    }


    public void delete(final String url) {
        Realm realm = Realm.getDefaultInstance();
        PictureData gg = realm.where(PictureData.class).equalTo("image_url", url).findFirst();

        Log.d("ddd","딜리트호출오키");
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (gg.isValid()) {
                    Log.d("dd", "딜리트 이미지 잘들어오나" + gg);
                    gg.deleteFromRealm();
                }
            }
        });
    }
}
