package kr.uncode.firebaselog;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import kr.uncode.firebaselog.databinding.ListItemImageBinding;


public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.SearchListViewHolder> {

    private final List<RetrofitResponse.Documents> data = new ArrayList<>();
    private OnAdapterItemClickListener onAdapterItemClickListener;
    private Context context;
    public static final String EXTRA_KEY_IMAGE_URL = "EXTRA_KEY_IMAGE_URL";
    public String url = "";


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

        context = parent.getContext();
        return new SearchListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchListAdapter.SearchListViewHolder holder, int position) {


        RetrofitResponse.Documents documents = data.get(position);
        Glide.with(holder.binding.getRoot()).load(documents.image_url).into(holder.binding.ivImage);


        setOnAdapterItemClickListener(new OnAdapterItemClickListener() {
            @Override
            public void onAdapterViewClick(View view) {
                Log.d("hi","hi");
                url = documents.image_url;

                Log.d("image",url);
                Intent intent = new Intent(context.getApplicationContext(),DetailActivity.class);
                intent.putExtra(EXTRA_KEY_IMAGE_URL,url);
                Log.d(EXTRA_KEY_IMAGE_URL,url);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class SearchListViewHolder extends RecyclerView.ViewHolder {

        ListItemImageBinding binding;


         SearchListViewHolder(ListItemImageBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    interface OnAdapterItemClickListener {
        void onAdapterViewClick(View view);
    }
}