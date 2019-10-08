package kr.uncode.firebaselog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private TextInputLayout  emailedit;

    private String email = "";
    private String passwd = "";

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private AppBarConfiguration mAppBarConfiguration;

    private ActionBarDrawerToggle toggle;

    private LinearLayout main_layout;

    private static int drawer_open = 123;
    private static int drawer_close = -123;

    //구글로그인 result 상수
    private static final int RC_SIGN_IN = 900;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawer = findViewById(R.id.drawer);


        navigationView = findViewById(R.id.nav_view);
        main_layout = findViewById(R.id.main_layout);


        toggle = new ActionBarDrawerToggle(this,drawer, navigation_drawer_open,R.string.navigation_drawer_close);


        drawer.addDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(this);
        toggle.syncState();
//
        navigationView.setNavigationItemSelectedListener(this);
//       navigationView mAppBarConfiguration = new AppBarConfiguration.Builder(
//
//        ).setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this,R.id.main_layout);
//        NavigationUI.setupActionBarWithNavController(this,navController,mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView,navController);

//        getActionBar().setDisplayHomeAsUpEnabled(true);
//
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle("SNAP SETTER");



//
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                    Snackbar.make(view,"action",Snackbar.LENGTH_LONG)
//                            .setAction("Action!!",null).show();
//
//
//                    DrawerLayout drawer = findViewById(R.id.drawer_layout);
//                NavigationView navigationView = findViewById(R.id.nav_view);
//
//
//
//            }
//        });


        loginBtn = findViewById(R.id.loginBtn);
        createIdBtn = findViewById(R.id.createIdBtn);
        passwdedit = findViewById(R.id.passwdedit);
        emailedit = findViewById(R.id.emailedit);


        mAuth = FirebaseAuth.getInstance();
        createIdBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (view == loginBtn) {
            email = emailedit.getEditText().getText().toString();
            passwd = passwdedit.getEditText().getText().toString();

            Log.d("hi",email);
            Log.d("hi",passwd);

            loginUser();
            Toast.makeText(MainActivity.this, "hi" ,Toast.LENGTH_LONG).show();
        }

        if (view == createIdBtn) {
            email = emailedit.getEditText().getText().toString();
            passwd = passwdedit.getEditText().getText().toString();

            Log.d("hi",email);
            Log.d("hi",passwd);

            createUser(email,passwd);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.account:
                Toast.makeText(this, "item1 clicked..", Toast.LENGTH_SHORT).show();
                break;
            case R.id.star:
                Toast.makeText(this, "item2 clicked..", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cart:
                Toast.makeText(this, "item3 clicked..", Toast.LENGTH_SHORT).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
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
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }


    private void createUser(String email, String passwd) {

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(passwd)) {
            Toast.makeText(MainActivity.this,"이메일과 비밀번호를 확인해주세요",Toast.LENGTH_LONG).show();
        } else {

            mAuth.createUserWithEmailAndPassword(email,passwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {

                                Toast.makeText(MainActivity.this,"회원가입성공",Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(MainActivity.this,"회원가입 실패",Toast.LENGTH_LONG).show();
                            }


                        }
                    });
        }
    }

    private void loginUser() {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(passwd)) {
            Toast.makeText(MainActivity.this,"이메일 OR 비밀번호를 입력해주세요",Toast.LENGTH_LONG).show();

        } else {
            email = emailedit.getEditText().getText().toString();
            passwd = passwdedit.getEditText().getText().toString();

            mAuth.signInWithEmailAndPassword(email,passwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException e) {
                                    Toast.makeText(MainActivity.this,"존재하지 않는 id 입니다." ,Toast.LENGTH_SHORT).show();
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    Toast.makeText(MainActivity.this,"이메일 형식이 맞지 않습니다." ,Toast.LENGTH_SHORT).show();
                                } catch (FirebaseNetworkException e) {
                                    Toast.makeText(MainActivity.this,"Firebase NetworkException" ,Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(MainActivity.this,"Exception" ,Toast.LENGTH_SHORT).show();
                                }

                            }else{


                                currentUser = mAuth.getCurrentUser();

                                Toast.makeText(MainActivity.this, "로그인 성공" + "/" + currentUser.getEmail() + "/" + currentUser.getUid() ,Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                                finish();
                            }




//                            if (task.isSuccessful()) {
//                                Toast.makeText(MainActivity.this,"로그인 성공!",Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
//                                startActivity(intent);
//                                finish();
//
//                                ///인텐드가 들어갈 자리 데스용
//                            } else {
//                                Toast.makeText(MainActivity.this,"아이디와 비밀번호를 확인해주세요",Toast.LENGTH_LONG).show();
//                            }
                        }
                    });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(MainActivity.this,SearchActivity.class));
            finish();
        }
    }


}
