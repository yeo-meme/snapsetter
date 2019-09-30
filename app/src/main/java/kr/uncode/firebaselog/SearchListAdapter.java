package kr.uncode.firebaselog;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.SearchListViewHolier> {

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
    public SearchListViewHolier onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchListAdapter.SearchListViewHolier holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class SearchListViewHolier extends RecyclerView.ViewHolder {



        public SearchListViewHolier(@NonNull View itemView) {
            super(itemView);
        }
    }

    interface OnAdapterItemClickListener {
        void onAdapterViewClick(View view);
    }
}