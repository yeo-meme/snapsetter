package kr.uncode.firebaselog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.google.firebase.auth.FirebaseAuth;

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

    private Button logout;
    private FirebaseAuth mAuth;

    private ImageView eHeart;
    private ImageView heart;

    private ListItemImage listItemImage;


    private static final int RC_SIGN_IN = 900;

    private static final String EMPTY_H = "EMPTY";
    private static final String FULL_H = "FULL";
    private String heart_state = EMPTY_H;


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


//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        listItemImage = new ListItemImage();


        logout = findViewById(R.id.logout);

        searchListAdapter = new SearchListAdapter();

        rvImageList.setAdapter(searchListAdapter);

        back.setOnClickListener(this);

        searchBtn.setOnClickListener(this);

        logout.setOnClickListener(this);

//        emptyheart.setOnClickListener(this);
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

        if (view == logout) {
            logout();
        }


    }

//    private void heartChange() {
//        if (heart_state == EMPTY_H) {
//            emptyheart.setImageResource(R.drawable.eheart);
//            heart_state = EMPTY_H;
//        } else if (heart_state == FULL_H) {
//            emptyheart.setImageResource(R.drawable.heart);
//            heart_state = FULL_H;
//        }
//
//    }

    private void logout() {
        mAuth.getInstance().signOut();
        Toast.makeText(SearchActivity.this,"logout!! bye~",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void hideKeyboard() {

        manager.hideSoftInputFromWindow(search_edit_frame.getWindowToken(),0);
    }
}


