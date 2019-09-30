package kr.uncode.firebaselog;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private SearchListAdapter searchListAdapter;
    private EditText search_edit_frame;
    private Button searchBtn;
    private RecyclerView rvImageList;
    private ProgressBar progress_bar;
    private ImageView font;
    private TextView tx;
    private InputMethodManager manager;
    private LinearLayout back;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_edit_frame = findViewById(R.id.search_edit_frame);
        searchBtn = findViewById(R.id.searchBtn);
        rvImageList = findViewById(R.id.rv_image_list);
        progress_bar = findViewById(R.id.progress_bar);
        font = findViewById(R.id.font);
        tx = findViewById(R.id.tx);
        back = findViewById(R.id.back);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);



        searchListAdapter = new SearchListAdapter();

        rvImageList.setAdapter(searchListAdapter);

        back.setOnClickListener(this);

        searchBtn.setOnClickListener(this);

    }


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


    private void showProgressBar() {
        progress_bar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progress_bar.setVisibility(View.GONE);
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
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();

    }

    @Override
    public void onClick(View view) {


        if (view == searchBtn) {
            btnSearch(view);
        }

        if (view == back) {
            hideKeyboard();
        }

    }

    private void hideKeyboard() {

        manager.hideSoftInputFromWindow(search_edit_frame.getWindowToken(),0);
    }
}


