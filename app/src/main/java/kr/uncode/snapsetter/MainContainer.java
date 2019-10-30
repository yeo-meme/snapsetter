package kr.uncode.snapsetter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class MainContainer extends Fragment {

    private CheckBox checkBox;
    private String email = "";
    private String passwd = "";

    private String recentUser = "";
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener mAuthListener;


    /**
     * 그 프리페어런스 저장 삭제를 위해 에디터 선언
     */
    private SharedPreferences.Editor editor;

    /**
     * 사용자가 저장한 이메일과 비밀번호를 저장하기
     * 위해 프리페어런스 선언
     */
    private SharedPreferences sharedPreferences;


    private TextInputLayout passwdedit;
    private TextInputLayout emailedit;
    private TextInputEditText putEmailEdit;
    private TextInputEditText putPassEdit;


    private Button loginBtn;
    private Button createIdBtn;
    private Context context;

    public static MainContainer newInstance() {
        return new MainContainer();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         context = getContext();
    }

    @Override
    public void onStart() {
        Activity activity = getActivity();
        if (activity != null && activity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) activity;
            mainActivity.removeToolbar(false);
            Log.d("dd", "난 툴바를 지우러 갈꺼음");
        }
        mAuth = FirebaseAuth.getInstance();
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_fragment,container,false);

        putEmailEdit = view.findViewById(R.id.putEmailEdit);
        putPassEdit = view.findViewById(R.id.putPassEdit);
        loginBtn = view.findViewById(R.id.loginBtn);
        createIdBtn = view.findViewById(R.id.createIdBtn);

        emailedit = view.findViewById(R.id.emailedit);
        passwdedit = view.findViewById(R.id.passwdedit);
        checkBox = view.findViewById(R.id.checkbox);
        checkBoxOnClick();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //에딧텍스트에 내용을 toString으로 가져온다 !
                editTextgetToString();

                // 에딧텍스트가 비워져 있으면 아래 토스트로 사용자에게 알림!
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(passwd)) {
                    Toast.makeText(context, "이메일 OR 비밀번호를 입력해주세요", Toast.LENGTH_LONG).show();
                } else {

                    //비워져 있지 않으면 로그인 프로게스
                    loginUser();
                }

                // 투스트링으로 전역변수에 값이 잘 담겼는지 로그확인
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

        return view;
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

    //체크박스 체크하면 아이디,비밀번호 값을 프리페얼런스로 값을 저장해서 에딧창에 담고 있는 메서드
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


    //로그인이 처음에 안된 사용자가 로그인을 시도를 성공했을때 서치_프래그먼트를 리플레이스
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
                            //에러후 새로 옮긴 프래그먼트로 이동
                            replaceFragment(SearchingFragment.newInstance());
                            Log.d("cc", "코드수정후 변화확인");
                        }
                    }
                });
    }

//회원가입 버튼을 누르면 파이어베이스 인증을 걸쳐 회원가입 하는 메서드
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

    //리플레이스 프래그먼트를 연속적으로 페이지 리플레이스 할떄 프래그먼트 스택을 쌓고 재배치하는 메서드
    public void replaceFragment(Fragment fragment) {
        if(isAdded()) {
            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }


    //머티리얼 디자인 에딧텍스트의 값을 가져오져 오는 메서드
    private void editTextgetToString() {
        email = emailedit.getEditText().getText().toString();
        passwd = passwdedit.getEditText().getText().toString();
    }


}
