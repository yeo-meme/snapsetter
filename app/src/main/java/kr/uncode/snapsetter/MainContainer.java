package kr.uncode.snapsetter;

import android.content.Context;
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

import org.w3c.dom.Text;

public class MainContainer extends Fragment {

    private CheckBox checkBox;
    private String email = "";
    private String passwd = "";

    private String recentUser = "";
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener mAuthListener;


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
        mAuth = FirebaseAuth.getInstance();
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_fragment,container,false);
        putEmailEdit = view.findViewById(R.id.putEmailEdit);
        loginBtn = view.findViewById(R.id.loginBtn);
        createIdBtn = view.findViewById(R.id.createIdBtn);

        emailedit = view.findViewById(R.id.emailedit);
        passwdedit = view.findViewById(R.id.passwdedit);

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
        return view;
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
                            replaceFragment(Search_Fragment.newInstance());
                            Log.d("cc", "코드수정후 변화확인");
                        }
                    }
                });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null).commit();
    }


    private void editTextgetToString() {
        email = emailedit.getEditText().getText().toString();
        passwd = passwdedit.getEditText().getText().toString();
    }


}
