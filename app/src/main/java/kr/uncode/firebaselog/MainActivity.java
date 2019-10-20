package kr.uncode.firebaselog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import static kr.uncode.firebaselog.R.string.navigation_drawer_open;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Button loginBtn;
    private Button createIdBtn;

    private TextInputLayout passwdedit;
    private TextInputLayout emailedit;

    private String email = "";
    private String passwd = "";

    /**
     * 메인화면 툴바 왼쪽 버튼 누르면 나오는 네비게이션뷰 객체
     */
    private NavigationView navigationView;
    private DrawerLayout drawer;

    public  MenuInflater menuInflater;
    private ActionBarDrawerToggle toggle;
    private MainFragment mainFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //파인드바이 뷰 및 툴바 설정
        initView();

        //프래그먼트 셋
        fragmentLayoutSet();


        //네비게이션 바를 여는 토글 버튼
        toggle = new ActionBarDrawerToggle(MainActivity.this, drawer, navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        mAuth = FirebaseAuth.getInstance();

    }

    //이걸 해야지 이xml아이디를 쓸수있는 == 온크리에이트와 같은거임
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;

    }

    private void fragmentLayoutSet() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, MainFragment.newInstance()).commit();
        mainFragment = new MainFragment();


    }


    //init View setting
    private void initView() {

        loginBtn = findViewById(R.id.loginBtn);
        createIdBtn = findViewById(R.id.createIdBtn);
        passwdedit = findViewById(R.id.passwdedit);
        emailedit = findViewById(R.id.emailedit);
        drawer = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

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

        if (view == loginBtn) {
            email = emailedit.getEditText().getText().toString();
            passwd = passwdedit.getEditText().getText().toString();

            Log.d("hi", email);
            Log.d("hi", passwd);

            loginUser();
            Toast.makeText(getApplicationContext(), "hi", Toast.LENGTH_LONG).show();
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


    //로그인 메서드
    private void loginUser() {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(passwd)) {
            Toast.makeText(getApplicationContext(), "이메일 OR 비밀번호를 입력해주세요", Toast.LENGTH_LONG).show();

        } else {
            email = emailedit.getEditText().getText().toString();
            passwd = passwdedit.getEditText().getText().toString();

            mAuth.signInWithEmailAndPassword(email, passwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException e) {
                                    Toast.makeText(getApplicationContext(), "존재하지 않는 id 입니다.", Toast.LENGTH_SHORT).show();
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    Toast.makeText(getApplicationContext(), "이메일 형식이 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                                } catch (FirebaseNetworkException e) {
                                    Toast.makeText(getApplicationContext(), "Firebase NetworkException", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                currentUser = mAuth.getCurrentUser();
                                Toast.makeText(getApplicationContext(), "로그인 성공" + "/" + currentUser.getEmail() + "/" + currentUser.getUid(), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                                finish();

                            }
                        }
                    });
        }
    }


}
