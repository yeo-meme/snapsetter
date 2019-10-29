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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment implements View.OnClickListener {
    private SearchListAdapter searchListAdapter;
    private EditText search_edit_frame;
    private Button searchBtn;
    private RecyclerView rvImageList;
    private ProgressBar progress_bar;
    private ImageView font;
    private TextView tx;
    private InputMethodManager manager;
    private LinearLayout back;

    private FirebaseAuth mAuth;
    private ImageView eHeart;
    private ImageView heart;
    private static final int RC_SIGN_IN = 900;

    private static final String EMPTY_H = "EMPTY";
    private static final String FULL_H = "FULL";
    private String heart_state = EMPTY_H;


    private Realm mRealm;
    private Context context;


    //리스트뷰 아이템 시작

    private List<String> list;
//    private ListView listView;
    private SearchAdapter adapter;
    private ArrayList<String> arrayList;



    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mRealm != null && !mRealm.isClosed()) mRealm.close();
    }



    //온스타트에서 툴바를 넣어주기
    @Override
    public void onStart() {
        Activity activity = getActivity();
        if (activity != null && activity instanceof MainActivity) {
            Log.d("dd","서치에 들어와서 툴바를 넣으러");
            MainActivity mainActivity = (MainActivity) activity;
            mainActivity.removeToolbar(true);
        }
        super.onStart();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        context = getContext();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.search_fragment, container, false);
        search_edit_frame = rootView.findViewById(R.id.search_edit_frame);
        searchBtn = rootView.findViewById(R.id.searchBtn);
        rvImageList = rootView.findViewById(R.id.rv_image_list);
        progress_bar = rootView.findViewById(R.id.progress_bar);
        font = rootView.findViewById(R.id.font);
        tx = rootView.findViewById(R.id.tx);
        back = rootView.findViewById(R.id.back);

//        listView = rootView.findViewById(R.id.listView);


//        list = new ArrayList<String>();
//        settingList();
//
//        arrayList = new ArrayList<String>();
//        arrayList.addAll(list);


//        adapter = new SearchAdapter(list, context);
//        listView.setAdapter(adapter);

//        search_edit_frame.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable edit) {
//
//                String text = search_edit_frame.getText().toString();
////                listitem(text);
//
//            }
//        });


        // 키보드내리기
//        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        searchListAdapter = new SearchListAdapter(mRealm);
        rvImageList.setAdapter(searchListAdapter);

        //서치버튼을 클릭했을때 이미지를 찾는 온클릭 이벤트 메서드를 호출하는 버튼
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSearch(container.getRootView());
            }
        });

//        list.clear();
        return rootView;
    }

//    private void listitem(String charText) {
//        list.clear();
//
//        if (charText.length() == 0) {
//            for (int i =0; i<arrayList.size(); i++) {
//                if (arrayList.get(i).toLowerCase().contains(charText)) {
//                    list.add(arrayList.get(i));
//                }
//            }
//
//        } else {
//            for (int i =0; i<arrayList.size(); i++) {
//                if (arrayList.get(i).toLowerCase().contains(charText)) {
//                    list.add(arrayList.get(i));
//                }
//            }
//
//        }
//
//        adapter.notifyDataSetChanged();
//    }

    //최근 검색 기록
    private void settingList() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<PictureData> keyword = realm.where(PictureData.class).findAll();

        for (PictureData pd : keyword) {
            pd.getKeyword();
            Log.d("dd","get keyword :" + pd.getKeyword());
            String aa = pd.getKeyword();
            list.add(aa);
        }

    }


    public static void downKeyboard(Context context, EditText editText)
    { InputMethodManager mInputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
    mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0); }

    //서치버튼을 클릭했을때 이미지를 찾는 온클릭 이벤트를 만드는 메서드
    private void btnSearch(View view) {
        String query = search_edit_frame.getText().toString();
        downKeyboard(context,search_edit_frame);
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                PictureData pictureData = realm.createObject(PictureData.class);
                pictureData.setKeyword(query);
            }
        });
        tx.setVisibility(View.GONE);
        font.setVisibility(View.GONE);
        if (TextUtils.isEmpty(query)) {
            showToast("검색어를 입력하세요.");
            return;
        }
        search(query);
    }

    private void search(String query) {
        showProgressBar();
        RetrofitConfig.getService().search(query, "", "", "")
                .enqueue(new Callback<RetrofitResponse>() {
                    @Override
                    public void onResponse(Call<RetrofitResponse> call, Response<RetrofitResponse> response) {
                        if (response != null && response.body() != null) {
                            searchListAdapter.addDataAll(response.body().documents);
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

    }
}
