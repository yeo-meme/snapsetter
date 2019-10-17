package kr.uncode.firebaselog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

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

public class DrawerDetailActivity extends AppCompatActivity {

    private RealmResults<PictureData> imageList;


    public DrawerListAdapter drawerListAdapter;
    public RealmChangeListener realmChangeListener;
    public static String KEY_IMAGE_URL = "YEOMEME";
    public static String KEY_IMAGE_POSITION = "PIE";
    private ViewDataBinding binding;
    private Toolbar toolbar;
    private ImageView detailsDrawer;
    private String image_position = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_drawerdetail);

        detailsDrawer = findViewById(R.id.drawerDetail_img);
        toolbar = findViewById(R.id.toolbar);


        Intent intent = getIntent();
        String image_url = intent.getStringExtra(KEY_IMAGE_URL);
        String image_position = intent.getStringExtra(KEY_IMAGE_POSITION);
        Log.d("check_image",image_url);
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

                drawerListAdapter.listDelete();

                break;

        }
        return true;
    }

    private void toolbarSet() {


    }


    public void setDetail_img(String url) {
        if (url != null) {
            Glide.with(getApplicationContext()).load(url).into(detailsDrawer);
        } else {
            Log.d("image error","error");
        }
    }
}
