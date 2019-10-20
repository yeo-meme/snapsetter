package kr.uncode.firebaselog;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
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


    private RealmHelper pic;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String name = "";
    private String image_url = "";
    private Realm realm;
    private final List<RetrofitResponse.Documents> data = new ArrayList<>();
    private OnAdapterItemClickListener onAdapterItemClickListener;
    private Context context;
    public static final String EXTRA_KEY_IMAGE_URL = "EXTRA_KEY_IMAGE_URL";
    public String url = "";
//    public RetrofitResponse.Documents documents;

    //    private String img_url;
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

        return new SearchListViewHolder(binding,this);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchListAdapter.SearchListViewHolder holder, int position) {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        String recentUser = currentUser.getEmail();

        Log.d("hh", "사용자 이메일은 " + recentUser);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<PictureData> kk = realm.where(PictureData.class).equalTo("name", recentUser).findAll();
        RetrofitResponse.Documents documents = data.get(position);

        Log.d("vv", String.valueOf(kk));
        if (kk == null|| kk.size() == 0 ) {
            holder.binding.eheart.setImageResource(R.drawable.eheart);
        } else {
            for (int k = 0; k < kk.size(); k++) {
                PictureData dd = kk.get(k);
                Log.d("cc", String.valueOf(dd));
                Log.d("yy", "db url : " + dd.getImage_url());
                Log.d("yy", "bind url : " + documents.image_url);
                if (dd.getImage_url().equals(documents.image_url)) {
                    holder.binding.eheart.setImageResource(R.drawable.heart);
                } else if (!dd.getImage_url().equals(documents.image_url)) {
                    holder.binding.eheart.setImageResource(R.drawable.eheart);
                }
            }

        }
        Glide.with(holder.binding.getRoot()).load(documents.image_url).into(holder.binding.ivImage);

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    static class SearchListViewHolder extends RecyclerView.ViewHolder {
        RealmHelper realmHelper = new RealmHelper();
        ListItemImageBinding binding;
        boolean checkClick = false;


        // 하트를 눌린 순간 하트를 누린 이미지를 저장하는 메서드
        SearchListViewHolder(ListItemImageBinding itemView, SearchListAdapter searchListAdapter) {
            super(itemView.getRoot());
            binding = itemView;

            // 하트를 클릭하면 내보관함에 사진을 리스트로 뿌리기 위해
            //이미지 url을 DB에 따로 저장하기 위해 액티비티 안에 메서드로 저장작업을 분리함


            binding.clickheart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FirebaseAuth mAuth;
                    FirebaseUser currentUser;

                    mAuth = FirebaseAuth.getInstance();
                    currentUser = mAuth.getCurrentUser();
                    String recentUser = currentUser.getEmail();

                    //지금 사용자가 클릭한 이미지
                    int position = getAdapterPosition();
                    final String nowClickImg = searchListAdapter.data.get(position).image_url;

                    Realm realm = Realm.getDefaultInstance();
                    final RealmResults<PictureData> ll = realm.where(PictureData.class).equalTo("name", recentUser).findAll();

                    //보관함에 들어있는 url
                    Log.d("gg", "ll데이터" + ll.toString());

                    if (ll == null || ll.size() == 0) {
                        //없으면 하트주려고
                        saveDbImage(nowClickImg,searchListAdapter);
                    } else {
                        for (int r = 0; r < ll.size(); r++) {
                            PictureData hh = ll.get(r);

                            Log.d("gg", "현재클릭한 이미지주소 " + nowClickImg);
                            Log.d("gg", "디비에서 가져와서 비교할 데이터" + hh.getImage_url());
                            if (hh.getImage_url().equals(nowClickImg)) {
//                                binding.eheart.setImageResource(R.drawable.eheart);
                                Log.d("gg", "같다");
                                realmHelper.delete(nowClickImg);
                                searchListAdapter.notifyDataSetChanged();

                            } else if (!hh.getImage_url().equals(nowClickImg)) {
                                saveDbImage(nowClickImg,searchListAdapter);
                            }
                        }
                    }
                    realm.close();
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
                        RetrofitResponse.Documents documents = searchListAdapter.data.get(position);
                        final String url = documents.image_url;

                        Log.d("image", url);

                        //그 URL을 Intent 에 담아서 디테일 액티비티로 보낸다
                        if (url != null) {
                            Intent intent = new Intent(view.getContext() , ViewTwoStepActivity.class);
                            intent.putExtra(EXTRA_KEY_IMAGE_URL, url);
                            Log.d(EXTRA_KEY_IMAGE_URL, url);
                            view.getContext().startActivity(intent);
                        }

                    }
                }
            });


        }

        /**
         * 하트 클릭이벤트 체인저의 URL파라미터를 DB에 저장하는 메서드
         */
        private void saveDbImage(String nowClickImg,SearchListAdapter searchListAdapter) {
            Log.d("gg", "다르다");
            realmHelper.savePic(nowClickImg);
            searchListAdapter.notifyDataSetChanged();

        }
    }

    interface OnAdapterItemClickListener {
        void onAdapterViewClick(View view);
    }
}