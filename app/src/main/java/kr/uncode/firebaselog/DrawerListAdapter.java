package kr.uncode.firebaselog;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;
import kr.uncode.firebaselog.databinding.DrawerItemImageBinding;

public class DrawerListAdapter extends RecyclerView.Adapter<DrawerListAdapter.DrawerListViewHolder>  {


    private final List<RetrofitResponse.Documents> data = new ArrayList<>();
    private RealmResults<PictureData> getImageList;
    private OnAdapterItemClickListener onAdapterItemClickListener;
    private Context context;
    private String userEmail="";
    private String image_url="";
    private String getPic = "";
    //----------------------------------------------------------------더미데이터

    private ArrayList<DermyData> mDataset;


    private String word;
    private String meaning;


    //생성자
    public DrawerListAdapter() {
    }

    // 프래그먼트에서 얻은 이미지 URL을 뿌리기 위해 이미지 주소를 받아서 전역변수에 담는 메소드
    public void  getPic(RealmResults<PictureData> img) {
        if (img != null) {
            getImageList = img;
            Log.d("00", String.valueOf(getImageList));

        }
    }


    public void setOnAdapterItemClickListener(OnAdapterItemClickListener itemClickListener) {
        onAdapterItemClickListener = itemClickListener;
    }

    public void addData(RetrofitResponse.Documents documents) {
        data.add(documents);
        notifyDataSetChanged();
    }


    public void addDataAll(List<RetrofitResponse.Documents> getImageList) {
        data.clear();
        data.addAll(getImageList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DrawerListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.drawer_item_image,parent,false);

        DrawerItemImageBinding binding = DrawerItemImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        binding.getRoot().setOnClickListener(v -> onAdapterItemClickListener.onAdapterViewClick(v));

        context = parent.getContext();

        return new DrawerListViewHolder(binding);

    }



    @Override
    public void onBindViewHolder(@NonNull DrawerListViewHolder holder, int position) {


//        Glide.with(holder.binding.getRoot()).load(getImage).into(holder.binding.ivImage);


//            holder.name.setText(mDataset.get(position).getName());
//        RetrofitResponse.Documents documents = data.get(position);
        Glide.with(holder.binding.getRoot()).load(getPic).into(holder.binding.ivImage);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class DrawerListViewHolder extends RecyclerView.ViewHolder {
//        DrawerItemImageBinding binding;
            DrawerItemImageBinding binding;

        public DrawerListViewHolder(DrawerItemImageBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
//            name = view.findViewById(R.id.name);
        }}

//        public DrawerListViewHolder(@NonNull DrawerItemImageBinding itemView) {
//            super(itemView.getRoot());
//            binding = itemView;
//        }
//    }


    interface OnAdapterItemClickListener {
        void onAdapterViewClick(View view);
    }
}
