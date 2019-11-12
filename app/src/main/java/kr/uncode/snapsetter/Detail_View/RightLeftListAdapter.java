package kr.uncode.snapsetter.Detail_View;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import kr.uncode.snapsetter.RetrofitResponse;

public class RightLeftListAdapter extends RecyclerView.Adapter<RightLeftListAdapter.RightLeftViewHolder> {
    private final List<RetrofitResponse.Documents> mRestApiImageData = new ArrayList<>();
    private Realm mRealm;

    public RightLeftListAdapter(Realm realm) {
        this.mRealm = realm;
    }
    @NonNull
    @Override
    public RightLeftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RightLeftViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mRestApiImageData.size();
    }

    class RightLeftViewHolder extends RecyclerView.ViewHolder {
         public RightLeftViewHolder(@NonNull View itemView) {
             super(itemView);
         }
     }
}
