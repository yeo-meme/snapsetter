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

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import kr.uncode.firebaselog.databinding.ListItemImageBinding;


public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.SearchListViewHolder> {
    int so = 0;

    private PicActivity pic;

    private String name ="" ;
    private String image_url = "";
    private Realm realm;
    private final List<RetrofitResponse.Documents> data = new ArrayList<>();
    private OnAdapterItemClickListener onAdapterItemClickListener;
    private Context context;
    public static final String EXTRA_KEY_IMAGE_URL = "EXTRA_KEY_IMAGE_URL";
    public String url = "";
//    public RetrofitResponse.Documents documents;

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


        RetrofitResponse.Documents documents = data.get(position);
        Glide.with(holder.binding.getRoot()).load(documents.image_url).into(holder.binding.ivImage);


//        setOnAdapterItemClickListener(new OnAdapterItemClickListener() {
//            @Override
//            public void onAdapterViewClick(View view) {
//
//                Log.d("hi","hi");
//                //온어탭터뷰클릭 순간에 Url을 담는다
//                url = documents.image_url;
//
//                Log.d("image",url);
//                //그 URL을 Intent 에 담아서 디테일 액티비티로 보낸다
//                if (url != null) {
//                    Intent intent = new Intent(context.getApplicationContext(),DeleteActivity.class);
//                    intent.putExtra(EXTRA_KEY_IMAGE_URL,url);
//                    Log.d(EXTRA_KEY_IMAGE_URL,url);
//                    context.startActivity(intent);
//                }
//
//            }
//        });

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

            binding.heartArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    String img_url = data.get(position).image_url;

                    picActivity.savePic(img_url);
                }
            });

            itemView.area.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    return;
                }
            });

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
                            Intent intent = new Intent(context.getApplicationContext(), DrawerDetailActivity.class);
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