package kr.uncode.snapsetter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import kr.uncode.snapsetter.Drawer.DrawerFragment;

import static kr.uncode.snapsetter.R.string.navigation_drawer_open;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    /**
     * 네비게이션드로어에서 로그아웃 기능을 위해 파이어베이스 인증 변수를 사용함 (메인액티비티)
     */
    private FirebaseAuth mAuth;
    /**
     * 네비게이션드로어에서 로그아웃 기능을 위해 파이어베이스 인증 변수를 사용함 (메인액티비티)
     */
    private FirebaseUser currentUser;

    /**
     * 파이어베이스 인증을 통해 얻은 사용자이메일을 스트링타입 저장
     */
    private String recentUser = "";

    /**
     * 로그인 버튼
     */
    private Button loginBtn;
    /**
     * 회원가입버튼
     */
    private Button createIdBtn;

    /**
     * 머티리얼 디자인 에딧텍스 사용자가 입력한 패스워드를 들고있따
     */
    private TextInputLayout passwdedit;
    /**
     * 머티리얼 디자인 에딧텍스 사용자가 입력한 이메일=아이디를 들고있따
     */
    private TextInputLayout emailedit;

    /**
     * 사용자가 에딧텍스트에 입력한 아이디 값을 스트링을 변환하는 변수
     */
    private String email = "";
    /**
     * 사용자가 에딧텍스트에 입력한 패스워드 값을 스트링을 변환하는 변수
     */
    private String passwd = "";

    /**
     * 메인화면 툴바 왼쪽 버튼 누르면 나오는 네비게이션뷰 객체
     */
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private TextView toolbarTitle;
    private Toolbar toolbar;

    /**
     * 파이어베이스 어스 리스너 객체
     */
    public FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 파이어베이스 인증 얻기 --
        // 리스너가 자동로그인을 하기위해 auth정보를 가장먼저 받는다
        getAuth();

        //툴바셋팅
        toolbarSet();

        //파인드바이 뷰 및 툴바 설정
        initView();

        //프래그먼트 셋
        fragmentLayoutSet();

        //네비게이션 바를 여는 토글 버튼
        navigationToggle();

        //자동로그인 리스너
        authListener();

    }

    private void authListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                Log.d("ooooo", "outoLogin USer :" + currentUser);
                if (currentUser != null) {
                    // User is signed in
                    String userEmail = currentUser.getEmail();
                    Toast.makeText(MainActivity.this, "USER ID\n" + userEmail, Toast.LENGTH_SHORT).show();
                    Log.d("ff", "자동로그인 들어왔따");
                    replaceFragment(SearchFragment.newInstance());
                } else {
                    Toast.makeText(MainActivity.this, "no id got", Toast.LENGTH_SHORT).show();
                    Log.d("ff", "자동로그인 안들어왔따");
                }
            }
        };
    }

    private void getAuth() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

    }

    @Override
    protected void onStop() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        super.onStop();
    }

    @Override
    protected void onStart() {
        mAuth.addAuthStateListener(mAuthListener);
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void navigationToggle() {
        toggle = new ActionBarDrawerToggle(MainActivity.this, drawer, navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

    }

    //이걸 해야지 이xml아이디를 쓸수있는 == 온크리에이트와 같은거임
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu, menu);
//        return true;
//
//    }

    private void fragmentLayoutSet() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, MainFragment.newInstance()).commit();
    }

    //init View setting
    private void initView() {

        loginBtn = findViewById(R.id.loginBtn);
        createIdBtn = findViewById(R.id.createIdBtn);
        passwdedit = findViewById(R.id.passwdedit);
        emailedit = findViewById(R.id.emailedit);
        drawer = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        toolbarTitle = findViewById(R.id.toolbarTex);


    }

    private void toolbarSet() {

        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTex);
        toolbarTitle.setOnClickListener(this::onClick);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    //TODO 내보관함에 들어갔을때만 툴바 오른쪽 버튼 보이고 보관함에 모든 데이터 삭제하기
    public void showToolbarRightBtn(Boolean show) {
        //true
        if (show) {
        }

    }

    // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null).commit();
    }


    @Override
    public void onClick(View view) {

        if (view == toolbarTitle) {
            Log.d("tt", "toolbattitttle");
            if (recentUser != null) {
                replaceFragment(SearchFragment.newInstance());
            } else if (recentUser == null) {
                replaceFragment(MainFragment.newInstance());
            }
        }
        if (view == loginBtn) {
            email = emailedit.getEditText().getText().toString();
            passwd = passwdedit.getEditText().getText().toString();

            Log.d("hi", email);
            Log.d("hi", passwd);

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(passwd)) {
                Toast.makeText(getApplicationContext(), "이메일 OR 비밀번호를 입력해주세요", Toast.LENGTH_LONG).show();
            }
        }

        if (view == createIdBtn) {
            email = emailedit.getEditText().getText().toString();
            passwd = passwdedit.getEditText().getText().toString();

            Log.d("hi", email);
            Log.d("hi", passwd);

            createUser(email, passwd);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_logout) {

            logout();
            Log.d("nav", "nav1");
        } else if (id == R.id.nav_drawer) {
            Log.d("nav", "nav2");
            replaceFragment(DrawerFragment.newInstance());

        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.all_delete_btn: { // 오른쪽 상단 버튼 눌렀을 때
                Toast.makeText(getApplicationContext(), "click", Toast.LENGTH_SHORT).show();
            }
        }

        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    //로그아웃
    private void logout() {
        mAuth.getInstance().signOut();
        Toast.makeText(MainActivity.this, "logout!! bye~", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    //회원 가입 메서드
    private void createUser(String email, String passwd) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(passwd)) {
            Toast.makeText(getApplicationContext(), "이메일과 비밀번호를 확인해주세요", Toast.LENGTH_LONG).show();
        } else {

            mAuth.createUserWithEmailAndPassword(email, passwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(getApplicationContext(), "회원가입성공", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}
