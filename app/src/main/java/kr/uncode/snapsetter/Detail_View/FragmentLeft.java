package kr.uncode.snapsetter.Detail_View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import kr.uncode.snapsetter.R;

public class FragmentLeft extends Fragment {


    public static FragmentLeft newInstance() {
        return new FragmentLeft();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_left,container,false);
    }
}
