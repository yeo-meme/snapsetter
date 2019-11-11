package kr.uncode.snapsetter.Detail_View;

import android.content.Intent;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import kr.uncode.snapsetter.R;
import kr.uncode.snapsetter.SearchListAdapter;
import kr.uncode.snapsetter.databinding.FragmentLeftBinding;

public class FragmentLeft extends Fragment {
    public static final String LEFT_KEY_IMAGE_URL = "LEFT_KEY_IMAGE_URL";
    private GestureDetector gestureDetector;
    private ImageView left_img;
    FragmentLeftBinding binding;
    private String image_url;

    public static FragmentLeft newInstance() {
        return new FragmentLeft();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_left,container,false);
        left_img = view.findViewById(R.id.left_img);
//
//


        Intent intent = getActivity().getIntent();
        String image_url = intent.getStringExtra(SearchListAdapter.LEFT_KEY_IMAGE_URL);
        String value = intent.getStringExtra(SearchListAdapter.keywordQuery);
        Log.d("chec", "Two Step keyword check : " + value);
        Log.d("check_image", image_url);
        setDetail_img(image_url);

        return view;
    }

    public void setDetail_img(String url) {

//        Activity activity = getActivity();
//        if (activity != null && activity instanceof MainActivity) {
//            MainActivity mainActivity = (MainActivity) activity;
        if (url != null) {
            Glide.with(getActivity()).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(left_img);
        } else {
            Log.d("image error", "error");
        }
//        }


    }



}
