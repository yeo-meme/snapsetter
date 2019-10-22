package kr.uncode.snapsetter.Drawer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.realm.Realm;
import io.realm.RealmResults;
import kr.uncode.snapsetter.MainActivity;
import kr.uncode.snapsetter.PictureData;
import kr.uncode.snapsetter.R;

public class DrawerFragment  extends Fragment {

    private DrawerListAdapter drawerListAdapter;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private RecyclerView recyclerView;

    private MainActivity mainActivity;
    private String userEmail = "";
    private String image_url = "";
    private String user_Id ="";
    private String getImage = "";
    private String recentUser = "";

    private RealmResults<PictureData> pictureDataList;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdater;

    public static DrawerFragment newInstance() {
        return new DrawerFragment();
    }

    private TextView drawer_word;

    @Override
    public void onResume() {
        super.onResume();
        Realm realm = Realm.getDefaultInstance();
        pictureDataList  = realm.where(PictureData.class).findAll();
        mAdater= new DrawerListAdapter(realm.where(PictureData.class).findAll());
        recyclerView.setAdapter(mAdater);

        Log.d("dd","내보관함 사진뿌리기 "+pictureDataList.toString());
        if (pictureDataList.size() == 0 ) {
            drawer_word.setVisibility(View.VISIBLE);
            Log.d("gg","kk");
        } else if (pictureDataList.size() != 0) {
            drawer_word.setVisibility(View.GONE);
            Log.d("gg","ll");


        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //부모를 부르거야 아하하하하하하 액티비티 커스텀 타입변환
        Activity activity = getActivity();
        if (activity != null && activity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) activity;
        }
    }


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
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        recentUser = currentUser.getEmail();
        Log.d("uu",recentUser);
        if (recentUser == null) {
            Toast.makeText(getActivity().getApplicationContext(),"로그인후 이용이 가능합니다.",Toast.LENGTH_LONG).show();
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_drawer, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView_drawer);
        drawer_word = rootView.findViewById(R.id.drawer_word);
        setAdapter();
        return rootView;
    }

    // 드로어 내보관함에 사진을 입력하기 위해
    //리엘엠에 데이터를 빼오고 레이아웃 매니저 및 어답터를
    //연결하는메서드
    private void setAdapter() {

        int numberOfColumns = 2;

        gridLayoutManager = new GridLayoutManager(getActivity(), numberOfColumns);
        recyclerView.setHasFixedSize(true);

//        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(gridLayoutManager);

//        pictureDataList  = realm.where(PictureData.class).findAll();
//        Log.d("2222",pictureDataList.toString());
//        mAdater= new DrawerListAdapter(realm.where(PictureData.class).findAll());
//        recyclerView.setAdapter(mAdater);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Realm realm = Realm.getDefaultInstance();
        realm.close();
    }
}