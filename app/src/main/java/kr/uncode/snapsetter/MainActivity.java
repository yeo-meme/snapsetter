package kr.uncode.snapsetter;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import kr.uncode.snapsetter.Current.TabViewDrawer;

import static kr.uncode.snapsetter.R.string.navigation_drawer_open;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener  {

    /**
     * 네비게이션드로어에서 로그아웃 기능을 위해 파이어베이스 인증 변수를 사용함 (메인액티비티)
     */
    private FirebaseAuth mAuth;


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

    private long pressedTime = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 파이어베이스 인증 얻기 --
        // 리스너가 자동로그인을 하기위해 auth정보를 가장먼저 받는다
        getAuth();

        //디폴트 툴바셋팅
        toolbarset();

        //파인드바이 뷰 및 툴바 설정
        initView();

        //자동로그인 리스너
        authListener();

    }





    //디폴트 툴바셋팅
    public void toolbarset() {
        //툴바는 첨에 셋팅하지만 -> 메인프래그먼트가 바로 호출되면서 -> 툴바를 감추는
        // 메인액티비티에 메서드를 호출하기 때문에 하이드 된다
        //처음부터 셋팅이 안되어 있으면 이 코드에서 hide가 널이 나기 때문에 첫 세팅을 했다
        toolbar = findViewById(R.id.toolbar);
        //액션바와 툴바의 차이 뭔들
        //일단 툴바가 더 유연하고 액션바를 네비게이션 토글 버튼을 구현하려면 필요한거 같다
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //자동 네비게이션 토글 버튼 연결
        actionBar.setDisplayHomeAsUpEnabled(true);
        Log.d("gg","여기는 오니?");
    }


    //어더 리스너 처음에 사용자가 기억에 있으면 = 서치프래그먼트로 (검색창)
    //처음에 사용자가 없으면 = 메인프래그먼트로 (회원가이브,로그인)
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
                        Log.d("ff", "자동로그인 들어왔따");
                        replaceFragmentNoStack(SearchingFragment.newInstance());
                    } else {
                        Log.d("ff", "자동로그인 안들어왔따 랑 사용자가 로그아웃상태");
                        replaceFragmentNoStack(MainContainer.newInstance());
                    }
                }
            };
        } catch (Exception e) {
            Toast.makeText(getApplicationContext()," firebase 오류! 띄우고 잠시후에 다시 시도하세요",Toast.LENGTH_LONG);
            e.printStackTrace();
        }
    }

    private void getAuth() {
        mAuth = FirebaseAuth.getInstance();
        Log.d("log","first getAuth");
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

    //네비게이션 드로우를 열고 닫는 토글버튼 장착
    public void navigationToggle() {
        Log.d("dd","when");
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


    //init View setting
    private void initView() {

        loginBtn = findViewById(R.id.loginBtn);
        createIdBtn = findViewById(R.id.createIdBtn);
        passwdedit = findViewById(R.id.passwdedit);
        emailedit = findViewById(R.id.emailedit);
        drawer = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);


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
    public void replaceFragmentNoStack(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
        //addToBackStack(null).
    }

    //메인프래그먼트가 이메서드를 호출하여 메인화면에 툴바를 삭제한다

    /**
     * 불린 값이 트루 = 비져블.쇼우 , 불린 값이 폴스이면 = 곤.하이드
     */
    public void removeToolbar(Boolean useToolbar) {
        if (useToolbar) {
            toolbarset();
            getSupportActionBar().show();
            toolbar.setVisibility(View.VISIBLE);
            navigationToggle();
            Log.d("dd","툴바를 셋팅한다");
        } else  {
            Log.d("dd","set2222222");
            getSupportActionBar().hide();
            toolbar.setVisibility(View.GONE);
            Log.d("dd","폴스로 툴바를 지운다");
        }
    }


    private void serch(String charText) {


    }
    @Override
    public void onClick(View view) {

        //툴바 메뉴를 선택했을때 메인화면 이동하는 메서드
        getAuth();
        FirebaseUser memberId = mAuth.getCurrentUser();
        if (view == toolbarTitle) {
            Log.d("tt", "toolbattitttle");
            if (memberId != null) {
                replaceFragment(SearchingFragment.newInstance());
                Log.d("kk","로그인 상태에서  툴바제목 선택하기 : " + memberId);

            } else if (memberId == null) {
                Log.d("kk","로그아웃 후 메인에서 툴바제목 선택하기 : " + memberId);
                replaceFragment(MainContainer.newInstance());
            }
        }

        //TODO 로그인 버튼을 눌를떄 프래그먼트에서 작동하는데 여기서 코드를 사용하는지 확인할것
//        if (view == loginBtn) {
//            email = emailedit.getEditText().getText().toString();
//            passwd = passwdedit.getEditText().getText().toString();
//
//            Log.d("hi", email);
//            Log.d("hi", passwd);
//
//            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(passwd)) {
//                Toast.makeText(getApplicationContext(), "이메일 OR 비밀번호를 입력해주세요", Toast.LENGTH_LONG).show();
//            }
//        }


        //TODO 크리에이트 아디 버튼을 눌렀을때 회원가입로직 : 프래그 먼트에서 하는데
        //여기서도 코드를 쓰는지 확인해보것
        if (view == createIdBtn) {
            email = emailedit.getEditText().getText().toString();
            passwd = passwdedit.getEditText().getText().toString();

            Log.d("hi", email);
            Log.d("hi", passwd);

//            createUser(email, passwd);
        }
    }

    //네비게이션 드로어 설렉티드
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            logout();
            Log.d("nav", "사용자가 로그아웃버튼을 선택 이벤트 ");
        } else if (id == R.id.nav_drawer) {
            Log.d("nav", "사용자가 네비게이션드로어 메뉴를 선택");
//            replaceFragment(DrawerFragment.newInstance());
            Intent intent = new Intent(getApplicationContext(), TabViewDrawer.class);
            startActivity(intent);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //로그아웃
    private void logout() {
        mAuth.getInstance().signOut();
        Toast.makeText(MainActivity.this, "로그아웃되었습니다:)!! bye~", Toast.LENGTH_LONG).show();
        replaceFragmentNoStack(MainContainer.newInstance());
    }


}
