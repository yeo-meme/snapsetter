package kr.uncode.snapsetter.Detail_View;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import kr.uncode.snapsetter.R;


public class ViewTwoStepActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private ImageView detail_img;
    private FrameLayout container;

    private Toolbar toolbar;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private GestureDetector gestureScanner;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twostep);
        gestureScanner = new GestureDetector(this);
//        detail_img = findViewById(R.id.detail_img);
        setToolbar();
        replaceFragment(TwoStepFragment.newInstance());
        //데이터수신
//        Intent intent = getIntent();
//        String image_url = intent.getStringExtra(SearchListAdapter.EXTRA_KEY_IMAGE_URL);
//        String value = intent.getStringExtra(SearchListAdapter.keywordQuery);
//        Log.d("chec", "Two Step keyword check : " + value);
//        Log.d("check_image", image_url);
//        setDetail_img(image_url);
//        Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_LONG).show();


    }
    @Override
    public boolean onTouchEvent(MotionEvent me) {
        Log.d("hi","hiiiiiiii");
        return gestureScanner.onTouchEvent(me);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //딜리트 메뉴 클릭시
        switch (item.getItemId()) {
            /**사진삭제 툴바메뉴
             *
             */
            case R.id.home:
                finish();
                return true;
        }
        Log.d("ww","케이스 트루전");
        return super.onOptionsItemSelected(item);
    }


    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anim1, R.anim.anim2, R.anim.anim3, R.anim.anim4);
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }


    public void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anim1, R.anim.anim2, R.anim.anim3, R.anim.anim4);
        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.commit();
    }

//    public void setDetail_img(String url) {
//        if (url != null) {
//            Glide.with(getApplicationContext()).load(url)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(detail_img);
//        } else {
//            Log.d("image error", "error");
//        }
//    }



    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;

            // right to left swipe
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                Toast.makeText(getApplicationContext(), "Left Swipe", Toast.LENGTH_SHORT).show();
//                replaceFragment(FragmentRight.newInstance());
            }
            // left to right swipe
            else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                Toast.makeText(getApplicationContext(), "Right Swipe", Toast.LENGTH_SHORT).show();
//                replaceFragment(FragmentLeft.newInstance());

            }
            // down to up swipe
            else if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                Toast.makeText(getApplicationContext(), "Swipe up", Toast.LENGTH_SHORT).show();
            }
            // up to down swipe
            else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                Toast.makeText(getApplicationContext(), "Swipe down", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {

        }
        return true;
    }



}

