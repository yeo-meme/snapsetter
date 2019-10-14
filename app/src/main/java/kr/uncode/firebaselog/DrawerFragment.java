package kr.uncode.firebaselog;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DrawerFragment  extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private MainActivity mainActivity;


    public static DrawerFragment newInstance() {
        return new DrawerFragment();
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mainActivity = (MainActivity) getActivity();

    }


    @Override
    public void onDetach() {
        super.onDetach();

        mainActivity = null;
    }

    @Override
    public void onStart() {
        super.onStart();
//        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            Log.d("ddd","ddd");
//            ((MainActivity)getActivity()).replaceFragment(DrawerFragment.newInstance());

//            startActivity(new Intent(context,SearchActivity.class));
//            onDestroy();
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_drawer, container,false);
        return rootView;


    }
}
