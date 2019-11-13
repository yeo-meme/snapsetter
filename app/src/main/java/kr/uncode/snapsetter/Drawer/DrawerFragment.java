package kr.uncode.snapsetter.Drawer;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import io.realm.Realm;
import io.realm.RealmResults;
import kr.uncode.snapsetter.MainActivity;
import kr.uncode.snapsetter.PictureData;
import kr.uncode.snapsetter.R;
import kr.uncode.snapsetter.SearchingFragment;

public class DrawerFragment extends Fragment {


    /**
     * 내보관함 리사이클러뷰 변수
     */
    private RecyclerView recyclerView;

    /**
     * 리얼엠 리절트 를 픽쳐데이타 타입 리스트로 보관하는 변수
     */
    private RealmResults<PictureData> pictureDataList;

    /**
     * 보관함에 리사이클러뷰 정렬을 위해 매니저 변수 사용
     */
    private GridLayoutManager gridLayoutManager;

    /**
     * 보관함에 리스트를 가져오기 위해 어댑터 셋하기 위한 변수
     */
    private RecyclerView.Adapter mAdater;


    private RealmResults<PictureData> results;
    /**
     * 데이터 == 0 일때 리사이클러뷰대신 안내 텍스트 보이게하는 텍스트
     */
    private TextView drawer_word;

    private Toolbar toolbar;

    private Button all_delete_btn;

    //리섬이 시작할때 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //다시 리얼엠 인스턴스를 불러오고
    //드로어 어댑터에 리얼엠 데이터를 모두 받아서 넘긴다
    //리사이클뷰에 어답터를 작용해서 셋


    public static DrawerFragment newInstance() {
        Log.d("zzz", "100");

        return new DrawerFragment();
    }


//    public static boolean isActivityAvailable(Activity activity) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            return !activity.isFinishing() && !activity.isDestroyed();
//        } else {
//            return !activity.isFinishing();
//        }
//    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Activity activity = getActivity();

        View rootView = inflater.inflate(R.layout.fragment_drawer, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView_drawer);
        drawer_word = rootView.findViewById(R.id.drawer_word);
        all_delete_btn = rootView.findViewById(R.id.all_delete_btn);

        dataSet();
//        Realm realm = Realm.getDefaultInstance();
//        //데이터 주석 확인
//        pictureDataList = realm.where(PictureData.class).findAll();
//
//        //실제 어댑터 담아서 사용
//        drawerListAdapter = new DrawerListAdapter(realm.where(PictureData.class).findAll());
//        recyclerView.setAdapter(drawerListAdapter);
//
//        //보관함에 저장된 데이터 로그 확인
//        Log.d("dd", "내보관함 사진뿌리기 " + pictureDataList.toString());
//        drawerListAdapter = new DrawerListAdapter(pictureDataList);

//        checkDrawerEmpty();
        //보관함이 비워졌을때 안내멘트 비져블, 곤 해주기
//        if (pictureDataList.size() == 0) {
//            drawer_word.setVisibility(View.VISIBLE);
////                toolbar.getMenu().clear();
//            Log.d("gg", "kk");
//        } else if (pictureDataList.size() != 0) {
//            drawer_word.setVisibility(View.GONE);
//            Log.d("gg", "ll");
//        }
//        getActivity().invalidateOptionsMenu();
        Log.d("zzz", "88");

        //어댑터 리사이클뷰 뷰를 적용 시키는 메서드
//        fragmentToolbarSet();
        setAdapter();
        Log.d("zzz", "99");

