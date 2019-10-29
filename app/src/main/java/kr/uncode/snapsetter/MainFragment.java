package kr.uncode.snapsetter;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.LinearGradient;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
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

    private String fred = "fred";
    private TextInputLayout passwdedit;
    private TextInputLayout emailedit;

    private CheckBox checkBox;
    private String email = "";
    private String passwd = "";

    /**
     * 사용자가 저장한 이메일과 비밀번호를 저장하기
     * 위해 프리페어런스 선언
     */
    private SharedPreferences sharedPreferences;
    /**
     * 그 프리페어런스 저장 삭제를 위해 에디터 선언
     */
    private SharedPreferences.Editor editor;


    /**
     * 머리티얼 디자인중 텍스트 가져오지게 머티리얼 layout으로 되어있는데
     * 셋 텍스트할떄는 layout이 셋이 안되서 layout 안에 있는 텍스트 에디터 를 뻄
     */
    private TextInputEditText putPassEdit;
    /**
     * 머리티얼 디자인중 텍스트 가져오지게 머티리얼 layout으로 되어있는데
     * 셋 텍스트할떄는 layout이 셋이 안되서 layout 안에 있는 텍스트 에디터 를 뻄
     */
    private TextInputEditText putEmailEdit;


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onStart() {
        Activity activity = getActivity();
        if (activity != null && activity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) activity;
            mainActivity.removeToolbar(false);
            Log.d("dd", "난 툴바를 지우러 갈꺼음");
        }

        super.onStart();
    }


    private void onClickEvent() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editTextgetToString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(passwd)) {
                    Toast.makeText(context, "이메일 OR 비밀번호를 입력해주세요", Toast.LENGTH_LONG).show();
                } else {
                    loginUser();
                }
                Log.d("hi", email);
                Log.d("hi", passwd);
            }
        });
        createIdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email != null && passwd != null)
                    createUser(email, passwd);
            }
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null).commit();
    }

    public void replaceFragmentNoStack(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void editTextgetToString() {
        email = emailedit.getEditText().getText().toString();
        passwd = passwdedit.getEditText().getText().toString();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        context = getContext();

        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        loginBtn = rootView.findViewById(R.id.loginBtn);
        createIdBtn = rootView.findViewById(R.id.createIdBtn);
        passwdedit = rootView.findViewById(R.id.passwdedit);
        emailedit = rootView.findViewById(R.id.emailedit);

        checkBox = rootView.findViewById(R.id.checkbox);

        putPassEdit = rootView.findViewById(R.id.putPassEdit);
        putEmailEdit = rootView.findViewById(R.id.putEmailEdit);
//        getPf();
        onClickEvent();
        //체크박스를 클릭시 에딧내용을 저장하는 메서드
        checkBoxOnClick();


        return rootView;
    }

    private void checkBoxOnClick() {
        sharedPreferences = context.getSharedPreferences("idpw", android.content.Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkBox.isChecked()) {
                    editTextgetToString();
                    if (email != null && passwd != null) {
                        editor.putString("email_id", email);
                        Log.d("ee", "email put check : " + email);
                        editor.putString("pass", passwd);
                        editor.putBoolean("check", checkBox.isChecked());
                        editor.apply();

                        //체크값이 잘들어갔나 확인차 값을 빼봄
                        boolean value = sharedPreferences.getBoolean("check", false);
                        Log.d("hhh", "check box boolean 저장 후 : " + value);
                    } else {
                        Toast.makeText(context.getApplicationContext(), "Email 과 패스워드를 입력해주세요", Toast.LENGTH_LONG);
                    }

                    //사용자가 체크를 풀었을때 프리페어런스를 삭제하고 에딧텍스트를 지우기
                } else if (!checkBox.isChecked()) {
                    editor.remove("email_id");
                    editor.remove("paa");
                    editor.remove("check");
                    putEmailEdit.setText("");
                    putPassEdit.setText("");
                }
            }
        });
    }

    private void getPf() {
        Log.d("tt", "저장후 로그인 시도 들어옴");
        SharedPreferences SharedPreferences = context.getSharedPreferences("idpw", android.content.Context.MODE_PRIVATE);
        String valueId = SharedPreferences.getString("email_id", "");
        String valuePw = SharedPreferences.getString("pass", "");
        if (!valueId.isEmpty() && !valuePw.isEmpty()) {
            putEmailEdit.setText(valueId);
            putPassEdit.setText(valuePw);
            checkBox.setChecked(SharedPreferences.getBoolean("check", false));
        }
    }

    private void loginUser() {
        mAuth.signInWithEmailAndPassword(email, passwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                Toast.makeText(context, "존재하지 않는 id 입니다.", Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(context, "이메일 형식이 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                            } catch (FirebaseNetworkException e) {
                                Toast.makeText(context, "Firebase 오류", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(context, "네트워크 상태를 확인해주세요", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "환영합니다! 원하시는 이미지를 검색하고 나만의이미지를 수집해보세요", Toast.LENGTH_SHORT).show();
                            replaceFragment(SearchFragment.newInstance());
//                                Fragment currentFragment = MainActivity.manager.findFragmentById(R.id.container);
//                            // 이동버튼 클릭할 때 stack에 push
//                            // 이동버튼 클릭할 때 stack에 push
////                                MainActivity.fragmentStack.push(currentFragment);
////                                MainActivity.changeFragment(MainActivity.FRAGMENT_SEARCH);
                            Log.d("cc", "코드수정후 변화확인");
                        }
                    }
                });
    }

    private void createUser(String email, String passwd) {

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(passwd)) {
            Toast.makeText(context, "이메일과 비밀번호를 확인해주세요", Toast.LENGTH_LONG).show();
        } else {

            mAuth.createUserWithEmailAndPassword(email, passwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(context, "회원가입성공", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, "회원가입 실패", Toast.LENGTH_LONG).show();
                            }


                        }
                    });
        }
    }


    //뒤로가기 버튼을 뺏어올 리스너 등록

    //메인에서 토스트를 띄우며 종료확인을 하기 위해 필드선언
    MainFragment mainFragment = new MainFragment();


}
