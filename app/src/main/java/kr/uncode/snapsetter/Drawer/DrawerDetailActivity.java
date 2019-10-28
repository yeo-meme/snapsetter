package kr.uncode.snapsetter.Drawer;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.realm.Realm;
import io.realm.RealmResults;
import kr.uncode.snapsetter.MainActivity;
import kr.uncode.snapsetter.MainFragment;
import kr.uncode.snapsetter.PictureData;
import kr.uncode.snapsetter.R;


/**
 * 내보관함을 열면 내보관함의 리스트를 클릭했을때 화면 (가장 마지막 디테일의 자세한 화면 삭제가 가능)
 */
public class DrawerDetailActivity extends AppCompatActivity {

    private static final String TAG = DrawerDetailActivity.class.getSimpleName();

    /**리얼엠에서 리스트객체로 데이터를 꺼내기위 리얼엠리절트 PictureData 타입의 리스ㅡㅌ
     *
     */
    private RealmResults<PictureData> imageList;


    public DrawerListAdapter drawerListAdapter;
    /**클릭했을때 이미지확대로 이미지를 보낼때
     * 인텐트를 사용하기 때문에 인텐트 키로사용하는 변수
     * 보내진 변수를 받기위해서 변수 선언
     */
    public static String KEY_IMAGE_URL = "YEOMEME";
    /**리스트 삭제를 위한 포지션 값을 리스트 어댑터에서 getIntent로 받아오기 위한 상수
     *
     */
    private static String KEY_IMAGE_POSITION = "PIE";

    /**드로어어댑터 홀더에서 저장한 클릭할 당시의 이미지 URL을 인텐트에 키와 함께
     * 담아서 보냈을때 그걸 getIntent 로 키를 이용해 해당 URL을 꺼내 String타입으로 저장한 변수
     */
    private String image_url;

    private String positon;
    private ViewDataBinding binding;
    private Toolbar toolbar;
    /**
     * 디테일 이미지를 표현하는 이미지뷰
     */
    private ImageView detailsDrawer;
    private RealmResults<PictureData> bb ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_drawerdetail);

        //중복 액티티비가 존재하여 실제동작하는 액티비티 확인차 토스트
//        Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_LONG).show();

        //딜리트 메뉴를 포함하는 툴바
        setToolbar();


        detailsDrawer = findViewById(R.id.drawerDetail_img);

        //드로어리스어탭터 홀더에서 보낸 인텐트 키를 받아오는 구문
        getImageUrlKey();

        //툴바셋팅

    }

    private void getImageUrlKey() {
        Intent intent = getIntent();
        image_url = intent.getStringExtra(KEY_IMAGE_URL);
        positon = intent.getStringExtra(KEY_IMAGE_POSITION);

        //불러온 키로 글라이드로 이미지 셋팅을 위해 메서드 호출
        setDetail_img(image_url);
    }



//    툴바 셋팅 메서드
    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ddmenu,menu);
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
                Log.d("ww","케이스 끝내고");
                break;
        }
        Log.d("ww","케이스 트루전");

        finish();

        return super.onOptionsItemSelected(item);
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
         PictureData aa = realm.where(PictureData.class).equalTo("image_url",image_url)
                .findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (aa.isValid()) {
                    aa.deleteFromRealm();
                    Log.d("aa","삭제되었습니다");

                }
            }
        });

        Toast.makeText(getApplicationContext(),"삭제되었습니다.",Toast.LENGTH_LONG).show();
        Log.d("ww","딜리트끝나고");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Realm realm = Realm.getDefaultInstance();
        realm.close();
    }

    public void setDetail_img(String url) {
        if (url != null) {
            Glide.with(getApplicationContext()).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(detailsDrawer);
        } else {
            Log.d("image error","error");
        }
    }
}
