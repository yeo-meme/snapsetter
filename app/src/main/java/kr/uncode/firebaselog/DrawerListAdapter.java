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

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import kr.uncode.firebaselog.databinding.DrawerItemImageBinding;


public class DrawerListAdapter extends RecyclerView.Adapter<DrawerListAdapter.DrawerListViewHolder> {

    public RealmChangeListener realmChangeListener;
    private static String KEY_IMAGE_POSITION = "PIE";
    private static String KEY_IMAGE_URL = "YEOMEME";
    private RealmResults<PictureData> getImageList;
    private OnAdapterItemClickListener onAdapterItemClickListener;
    private Context context;
    private String userEmail = "";
    private String image_url = "";
    private int image_position ;


    public void listDelete() {

        final Realm realm = Realm.getDefaultInstance();
        RealmResults<PictureData> data = realm.where(PictureData.class).findAllAsync();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                getImageList = realm.where(PictureData.class).findAll();

                for (int i =0; i<getImageList.size(); i++) {
                    if ( i == image_position) {
                        PictureData data = getImageList.get(image_position);
                        data.deleteFromRealm();
                    }
                }
            }
        });

        realmChangeListener = new RealmChangeListener() {
            @Override
            public void onChange(Object o) {
                notifyDataSetChanged();
            }
        };

        data.addChangeListener(realmChangeListener);

    }
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

//        holder.binding.name.setText(getImageList.get(position).getImage_url());
        Glide.with(context.getApplicationContext()).load(getImageList.get(position).getImage_url()).into(holder.binding.ivImage);
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

            binding.ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    image_position = position;
                    String xx = getImageList.get(position).getImage_url();

                    if (xx != null) {
                        //드로어디테일액티비티가 리스트를 구성하는 리스트 보관함
                        Intent intent = new Intent(context.getApplicationContext(),DrawerDetailActivity.class);
                        intent.putExtra(KEY_IMAGE_URL,xx);
                        intent.putExtra(KEY_IMAGE_POSITION,position);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }


    interface OnAdapterItemClickListener {
        void onAdapterViewClick(View view);
    }
}
