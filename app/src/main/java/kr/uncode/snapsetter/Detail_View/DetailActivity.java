package kr.uncode.snapsetter.Detail_View;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import io.realm.Realm;
import kr.uncode.snapsetter.R;
import kr.uncode.snapsetter.RetrofitConfig;
import kr.uncode.snapsetter.RetrofitResponse;
import kr.uncode.snapsetter.SearchListAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_KEY_IMAGE_URL = "EXTRA_KEY_IMAGE_URL";


    private SearchListAdapter searchListAdapter;
    private PhotoView detail_img;

    private String img = "img";

    private PhotoViewAttacher mAttacher;
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Realm realm = Realm.getDefaultInstance();
        searchListAdapter = new SearchListAdapter(realm);


//        RetrofitConfig.getService().search(query, "", "", "")
//                .enqueue(new Callback<RetrofitResponse>() {
//
//                    @Override
//                    public void onResponse(Call<RetrofitResponse> call, Response<RetrofitResponse> response) {
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<RetrofitResponse> call, Throwable t) {
//
//                    }
//
//
//                });
    }
    public void setDetail_img(String url) {
        if (url != null) {
            Glide.with(getApplicationContext()).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(detail_img);
            mAttacher = new PhotoViewAttacher(detail_img);
            mAttacher.update();
        } else {
            Log.d("image error","error");
        }
    }
}
