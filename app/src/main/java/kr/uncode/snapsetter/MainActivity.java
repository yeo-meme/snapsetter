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
import android.view.Window;
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

import java.time.LocalDate;
import java.util.Stack;

import kr.uncode.snapsetter.Drawer.DrawerFragment;

import static kr.uncode.snapsetter.R.string.navigation_drawer_open;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    /**
     * 네비게이션드로어에서 로그아웃 기능을 위해 파이어베이스 인증 변수를 사용함 (메인액티비티)
     */
    private FirebaseAuth mAuth;

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



    final static int FRAGMENT_SEARCH= 1001;
    final static int FRAGMENT_DRAWER = 1002;
    final static int FRAGMENT_MAIN = 1003;


    public static DrawerFragment drawerFragment;
    public static MainFragment mainFragment;
    public static SearchFragment searchFragment;

    public static Stack<Fragment> fragmentStack;

    public static FragmentManager manager;

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
        try {
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser listnerCurrentUser = mAuth.getCurrentUser();
                    Log.d("ooooo", "outoLogin USer :" + listnerCurrentUser);
                    if (listnerCurrentUser != null) {
                        // User is signed in
                        String userEmail = listnerCurrentUser.getEmail();
//                        Toast.makeText(MainActivity.this, "USER ID\n" + userEmail, Toast.LENGTH_SHORT).show();
                        Log.d("ff", "자동로그인 들어왔따");
                        replaceFragment(SearchFragment.newInstance());
                    } else {
//                    Toast.makeText(MainActivity.this, "로그인 후 이용 부탁드립니다", Toast.LENGTH_SHORT).show();
                        Log.d("ff", "자동로그인 안들어왔따 랑 사용자가 로그아웃상태");
                    }
                }
            };
        } catch (Exception e) {
            Toast.makeText(getApplicationContext()," firebase에러라고 띄우고 잠시후에 다시 시도하세요",Toast.LENGTH_LONG);
            e.printStackTrace();
        }
    }

    private void getAuth() {
        mAuth = FirebaseAuth.getInstance();
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

        mainFragment = new MainFragment();
        searchFragment = new SearchFragment();
        drawerFragment = new DrawerFragment();


//        fragmentStack.push(mainFragment);
//        manager = getSupportFragmentManager();
//        manager.beginTransaction().replace(R.id.frameLayout, mainFragment);


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, MainFragment.newInstance()).commit();
    }

//    public static void changeFragment(int index) {
//        switch (index) {
//            case FRAGMENT_MAIN :
//                manager.beginTransaction().replace(R.id.frameLayout, mainFragment).commit();
//                break;
//            case  FRAGMENT_SEARCH:
//                manager.beginTransaction().replace(R.id.frameLayout, searchFragment).commit();
//                break;
//            case  FRAGMENT_DRAWER:
//                manager.beginTransaction().replace(R.id.frameLayout,drawerFragment).commit();
//                break;
//        }
//    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }

    //init View setting
    private void initView() {

        loginBtn = findViewById(R.id.loginBtn);
        createIdBtn = findViewById(R.id.createIdBtn);
        passwdedit = findViewById(R.id.passwdedit);
        emailedit = findViewById(R.id.emailedit);
        drawer = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);


    }

    public void toolbarSet() {
        Log.d("dd","set1");
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTex);
        toolbarTitle.setOnClickListener(this::onClick);
        setSupportActionBar(toolbar);

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


    //메인프래그먼트가 이메서드를 호출하여 메인화면에 툴바를 삭제한다
    public void removeToolbar(Boolean useToolbar) {
        if (useToolbar) {
//            toolbarSet();
            getSupportActionBar().show();
            toolbar.setVisibility(View.VISIBLE);
            Log.d("dd","툴바를 셋팅한다");
        } else  {
            Log.d("dd","set2222222");

            getSupportActionBar().hide();
            toolbar.setVisibility(View.GONE);
            Log.d("dd","폴스로 툴바를 지운다");
        }
    }


    @Override
    public void onClick(View view) {

        getAuth();
        FirebaseUser memberId = mAuth.getCurrentUser();
        if (view == toolbarTitle) {
            Log.d("tt", "toolbattitttle");
            if (memberId != null) {
                replaceFragment(SearchFragment.newInstance());
                Log.d("kk","로그인 상태에서  툴바제목 선택하기 : " + memberId);

            } else if (memberId == null) {
                Log.d("kk","로그아웃 후 메인에서 툴바제목 선택하기 : " + memberId);
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
