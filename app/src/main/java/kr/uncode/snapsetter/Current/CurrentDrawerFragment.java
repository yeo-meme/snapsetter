package kr.uncode.snapsetter.Current;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.realm.Realm;
import io.realm.RealmResults;
import kr.uncode.snapsetter.R;

public class CurrentDrawerFragment extends Fragment {

    private RecyclerView recyclerView;
    private String currentUrl ="";
    private RealmResults<CurrentUserPicData> dataList;
    private GridLayoutManager gridLayoutManager;
    private CurrentListAdapter currentListAdapter;

    public static CurrentDrawerFragment newInstance() {
        return new CurrentDrawerFragment();
    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_currentdrawer,container,false);
        recyclerView = view.findViewById(R.id.rcList);


        Realm realm = Realm.getDefaultInstance();

        dataList = realm.where(CurrentUserPicData.class).findAll();

        currentListAdapter = new CurrentListAdapter(realm.where(CurrentUserPicData.class).findAll());
        recyclerView.setAdapter(currentListAdapter);

        Log.d("xx","current Image get List : " + dataList);
        setAdapter();

        return view;
    }

    private void setAdapter() {
        int numberOfColumns = 2;
        Context context = getContext();
        gridLayoutManager = new GridLayoutManager(context, numberOfColumns);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        Log.d("zzz","xx22xx");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Realm realm = Realm.getDefaultInstance();
        realm.close();
    }
}

