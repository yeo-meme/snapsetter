package kr.uncode.firebaselog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.bumptech.glide.Glide;

public class DrawerDetailActivity extends AppCompatActivity {

    public static String KEY_IMAGE_URL = "YEOMEME";
    private ViewDataBinding binding;

    private ImageView detailsDrawer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_drawerdetail);

        detailsDrawer = findViewById(R.id.drawerDetail_img);
        Intent intent = getIntent();
        String image_url = intent.getStringExtra(KEY_IMAGE_URL);
        Log.d("check_image",image_url);
        setDetail_img(image_url);
    }


    public void setDetail_img(String url) {
        if (url != null) {
            Glide.with(getApplicationContext()).load(url).into(detailsDrawer);
        } else {
            Log.d("image error","error");
        }
    }
}
