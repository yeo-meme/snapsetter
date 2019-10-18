package kr.uncode.firebaselog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.bumptech.glide.Glide;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;


/**
 * 내보관함을 열면 내보관함의 리스트를 클릭했을때 화면 (가장 마지막 디테일의 자세한 화면 삭제가 가능)
 */
public class DrawerDetailActivity extends AppCompatActivity {


    private static final String TAG = DrawerDetailActivity.class.getSimpleName();

    private RealmResults<PictureData> imageList;


    public DrawerListAdapter drawerListAdapter;
    /**클릭했을때 이미지확대로 이미지를 보낼때
     * 인텐트를 사용하기 때문에 인텐트 키로사용하는 변수
     * 보내진 변수를 받기위해서 변수 선언
     */
    public static String KEY_IMAGE_URL = "YEOMEME";
    private ViewDataBinding binding;
    private Toolbar toolbar;
    private ImageView detailsDrawer;
    private String image_position = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_drawerdetail);

        Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_LONG).show();
        detailsDrawer = findViewById(R.id.drawerDetail_img);
        toolbar = findViewById(R.id.toolbar);


        Intent intent = getIntent();
        String image_url = intent.getStringExtra(KEY_IMAGE_URL);
        Log.d("22222",image_url);
        setDetail_img(image_url);

        //툴바셋팅
        setToolbar();

    }

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

        switch (item.getItemId()) {
            case R.id.menu_search:
                Log.d("one","checkcheck");
                break;

        }
        return true;
    }



    public void setDetail_img(String url) {
        if (url != null) {
            Glide.with(getApplicationContext()).load(url).into(detailsDrawer);
        } else {
            Log.d("image error","error");
        }
    }
}
