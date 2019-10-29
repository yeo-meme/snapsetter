package kr.uncode.snapsetter.Drawer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import kr.uncode.snapsetter.MainActivity;
import kr.uncode.snapsetter.PictureData;
import kr.uncode.snapsetter.R;

public class DrawerFragment  extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    /**내보관함 리사이클러뷰 변수
     */
    private RecyclerView recyclerView;
    /**파이어베이스 최신사용자 를 스트링에 담는 변수
     */
    private String recentUser = "";

    /** 리얼엠 리절트 를 픽쳐데이타 타입 리스트로 보관하는 변수
     */
    private RealmResults<PictureData> pictureDataList;

    /**보관함에 리사이클러뷰 정렬을 위해 매니저 변수 사용
     */
    private GridLayoutManager gridLayoutManager;

    /**보관함에 리스트를 가져오기 위해 어댑터 셋하기 위한 변수
     */
    private RecyclerView.Adapter mAdater;

    public static DrawerFragment newInstance() {
        return new DrawerFragment();
    }

    /**데이터 == 0 일때 리사이클러뷰대신 안내 텍스트 보이게하는 텍스트
     */
    private TextView drawer_word;

    private Toolbar toolbar;

    private DrawerListAdapter drawerListAdapter;

    //리섬이 시작할때 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //다시 리얼엠 인스턴스를 불러오고
    //드로어 어댑터에 리얼엠 데이터를 모두 받아서 넘긴다
    //리사이클뷰에 어답터를 작용해서 셋

    @Override
    public void onResume() {
        super.onResume();
        Realm realm = Realm.getDefaultInstance();
        pictureDataList  = realm.where(PictureData.class).findAll();
        mAdater= new DrawerListAdapter(realm.where(PictureData.class).findAll());
        recyclerView.setAdapter(mAdater);

        //보관함에 저장된 데이터 로그 확인
        Log.d("dd","내보관함 사진뿌리기 "+pictureDataList.toString());

        //보관함이 비워졌을때 안내멘트 비져블, 곤 해주기
        if (pictureDataList.size() == 0 ) {
            drawer_word.setVisibility(View.VISIBLE);
            toolbar.getMenu().clear();
            Log.d("gg","kk");
        } else if (pictureDataList.size() != 0) {
            drawer_word.setVisibility(View.GONE);
            Log.d("gg","ll");
        }
        getActivity().invalidateOptionsMenu();
        drawerListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawerListAdapter = new DrawerListAdapter(pictureDataList);
    }

    //    툴바 셋팅 메서드
    private void setToolbar() {

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.alldelete, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.all_delete :
                if (pictureDataList.size() == 0) {
                    Toast.makeText(getContext(),"보관함에 삭제할 사진이 없어요",Toast.LENGTH_LONG).show();
                } else {
                    myDrawerAllDelete();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //내보관함에 메뉴를 툴바 오른쪽 버튼을 통해 전체 삭제하는 메서드
    private void myDrawerAllDelete() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<PictureData> results = realm.where(PictureData.class).findAll();


            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    results.deleteAllFromRealm();
                    drawerListAdapter.notifyDataSetChanged();
                    String message = "보관함에 내용이 전체 삭제 되었습니다";
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    refresh();

                }
            });
        }



    private void refresh() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.detach(this).attach(this).commit();

    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }



    // 온스타트 할때 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // 다시 부모 액티비티를 얻어서 툴바를 셋하는 작업을 한다
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
        toolbar = rootView.findViewById(R.id.drawer_toolbar);

        //어댑터 리사이클뷰 뷰를 적용 시키는 메서드
        fragmentToolbarSet();

        setAdapter();

        return rootView;
    }

    //온크레이트 메뉴를 적용시키기 위해 필요한 메서드
    private void fragmentToolbarSet() {
        setHasOptionsMenu(true); //onCreateOptionMenu에서 바뀔 menu 를 승인
        MainActivity activity = (MainActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    //어댑터 리사이클뷰 뷰를 적용 시키는 메서드
    private void setAdapter() {
        int numberOfColumns = 2;
        gridLayoutManager = new GridLayoutManager(getActivity(), numberOfColumns);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Realm realm = Realm.getDefaultInstance();
        realm.close();
    }
}