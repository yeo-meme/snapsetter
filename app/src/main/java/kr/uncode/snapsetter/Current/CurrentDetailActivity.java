package kr.uncode.snapsetter.Current;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.realm.Realm;
import io.realm.RealmResults;
import kr.uncode.snapsetter.R;

public class CurrentDetailActivity extends AppCompatActivity {

    private static String CURRENT_IMAGE_URL = "JIN";
    private RealmResults<CurrentUserPicData> results;
    private Toolbar toolbar;
    private ImageView current_detail;
    private  String image_url;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        current_detail = findViewById(R.id.current_detail);
        Log.d("current","CurrentDetailActivity + onCreate");
        setToolbar();
        getImageUrlKey();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ddmenu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //딜리트 메뉴 클릭시
        switch (item.getItemId()) {
            /**사진삭제 툴바메뉴
             *
             */
            case R.id.menu_delete:
                deleteImg();
                Log.d("ww", "케이스 끝내고");
                break;
            case android.R.id.home: {
                finish();
                Log.d("home", "click");

                return true;
            }

        }
        Log.d("ww", "케이스 트루전");
        return super.onOptionsItemSelected(item);
    }

    //    툴바 셋팅 메서드
    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getImageUrlKey() {
        Intent intent = getIntent();
         image_url = intent.getStringExtra(CURRENT_IMAGE_URL);
//        positon = intent.getStringExtra(KEY_IMAGE_POSITION);

        Log.d("get",image_url);
        //불러온 키로 글라이드로 이미지 셋팅을 위해 메서드 호출
        setDetail_img(image_url);
    }

    public void setDetail_img(String url) {
        Log.d("current","CurrentDetailActivity +setDetailImage");

        if (url != null) {
            Glide.with(getApplicationContext()).load(url)
                    .override(200, 200)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(current_detail);

        } else {
            Log.d("image error", "error");
        }
    }
    private void deleteImg() {
        //리얼엠 인스턴스객체를 불러와성 이따 삭제할때 쓸꺼니깡
        Realm realm = Realm.getDefaultInstance();
//        //파이어베이스 인증 객체를 불러와성
//        mAuth = FirebaseAuth.getInstance();
//        //인증객체에서 최근 사용자를 불러와서 (접속해있는 최근 사용자)
//        currentUser = mAuth.getCurrentUser();
//        //최근 사용자를 스트링 객체로 담아서
//        String recentUser = currentUser.getEmail();
//        //최근 사용자 스트링으로 확인!!
//        Log.d("aa",recentUser);


        //리얼엠에 이미지 스트링은 이미 알고 있음 왜??
        //이 액티비티가 실행되기전 DrawerListAdapter 홀더에서 인텐드에 담아서 보냈음
        //그럼 나는 이미지삭제시 다시 불러와서 그거와 맞는 데이터를 찾아서 삭제할 생각이었는데
        //바보같은 생각이었음 : 왜냐 이미지 주소를 알고 있으니까 걍 삭제하면 되는 거였음

        //리얼엠에서 URL 삭제
        CurrentUserPicData aa = realm.where(CurrentUserPicData.class).equalTo("image_url",image_url )
                .findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (aa.isValid()) {
                    aa.deleteFromRealm();
                    results = realm.where(CurrentUserPicData.class).findAll();
                    CurrentListAdapter currentListAdapter = new CurrentListAdapter(results);
                    currentListAdapter.notifyDataSetChanged();
                    finish();
                    Log.d("aa", "삭제되었습니다");

                }
            }
        });

        Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_LONG).show();
        Log.d("ww", "딜리트끝나고");

    }

}
