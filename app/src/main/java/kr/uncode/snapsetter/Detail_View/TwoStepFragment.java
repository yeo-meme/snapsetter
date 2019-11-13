package kr.uncode.snapsetter.Detail_View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import kr.uncode.snapsetter.R;
import kr.uncode.snapsetter.SearchListAdapter;

public class TwoStepFragment extends Fragment {

    private GestureDetector gestureDetector;

    private ImageView detail_img;
    private Context context;

    public static TwoStepFragment newInstance() {
        return new TwoStepFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.twostep_fragment, container, false);
        detail_img = view.findViewById(R.id.detail_img);



        Intent intent = getActivity().getIntent();
        String image_url = intent.getStringExtra(SearchListAdapter.EXTRA_KEY_IMAGE_URL);
        String value = intent.getStringExtra(SearchListAdapter.keywordQuery);
        Log.d("chec", "Two Step keyword check : " + value);
        Log.d("check_image", image_url);

        // 포지션을 받앗다치면
//        int position = 5;
//        RetrofitResponse.Documents data = MemeApplication.tempList.get(position);
        Log.d("details", "TwoStepFragment + onCreateView");
        setDetail_img(image_url);

        if (detail_img != null){

        }

//        final GestureDetector gesture = new GestureDetector(getActivity(),
//                new GestureDetector.SimpleOnGestureListener() {
//
//                    @Override
//                    public boolean onDown(MotionEvent e) {
//                        return true;
//                    }
//
//                    @Override
//                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//                                           float velocityY) {
//                        Log.i("hi", "onFling has been called!");
//                        final int SWIPE_MIN_DISTANCE = 120;
//                        final int SWIPE_MAX_OFF_PATH = 250;
//                        final int SWIPE_THRESHOLD_VELOCITY = 200;
//                        try {
//                            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
//                                return false;
//                            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
//                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                                Log.i("hi", "Right to Left");
////                                searchListViewHolder.rightPlusImage(view);
//                                replaceFragment(FragmentRight.newInstance());
//
//                            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
//                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                                Log.i("hi", "Left to Right");
//
//                                replaceFragment(FragmentLeft.newInstance());
//
//                            }
//                        } catch (Exception e) {
//                            // nothing
//                        }
//                        return super.onFling(e1, e2, velocityX, velocityY);
//                    }
//                });


//        detail_img.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return gesture.onTouchEvent(event);
//            }
//        });

//        detail_img.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
////                call(motionEvent);
//                Log.d("hh", "hii");
//                return gestureDetector.onTouchEvent(motionEvent);
//            }
//        });
        return view;
    }






//        public void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.setCustomAnimations(R.anim.anim1, R.anim.anim2, R.anim.anim3, R.anim.anim4);
//        fragmentTransaction.replace(R.id.container, fragment);
//        fragmentTransaction.commit();
//    }

//    public void call(MotionEvent motionEvent) {
//        Activity activity = getActivity();
//        if (activity != null) {
//            ViewTwoStepActivity viewTwoStepActivity = (ViewTwoStepActivity) activity;
//            viewTwoStepActivity.onTouchEvent(motionEvent);
//            Log.d("hi","hiiii");
//        }

//    }



    public void setDetail_img(String url) {
        Log.d("details", "TwoStepFragment + setDetail_img");
        if (url != null) {
            Glide.with(getActivity()).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(detail_img);
        } else {
            Log.d("image error", "error");
        }
    }
}
