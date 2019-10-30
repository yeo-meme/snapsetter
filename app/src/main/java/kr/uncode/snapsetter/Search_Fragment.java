package kr.uncode.snapsetter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Search_Fragment extends Fragment {


    public static Search_Fragment newInstance() {
        return new Search_Fragment();
    }

    private EditText search_edit_frame;
    private Button searchBtn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.search_fragment,container,false);
        search_edit_frame = view.findViewById(R.id.search_edit_frame);
        searchBtn = view.findViewById(R.id.searchBtn);



        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
