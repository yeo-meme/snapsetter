package kr.uncode.firebaselog;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import io.realm.RealmResults;
import kr.uncode.firebaselog.databinding.DrawerItemImageBinding;


public class DrawerListAdapter extends RecyclerView.Adapter<DrawerListAdapter.DrawerListViewHolder> {


    private RealmResults<PictureData> getImageList;
    private OnAdapterItemClickListener onAdapterItemClickListener;
    private Context context;
    private String userEmail = "";
    private String image_url = "";


    //생성자
    public DrawerListAdapter(RealmResults<PictureData> all) {
        getImageList = all;
    }

    public void setOnAdapterItemClickListener(OnAdapterItemClickListener itemClickListener) {
        onAdapterItemClickListener = itemClickListener;
    }


    @NonNull
    @Override
    public DrawerListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        DrawerItemImageBinding binding = DrawerItemImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        binding.getRoot().setOnClickListener(v -> onAdapterItemClickListener.onAdapterViewClick(v));
        context = parent.getContext();

        return new DrawerListViewHolder(binding);

    }


    @Override
    public void onBindViewHolder(@NonNull DrawerListViewHolder holder, int position) {

        holder.binding.name.setText(getImageList.get(position).getImage_url());
        Glide.
        Log.d("11",getImageList.toString());

    }

    @Override
    public int getItemCount() {
        return getImageList.size();
    }


    class DrawerListViewHolder extends RecyclerView.ViewHolder {
        DrawerItemImageBinding binding;

        public DrawerListViewHolder(DrawerItemImageBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }


    interface OnAdapterItemClickListener {
        void onAdapterViewClick(View view);
    }
}
