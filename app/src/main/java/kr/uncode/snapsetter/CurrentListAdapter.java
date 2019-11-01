package kr.uncode.snapsetter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.realm.RealmResults;
import kr.uncode.snapsetter.databinding.CurrentItemImageBinding;

public class CurrentListAdapter extends RecyclerView.Adapter<CurrentListAdapter.CurrentListViewHolder> {

    private OnAdapterItemClickListener onAdapterItemClickListener;

    private RealmResults<CurrentUserPicData> getImageList;
    private Context context;

    //생성장
    public CurrentListAdapter(RealmResults<CurrentUserPicData> all) {
        getImageList = all;

    }

    public void setOnAdapterItemClickListener(OnAdapterItemClickListener itemClickListener) {
        onAdapterItemClickListener = itemClickListener;
    }


    @NonNull
    @Override
    public CurrentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CurrentItemImageBinding binding = CurrentItemImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        binding.getRoot().setOnClickListener(view ->
                onAdapterItemClickListener.onAdapterViewClick(view));
        context = parent.getContext();


        return new CurrentListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentListViewHolder holder, int position) {

        try {
            Glide.with(context.getApplicationContext()).load(
                    getImageList.get(position).getCurrent_url())
                    .override(150,150)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.binding.ivImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return getImageList.size();
    }

    class CurrentListViewHolder extends RecyclerView.ViewHolder {
        CurrentItemImageBinding binding;

        public CurrentListViewHolder(CurrentItemImageBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }


}

interface OnAdapterItemClickListener {
    void onAdapterViewClick(View view);
}

