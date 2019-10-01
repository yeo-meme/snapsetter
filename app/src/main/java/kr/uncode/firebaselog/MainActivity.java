package kr.uncode.firebaselog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private Button loginBtn;
    private Button createIdBtn;


    private TextInputLayout passwdedit;
    private TextInputLayout  emailedit;

    private String email = "";
    private String passwd = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        
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
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this,"로그인 성공!",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
                                startActivity(intent);
                                finish();

                                ///인텐드가 들어갈 자리 데스용
                            } else {
                                Toast.makeText(MainActivity.this,"아이디와 비밀번호를 확인해주세요",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}
