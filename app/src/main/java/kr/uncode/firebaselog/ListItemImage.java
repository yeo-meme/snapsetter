package kr.uncode.firebaselog;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ListItemImage extends AppCompatActivity  {

    private ImageView eHeart;
    private RelativeLayout heartArea;
    private LinearLayout area;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.list_item_image);
        eHeart = findViewById(R.id.eheart);


        heartArea.bringToFront() ;
        area = findViewById(R.id.area);

        heartArea = findViewById(R.id.clickheart);


//        area.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (area != null) {
//
//                    return;
//                } else {
//                    return;
//                }
//            }
//        });


    }


}
