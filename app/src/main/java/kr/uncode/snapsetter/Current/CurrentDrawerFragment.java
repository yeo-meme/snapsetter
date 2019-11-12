package kr.uncode.snapsetter.Current;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.realm.Realm;
import io.realm.RealmResults;
import kr.uncode.snapsetter.BuildConfig;
import kr.uncode.snapsetter.Drawer.DrawerListAdapter;
import kr.uncode.snapsetter.PictureData;
import kr.uncode.snapsetter.R;

public class CurrentDrawerFragment extends Fragment {

    private RecyclerView recyclerView;
    private String currentUrl = "";
    private RealmResults<CurrentUserPicData> dataList;
    private GridLayoutManager gridLayoutManager;
    private CurrentListAdapter currentListAdapter;
    private Button all_delete_btn;

    public static CurrentDrawerFragment newInstance() {
        return new CurrentDrawerFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_currentdrawer, container, false);
        recyclerView = view.findViewById(R.id.rcList);

        all_delete_btn = view.findViewById(R.id.all_delete_btn);


        Realm realm = Realm.getDefaultInstance();

        dataList = realm.where(CurrentUserPicData.class).findAll();

        currentListAdapter = new CurrentListAdapter(realm.where(CurrentUserPicData.class).findAll());
        recyclerView.setAdapter(currentListAdapter);

        onClicklistener(view);
        Log.d("xx", "current Image get List : " + dataList);
        setAdapter();

        return view;
    }

    private void onClicklistener(View view) {
        all_delete_btn.setOnClickListener(this::allDataDelete);
    }

    private void allDataDelete(View view) {
        Log.d("zzz", "mydrawer delete");
        Realm realm = Realm.getDefaultInstance();
        RealmResults<CurrentUserPicData> results = realm.where(CurrentUserPicData.class).findAll();


        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();

                CurrentListAdapter currentListAdapter = new CurrentListAdapter(results);
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
    }
}

