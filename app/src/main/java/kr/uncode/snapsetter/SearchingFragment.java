package kr.uncode.snapsetter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchingFragment extends Fragment implements View.OnClickListener {


    /**카카오 APi 이미지 목록을 보여주는 어댑터
     */
    private SearchListAdapter searchListAdapter;
    private EditText search_edit_frame;
    private Button searchBtn;
    /** 카카오 APi 검색 이미지를 보여주는
     서칭프래그먼트의 리사이클러뷰
     */
    private RecyclerView rvImageList;
    private ProgressBar progress_bar;
    private ImageView font;
    private TextView tx;

    private Realm mRealm;
    private Context context;
    private String query;


    private OnClickListener<String> onClickListener;
    private List<RetrofitResponse.Documents> dataList;
    //최근 검색어 리스트뷰 아이템 변수 시작
    private List<String> keywordList;
    private ListView listView;
    private KeywordAdapter keywordAdapter;
    private ArrayList<String> keywordArrayList;


    public static SearchingFragment newInstance() {
        return new SearchingFragment();
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d("11", "55");
        mRealm = Realm.getDefaultInstance();
    }



    @Override
    public void onResume() {
        super.onResume();
//        if (dataList != null) {
//            searchListAdapter.addDataAll(dataList);
//            Log.d("restart", "onResume");
//            Log.d("restart", "onResume + dataList" + dataList);
//        }

    }

    //온스타트에서 툴바를 넣어주기
    @Override
    public void onStart() {
        Log.d("restart", "onStart");

        Activity activity = getActivity();
        if (activity != null && activity instanceof MainActivity) {
            Log.d("dd", "서치에 들어와서 툴바를 넣으러");
            MainActivity mainActivity = (MainActivity) activity;
            Log.d("11", "33");
            mainActivity.removeToolbar(true);
        }
        Log.d("11", "11");
        super.onStart();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
//        context = getContext();
        Log.d("restart", "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("restart", "onCreateView");

        context = getContext();
        View rootView = inflater.inflate(R.layout.search_fragment, container, false);
        search_edit_frame = rootView.findViewById(R.id.search_edit_frame);
        searchBtn = rootView.findViewById(R.id.searchBtn);
        rvImageList = rootView.findViewById(R.id.rv_image_list);
        progress_bar = rootView.findViewById(R.id.progress_bar);
        font = rootView.findViewById(R.id.font);
        tx = rootView.findViewById(R.id.tx);
        Log.d("11", "22");

        searchBtn.setOnClickListener(this::onClick);
        listView = rootView.findViewById(R.id.listView);
        //더미 데이터 메서드

        keywordShow();

        // 리사이클러뷰에 어댑터 붙이기
        kakaoApiImageListSet();
//        searchListAdapter = new SearchListAdapter(mRealm);
//        rvImageList.setAdapter(searchListAdapter);

        //서치버튼을 클릭했을때 이미지를 찾는 온클릭 이벤트 메서드를 호출하는 버튼

        return rootView;
    }

    private void keywordShow() {
        keywordList = new ArrayList<String>();
        settingList();

        HashSet<String> tem = new HashSet<String>(keywordList);

        keywordArrayList = new ArrayList<>();
//        keywordArrayList.addAll(keywordList);
        if (tem != null) {
            keywordArrayList.addAll(tem);
            keywordAdapter = new KeywordAdapter(keywordArrayList, context);
            listView.setAdapter(keywordAdapter);
        }
        search_edit_frame.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable edit) {
                String text = search_edit_frame.getText().toString();
                keywordListItem(text);
            }
        });
        keywordList.clear();

    }

    private void kakaoApiImageListSet() {
        searchListAdapter = new SearchListAdapter(mRealm);
        rvImageList.setAdapter(searchListAdapter);
    }

    private void keywordListItem(String text) {

        keywordList.clear();
        if (text.length() == 0) {
        } else {
            if (keywordArrayList.size() != 0) {
                for (int i = 0; i < keywordArrayList.size(); i++) {
                    if (keywordArrayList.get(i).toLowerCase().contains(text)) {
                        keywordList.add(keywordArrayList.get(i));
                    }
                }

            }
        }
        keywordAdapter.notifyDataSetChanged();
    }

    private void hideKeyboard(View view) {
        Log.d("ddd", "왜!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(search_edit_frame.getWindowToken(), 0);
    }

    //    public String getQueryTx() {
//        searchingWord = search_edit_frame.getText().toString();
//        return searchingWord;
//    }
    //서치버튼을 클릭했을때 이미지를 찾는 온클릭 이벤트를 만드는 메서드
    private void btnSearch(View view) {
        String searchingWord = search_edit_frame.getText().toString();
        query = searchingWord;
        tx.setVisibility(View.GONE);
        font.setVisibility(View.GONE);
        if (TextUtils.isEmpty(query)) {
            showToast("검색어를 입력하세요.");
            return;
        }
        search(query);
    }

    private void settingList() {
        Realm realm = Realm.getDefaultInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser memberId = mAuth.getCurrentUser();
        String userName = memberId.getEmail();
        RealmResults<CurrentKeywordData> keywordData = realm.where(CurrentKeywordData.class).equalTo("userName",userName).findAll();

        Log.d("find keyword", "find keyword data : " +keywordData);

        for (CurrentKeywordData cd:keywordData) {
            keywordList.add(cd.getQuery());
        }
    }

    private void search(String query) {
        showProgressBar();
        RetrofitConfig.getService().search(query, "", "", "")
                .enqueue(new Callback<RetrofitResponse>() {
                    @Override
                    public void onResponse(Call<RetrofitResponse> call, Response<RetrofitResponse> response) {
                        if (response != null && response.body() != null) {
                            searchListAdapter.addDataAll(response.body().documents);
//                            searchListAdapter.getQuery(query);
                            Log.d("ccccc", "check query" + query);
                            currentKeywordSaver(query);
                        }
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<RetrofitResponse> call, Throwable t) {
                        hideProgressBar();
                        showToast("카카오 api 호출 실패 !!!");
                    }
                });
    }

    private void currentKeywordSaver(String query) {
        Realm realm = Realm.getDefaultInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser memberId = mAuth.getCurrentUser();
        String userName = memberId.getEmail();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CurrentKeywordData keywordData = realm.createObject(CurrentKeywordData.class);
                keywordData.setUserName(userName);
                keywordData.setQuery(query);

            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();

    }

    private void hideProgressBar() {
        progress_bar.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        progress_bar.setVisibility(View.VISIBLE);
    }
    @Override
    public void onClick(View view) {
        if (R.id.searchBtn == view.getId()) {
            Log.d("xx","xx");
            hideKeyboard(view);
            btnSearch(view);
        }
    }
}
