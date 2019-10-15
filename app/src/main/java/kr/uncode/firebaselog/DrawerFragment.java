package kr.uncode.firebaselog;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import kr.uncode.firebaselog.databinding.DrawerItemImageBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrawerFragment  extends Fragment {

    private DrawerListAdapter drawerListAdapter;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private RecyclerView drawer_rc;

    private MainActivity mainActivity;
    private final List<RetrofitResponse.Documents> data = new ArrayList<>();
    private String userEmail = "";
    private String image_url = "";
    private String user_Id ="";
    private String getImage = "";
    private String recentUser = "";

    private RealmResults<PictureData> pictureDataList;
    private ArrayList<String> picGet = new ArrayList<>();
    private GridLayoutManager gridLayoutManager;
    //-----------------------------------------------------더미데이타
    private ArrayList<DermyData> dermyDataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DrawerListAdapter mAdater;
    private ArrayList<DermyData> mDataset;
    private Object ViewGroup;


    public static DrawerFragment newInstance() {
        return new DrawerFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBookmark();
    }


    // 리얼엠을 통해 사용자 아이로 검색된 url 가지고 오기
    private void getBookmark() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        recentUser= currentUser.getEmail();
        Log.d("fff", recentUser);

        final  Realm realm = Realm.getDefaultInstance();

        RealmResults<PictureData> pic = realm.where(PictureData.class).equalTo("name",recentUser)
                .findAll();

        pictureDataList = pic;
        Log.d("33", String.valueOf(pic));
        if (pic.size() != 0) {
//            getImage = pic.get(0).getImage_url();

            Log.d("aa", String.valueOf(pic));




            if (pic != null) {
                drawerListAdapter = new DrawerListAdapter();
//                drawerListAdapter.setPic(pic);
                drawerListAdapter.addDataAll(pic);


            }
        }
    }

//    private void prepareData() {
//        dermyDataList.add(new DermyData("서울시청","호호"));
//        dermyDataList.add(new DermyData("경복궁","호호"));
//        dermyDataList.add(new DermyData("서울역","호호"));
//        dermyDataList.add(new DermyData("남산","호호"));
//        dermyDataList.add(new DermyData("을지로입구역","호호"));
//
//
//
//    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

    @Override
    public void onStart() {
        super.onStart();

        //사용자가 아이디가 없으면 로그인후 네비게이션 드로어 사용가능 메세지 알림
        recentUser = currentUser.getEmail();
        Log.d("uu",recentUser);
        if (recentUser == null) {
            Toast.makeText(getActivity().getApplicationContext(),"로그인후 이용이 가능합니다.",Toast.LENGTH_LONG).show();
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        int numberOfColumns = 2;

        gridLayoutManager = new GridLayoutManager(getActivity(), numberOfColumns);
        View rootView = inflater.inflate(R.layout.fragment_drawer, container, false);

        drawer_rc = rootView.findViewById(R.id.drawer_rc);
        drawer_rc.setHasFixedSize(true);

        // 원래 더미 데이터 어레이 리스트가 생성자에도 들어가고 변수에도 들어가던 자리
        drawerListAdapter = new DrawerListAdapter();


        drawer_rc.setLayoutManager(gridLayoutManager);
        drawer_rc.setAdapter(drawerListAdapter);
//        drawerListAdapter.addDataAll(data);

//        setDetail_img(rootView);
        return rootView;
    }


    public void setDetail_img(View view) {
//        final Realm realm = Realm.getDefaultInstance();
//
//        mAuth = FirebaseAuth.getInstance();
//        currentUser = mAuth.getCurrentUser();



//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                Log.d("zzzzzzzzzzzzzzz", "zzzzzzzzzzzzzzzzzz");
//                RealmResults<PictureData> pic = realm.where(PictureData.class).equalTo("name", "동구")
//                        .findAll();
//
//                if (pic.size() != 0) {
//                    Log.d("11", "데이타있음");
//                } else {
//                    Log.d("11", "데이터없음");
//
//                }
//            }
//
//        });
//
    }
}