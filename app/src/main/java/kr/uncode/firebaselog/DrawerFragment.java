package kr.uncode.firebaselog;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


    private GridLayoutManager gridLayoutManager;
    //-----------------------------------------------------더미데이타
    private ArrayList<DermyData> dermyDataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DrawerListAdapter mAdater;
    private ArrayList<DermyData> mDataset;



    public static DrawerFragment newInstance() {
        return new DrawerFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareData();
    }

    private void prepareData() {
        dermyDataList.add(new DermyData("서울시청","호호"));
        dermyDataList.add(new DermyData("경복궁","호호"));
        dermyDataList.add(new DermyData("서울역","호호"));
        dermyDataList.add(new DermyData("남산","호호"));
        dermyDataList.add(new DermyData("을지로입구역","호호"));



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
//        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            Log.d("ddd", "ddd");
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        int numberOfColumns = 5;

        gridLayoutManager = new GridLayoutManager(getActivity(), numberOfColumns);
        View rootView = inflater.inflate(R.layout.fragment_drawer, container, false);

        drawer_rc = rootView.findViewById(R.id.drawer_rc);
        drawer_rc.setHasFixedSize(true);
        drawerListAdapter = new DrawerListAdapter(dermyDataList);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        drawer_rc.setLayoutManager(gridLayoutManager);
        drawer_rc.setAdapter(drawerListAdapter);
        drawerListAdapter.addDataAll(data);

        setDetail_img(rootView);
        return rootView;
    }


    public void setDetail_img(View view) {
        final Realm realm = Realm.getDefaultInstance();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.d("zzzzzzzzzzzzzzz", "zzzzzzzzzzzzzzzzzz");
                RealmResults<PictureData> pic = realm.where(PictureData.class).equalTo("name", "동구")
                        .findAll();

                if (pic.size() != 0) {
                    Log.d("11", "데이타있음");
                } else {
                    Log.d("11", "데이터없음");

                }
            }

        });
//
    }
}