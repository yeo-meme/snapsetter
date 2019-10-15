package kr.uncode.firebaselog;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import kr.uncode.firebaselog.databinding.DrawerItemImageBinding;
import kr.uncode.firebaselog.databinding.ListItemImageBinding;

public class DrawerListAdapter extends RecyclerView.Adapter<DrawerListAdapter.DrawerListViewHolder>  {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private final List<RetrofitResponse.Documents> data = new ArrayList<>();
    private OnAdapterItemClickListener onAdapterItemClickListener;
    private Context context;
    private String userEmail="";
    private String image_url="";

    //----------------------------------------------------------------더미데이터

    private ArrayList<DermyData> mDataset;


    private String word;
    private String meaning;


    public DrawerListAdapter(ArrayList<DermyData> mData) {
        this.mDataset = mData;
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
    public DrawerListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drawer_item_image,parent,false);

//        DrawerItemImageBinding binding = DrawerItemImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
//        binding.getRoot().setOnClickListener(v -> onAdapterItemClickListener.onAdapterViewClick(v));
//
//        context = parent.getContext();

//        return new DrawerListViewHolder(binding);

        return new DrawerListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DrawerListViewHolder holder, int position) {


            holder.name.setText(mDataset.get(position).getName());
//        RetrofitResponse.Documents documents = data.get(position);
//        Glide.with(holder.binding.getRoot()).load(documents.image_url).into(holder.binding.ivImage);


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    class DrawerListViewHolder extends RecyclerView.ViewHolder {
        public TextView name, lat;
//        DrawerItemImageBinding binding;

        public DrawerListViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
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
