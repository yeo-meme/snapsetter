package kr.uncode.snapsetter.Detail_View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import kr.uncode.snapsetter.R;
import kr.uncode.snapsetter.SearchListAdapter;


public class ViewTwoStepActivity extends AppCompatActivity {
    private ImageView detail_img;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twostep);

        detail_img = findViewById(R.id.detail_img);

        //데이터수신
        Intent intent = getIntent();
        String image_url = intent.getStringExtra(SearchListAdapter.EXTRA_KEY_IMAGE_URL);
        String value = intent.getStringExtra(SearchListAdapter.keywordQuery);
        Log.d("chec", "Two Step keyword check : " + value);
        Log.d("check_image", image_url);
        setDetail_img(image_url);
//        Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_LONG).show();


    }


    public void setDetail_img(String url) {
        if (url != null) {
            Glide.with(getApplicationContext()).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(detail_img);
        } else {
            Log.d("image error", "error");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

