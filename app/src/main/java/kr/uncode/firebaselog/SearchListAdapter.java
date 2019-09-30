package kr.uncode.firebaselog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import kr.uncode.firebaselog.databinding.ListItemImageBinding;


public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.SearchListViewHolder> {

    private final List<RetrofitResponse.Documents> data = new ArrayList<>();
    private OnAdapterItemClickListener onAdapterItemClickListener;


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
    public SearchListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ListItemImageBinding binding = ListItemImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        binding.getRoot().setOnClickListener(v -> onAdapterItemClickListener.onAdapterViewClick(v));

        return new SearchListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchListAdapter.SearchListViewHolder holder, int position) {

        RetrofitResponse.Documents documents = data.get(position);
        Glide.with(holder.binding.getRoot()).load(documents.image_url).into(holder.binding.ivImage);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class SearchListViewHolder extends RecyclerView.ViewHolder {

        ListItemImageBinding binding;


        public SearchListViewHolder(@NonNull ListItemImageBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    interface OnAdapterItemClickListener {
        void onAdapterViewClick(View view);
    }
}