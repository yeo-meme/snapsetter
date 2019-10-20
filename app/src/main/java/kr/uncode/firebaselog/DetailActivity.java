package kr.uncode.firebaselog;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_KEY_IMAGE_URL = "EXTRA_KEY_IMAGE_URL";


    private ImageView detail_img;

    private String img = "img";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twostep);

        detail_img = findViewById(R.id.detail_img);

        //데이터수신
        Intent intent = getIntent();
        String image_url = intent.getStringExtra(EXTRA_KEY_IMAGE_URL);
        Log.d("check_image",image_url);
        setDetail_img(image_url);
//        Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_LONG).show();


    }



    public void setDetail_img(String url) {
        if (url != null) {
            Glide.with(getApplicationContext()).load(url).into(detail_img);
        } else {
            Log.d("image error","error");
        }
    }
}
