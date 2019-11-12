package kr.uncode.snapsetter.Drawer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import kr.uncode.snapsetter.PictureData;
import kr.uncode.snapsetter.databinding.DrawerItemImageBinding;


public class DrawerListAdapter extends RecyclerView.Adapter<DrawerListAdapter.DrawerListViewHolder> {

    public RealmChangeListener realmChangeListener;
    private static String KEY_IMAGE_POSITION = "PIE";
    private static String KEY_IMAGE_URL = "YEOMEME";
    private RealmResults<PictureData> getImageList;
    private OnAdapterItemClickListener onAdapterItemClickListener;
    private Context context;
    private String userEmail = "";
    private String image_url = "";
    private int image_position;


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
        try {
            Glide.with(context.getApplicationContext()).load(getImageList.get(position).getImage_url())
                    .override(150 ,150)
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


    //찜했을떄
    class DrawerListViewHolder extends RecyclerView.ViewHolder {
        DrawerItemImageBinding binding;

        public DrawerListViewHolder(DrawerItemImageBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;


            binding.ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("xx","사용자 클릭이벤트 시도");
                    int position = getAdapterPosition();
                    image_position = position;
                    if (getImageList.get(position).getImage_url() != null) {
                        String xx = getImageList.get(position).getImage_url();
                        if (xx != null) {
                            //드로어디테일액티비티가 리스트를 구성하는 리스트 보관함

                            Intent intent = new Intent(context.getApplicationContext(), DrawerDetailActivity.class);
                            intent.putExtra(KEY_IMAGE_URL, xx);
                            context.startActivity(intent);
                        }
                    }

                }
            });
            //디테일 뷰로 넘어가는 온클릭 이벤트 액태비티 태스크 혹은 쓰레드 시점문제로 해결되지 않아
            // 다음 업데이트때에 업뎃 예정
            // EGL_BAD_ATTRIBUTE
            //https://gogorchg.tistory.com/entry/Android-activity-has-been-destroyed
            //https://blog.sangyoung.me/2016/12/28/BadTokenException/
//            binding.ivImage.setOnClickListener(new View.OnItemClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int position = getAdapterPosition();
//                    image_position = position;
//                    if (getImageList.get(position).getImage_url() != null) {
//                        String xx = getImageList.get(position).getImage_url();
//                        if (xx != null) {
//                            //드로어디테일액티비티가 리스트를 구성하는 리스트 보관함
//
//                            Intent intent = new Intent(context.getApplicationContext(), DrawerDetailActivity.class);
//                            intent.putExtra(KEY_IMAGE_URL, xx);
//                            context.startActivity(intent);
//                        }
//                    }
//                }
//            });
        }
    }


    interface OnAdapterItemClickListener {
        void onAdapterViewClick(View view);
    }
}