        initClickListener(rootView);
        return rootView;
    }

    private void initClickListener(View view) {
        all_delete_btn.setOnClickListener(this::myDrawerAllDelete);

    }

    private void checkDrawerEmpty() {
        if (pictureDataList.size() == 0) {
            drawer_word.setVisibility(View.VISIBLE);
//                toolbar.getMenu().clear();
            Log.d("gg", "kk");
        } else if (pictureDataList.size() != 0) {
            drawer_word.setVisibility(View.GONE);
            Log.d("gg", "ll");
        }
    }

    private void dataSet() {
        Realm realm = Realm.getDefaultInstance();
        //데이터 주석 확인
        pictureDataList = realm.where(PictureData.class).findAll();

        //실제 어댑터 담아서 사용
        DrawerListAdapter drawerListAdapter = new DrawerListAdapter(realm.where(PictureData.class).findAll());
        recyclerView.setAdapter(drawerListAdapter);
        checkDrawerEmpty();
        //보관함에 저장된 데이터 로그 확인
        Log.d("dd", "내보관함 사진뿌리기 " + pictureDataList.toString());
//        drawerListAdapter = new DrawerListAdapter(pictureDataList);
    }

    @Override
    public void onResume() {
        super.onResume();
//            DrawerListAdapter drawerListAdapter = new DrawerListAdapter(results);
//            drawerListAdapter.notifyDataSetChanged();
        Realm realm = Realm.getDefaultInstance();
        //데이터 주석 확인
        pictureDataList  = realm.where(PictureData.class).findAll();

        //실제 어댑터 담아서 사용
        DrawerListAdapter drawerListAdapter= new DrawerListAdapter(realm.where(PictureData.class).findAll());
        recyclerView.setAdapter(drawerListAdapter);

        //보관함에 저장된 데이터 로그 확인
        Log.d("dd","내보관함 사진뿌리기 "+pictureDataList.toString());
//        drawerListAdapter = new DrawerListAdapter(pictureDataList);

        //보관함이 비워졌을때 안내멘트 비져블, 곤 해주기
        if (pictureDataList.size() == 0 ) {
            drawer_word.setVisibility(View.VISIBLE);
//            toolbar.getMenu().clear();
            Log.d("gg","kk");
        } else if (pictureDataList.size() != 0) {
            drawer_word.setVisibility(View.GONE);
            Log.d("gg","ll");
        }
//        getActivity().invalidateOptionsMenu();
        Log.d("zzz","88");

        drawerListAdapter.notifyDataSetChanged();
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        Log.d("zzz", "77");
//        inflater.inflate(R.menu.alldelete, menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.all_delete:
//                if (pictureDataList.size() == 0) {
//                    Toast.makeText(getContext(), "보관함에 삭제할 사진이 없어요", Toast.LENGTH_LONG).show();
//                } else {
//                    myDrawerAllDelete();
//
//                }
//
//                break;
//        }
//        Log.d("zzz", "66");
//
//        return super.onOptionsItemSelected(item);
//    }

    //내보관함에 메뉴를 툴바 오른쪽 버튼을 통해 전체 삭제하는 메서드
    private void myDrawerAllDelete(View view) {

        Log.d("zzz", "mydrawer delete");

        Realm realm = Realm.getDefaultInstance();
        RealmResults<PictureData> results = realm.where(PictureData.class).findAll();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();

                DrawerListAdapter drawerListAdapter = new DrawerListAdapter(results);
                String message = "보관함에 내용이 전체 삭제 되었습니다";
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                refresh();
//                SearchingFragment searchingFragment = new SearchingFragment();
//                searchingFragment.hideKeyboard(searchingFragment.getView());
//                searchingFragment.hideProgressBar();
                Log.d("zzz", "mydrawer delete");

            }
        });
    }


    private void refresh() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.detach(this).attach(this).commit();
        Log.d("zzz", "44");

    }


    //온크레이트 메뉴를 적용시키기 위해 필요한 메서드
    private void fragmentToolbarSet() {
        setHasOptionsMenu(true); //onCreateOptionMenu에서 바뀔 menu 를 승인
        Activity activity = getActivity();
        if (activity != null && activity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) activity;
            mainActivity.setSupportActionBar(toolbar);
            ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        Log.d("zzz", "come");
    }

    //어댑터 리사이클뷰 뷰를 적용 시키는 메서드
    private void setAdapter() {
        int numberOfColumns = 2;
        Context context = getContext();
        gridLayoutManager = new GridLayoutManager(context, numberOfColumns);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        Log.d("zzz", "xx22xx");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Realm realm = Realm.getDefaultInstance();
        realm.close();
        Log.d("zzz", "11");

    }

}


