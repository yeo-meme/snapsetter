package kr.uncode.firebaselog;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import kr.uncode.firebaselog.databinding.ListItemImageBinding;


public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.SearchListViewHolder> {
    int so = 0;

    private PicActivity pic;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String name ="" ;
    private String image_url = "";
    private Realm realm;
    private final List<RetrofitResponse.Documents> data = new ArrayList<>();
    private OnAdapterItemClickListener onAdapterItemClickListener;
    private Context context;
    public static final String EXTRA_KEY_IMAGE_URL = "EXTRA_KEY_IMAGE_URL";
    public String url = "";
//    public RetrofitResponse.Documents documents;

    private String img_url;
    public SearchListAdapter() {
    }


    public void setOnAdapterItemClickListener(OnAdapterItemClickListener itemClickListener) {
        onAdapterItemClickListener = itemClickListener;
    }


    public void addData(RetrofitResponse.Documents documents) {
        data.add(documents);
        notifyDataSetChanged();
    }


    public void addDataAll(List<RetrofitResponse.Documents> dataList) {
        data.clear();
        data.addAll(dataList);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public SearchListViewHolder onCreateViewHolder(@NonNull ViewGroup xx, int viewType) {

        ListItemImageBinding binding = ListItemImageBinding.inflate(LayoutInflater.from(xx.getContext()), xx, false);
        binding.getRoot().setOnClickListener(v -> onAdapterItemClickListener.onAdapterViewClick(v));

        context = xx.getContext();

        return new SearchListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchListAdapter.SearchListViewHolder holder, int position) {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        String recentUser = currentUser.getEmail();

        Log.d("hh", "사용자 이메일은 " + recentUser );
        Realm realm = Realm.getDefaultInstance();
        RealmResults<PictureData> kk = realm.where(PictureData.class).equalTo("name", recentUser).findAll();
        RetrofitResponse.Documents documents = data.get(position);

        Log.d("vv", String.valueOf(kk));
        for (int k =0; k < kk.size(); k++) {
            PictureData dd = kk.get(k);
            Log.d("cc", String.valueOf(dd));
            Log.d("yy", "db url : "+dd.getImage_url());
            Log.d("yy","bind url : "+documents.image_url);
                if (dd.getImage_url().equals(documents.image_url)) {
                    holder.binding.eheart.setImageResource(R.drawable.heart);
                } else if (!dd.getImage_url().equals(documents.image_url)) {
                    holder.binding.eheart.setImageResource(R.drawable.eheart);
                }
        }
        Glide.with(holder.binding.getRoot()).load(documents.image_url).into(holder.binding.ivImage);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class SearchListViewHolder extends RecyclerView.ViewHolder {
        PicActivity picActivity = new PicActivity();

        ListItemImageBinding binding;


        // 하트를 눌린 순간 하트를 누린 이미지를 저장하는 메서드
        SearchListViewHolder(ListItemImageBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            // 하트를 클릭하면 내보관함에 사진을 리스트로 뿌리기 위해
            //이미지 url을 DB에 따로 저장하기 위해 액티비티 안에 메서드로 저장작업을 분리함

            binding.heartArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                   img_url = data.get(position).image_url;
                    picActivity.savePic(img_url);

                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<PictureData> ll = realm.where(PictureData.class).equalTo("image_url",img_url).findAll();

                    for (int r=0; r<ll.size(); r++) {
                        PictureData hh = ll.get(r);

                        Log.d("gg",img_url);
                            if (hh.getImage_url().equals(img_url)) {
                                binding.eheart.setImageResource(R.drawable.eheart);
                                Log.d("gg","같다");
                                picActivity.delete(img_url);
                                data.remove(position);
                                notifyDataSetChanged();
                            } else if (!hh.getImage_url().equals(img_url)) {
                                binding.eheart.setImageResource(R.drawable.heart);
                                Log.d("gg","다르다");
                                picActivity.savePic(image_url);
                                notifyDataSetChanged();

                            }
                    }
                }
            });


            // 이미지 사진영역을 클릭하면 자세히 보기 화면으로 새액티비티가 나타나
            // 리스트가 아닌 싱글로 사진을 보여준다
            binding.ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (binding.ivImage != null) {

                        Log.d("hi", "hi");
                        //온어탭터뷰클릭 순간에 Url을 담는
                        int position = getAdapterPosition();
                        RetrofitResponse.Documents documents = data.get(position);
                        url = documents.image_url;

                        Log.d("image", url);


                        //그 URL을 Intent 에 담아서 디테일 액티비티로 보낸다
                        if (url != null) {
                            Intent intent = new Intent(context.getApplicationContext(), ViewTwoStepActivity.class);
                            intent.putExtra(EXTRA_KEY_IMAGE_URL, url);
                            Log.d(EXTRA_KEY_IMAGE_URL, url);
                            context.startActivity(intent);
                        }

                    }
                }
            });


        }
    }

    interface OnAdapterItemClickListener {
        void onAdapterViewClick(View view);
    }
}