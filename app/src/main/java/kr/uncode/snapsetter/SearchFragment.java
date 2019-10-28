package kr.uncode.snapsetter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import io.realm.Realm;
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

        return rootView;
    }

    //서치버튼을 클릭했을때 이미지를 찾는 온클릭 이벤트를 만드는 메서드
    private void btnSearch(View view) {
        String query = search_edit_frame.getText().toString();
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
