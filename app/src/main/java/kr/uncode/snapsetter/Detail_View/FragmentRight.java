package kr.uncode.snapsetter.Detail_View;

import android.content.Intent;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.realm.Realm;
import kr.uncode.snapsetter.Drawer.DrawerListAdapter;
import kr.uncode.snapsetter.PictureData;
import kr.uncode.snapsetter.R;
import kr.uncode.snapsetter.SearchListAdapter;

public class FragmentRight extends Fragment {
    private GestureDetector gestureDetector;
    public static final String RIGHT_KEY_IMAGE_URL = "RIGHT_KEY_IMAGE_URL";

    private String image_url;
    private RecyclerView recyclerView;
    private ImageView right_img;
    private SearchListAdapter searchListAdapter;
   public static FragmentRight newInstance() {
       return new FragmentRight();
   }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_right,container,false);
//       right_img = view.findViewById(R.id.right_img);
        recyclerView = view.findViewById(R.id.recyclerView);


       Realm realm = Realm.getDefaultInstance();
//
//        Intent intent = getActivity().getIntent();
//        String image_url = intent.getStringExtra(SearchListAdapter.RIGHT_KEY_IMAGE_URL);
//        String value = intent.getStringExtra(SearchListAdapter.keywordQuery);
//        Log.d("chec", "Two Step keyword check : " + value);
//        Log.d("check_image", image_url);
//        setDetail_img(image_url);
        return view;
    }

    public void setDetail_img(String url) {

//        Activity activity = getActivity();
//        if (activity != null && activity instanceof MainActivity) {
//            MainActivity mainActivity = (MainActivity) activity;
        if (url != null) {
            Glide.with(getActivity()).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(right_img);
        } else {
            Log.d("image error", "error");
        }
//        }
    }

}
