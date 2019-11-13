package kr.uncode.snapsetter.Detail_View;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import kr.uncode.snapsetter.R;


public class ViewTwoStepActivity extends AppCompatActivity {

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
;
        replaceFragment(TwoStepFragment.newInstance());
        Log.d("details", "ViewTwoStepAcivity + onCreate");



    }






    public void replaceFragment(Fragment fragment) {
        Log.d("details", "ViewTwoStepAcivity + replaceMent");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment).commit();
    }



}

