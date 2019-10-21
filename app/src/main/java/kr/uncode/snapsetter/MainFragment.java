package kr.uncode.snapsetter;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class MainFragment extends Fragment {

    /**
     * 파이어베이스 객체에서 유저Email을 받아 스트링으로 받는 변수
     */
    private String recentUser = "";
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Context context;

    private Button loginBtn;
    private Button createIdBtn;

    private TextInputLayout passwdedit;
    private TextInputLayout  emailedit;


    private String email = "";
    private String passwd = "";
    private Object Context;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context = context;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

     private void onClickEvent(){
         loginBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 email = emailedit.getEditText().getText().toString();
                 passwd = passwdedit.getEditText().getText().toString();

                 if (TextUtils.isEmpty(email) || TextUtils.isEmpty(passwd)) {
                     Toast.makeText(context,"이메일 OR 비밀번호를 입력해주세요",Toast.LENGTH_LONG).show();
                 } else {
                     loginUser();
                 }
                 Log.d("hi",email);
                 Log.d("hi",passwd);
             }
         });
         createIdBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 if (email != null && passwd != null)
                     email = emailedit.getEditText().getText().toString();
                 passwd = passwdedit.getEditText().getText().toString();

                 Log.d("hi",email);
                 Log.d("hi",passwd);

                 createUser(email,passwd);
             }
         });
     }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        context = container.getContext();
        View rootView = inflater.inflate(R.layout.main_fragment, container,false);

        loginBtn = rootView.findViewById(R.id.loginBtn);
        createIdBtn = rootView.findViewById(R.id.createIdBtn);
        passwdedit = rootView.findViewById(R.id.passwdedit);
        emailedit = rootView.findViewById(R.id.emailedit);


        onClickEvent();

        return rootView;
    }

    private void loginUser() {
            mAuth.signInWithEmailAndPassword(email,passwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException e) {
                                    Toast.makeText(context,"존재하지 않는 id 입니다." ,Toast.LENGTH_SHORT).show();
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    Toast.makeText(context,"이메일 형식이 맞지 않습니다." ,Toast.LENGTH_SHORT).show();
                                } catch (FirebaseNetworkException e) {
                                    Toast.makeText(context,"Firebase NetworkException" ,Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(context,"Exception" ,Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(context, "환영합니다! 원하시는 이미지를 검색하고 나만의이미지를 수집해보세요",Toast.LENGTH_SHORT).show();
                                ((MainActivity)getActivity()).replaceFragment(SearchFragment.newInstance());
                                Log.d("cc","코드수정후 변화확인");
                            }
                        }
                    });
        }

    private void createUser(String email, String passwd) {

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(passwd)) {
            Toast.makeText(context,"이메일과 비밀번호를 확인해주세요",Toast.LENGTH_LONG).show();
        } else {

            mAuth.createUserWithEmailAndPassword(email,passwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {

                                Toast.makeText(context,"회원가입성공",Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context,"회원가입 실패",Toast.LENGTH_LONG).show();
                            }


                        }
                    });
        }
    }

}

