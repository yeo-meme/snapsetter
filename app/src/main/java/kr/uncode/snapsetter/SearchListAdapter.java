package kr.uncode.snapsetter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import kr.uncode.snapsetter.Detail_View.ViewTwoStepActivity;
import kr.uncode.snapsetter.databinding.ListItemImageBinding;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.SearchListViewHolder> {
    public static final String EXTRA_KEY_IMAGE_URL = "EXTRA_KEY_IMAGE_URL";

    private static final String TAG = SearchListAdapter.class.getSimpleName();

    private final List<RetrofitResponse.Documents> mRestApiImageData = new ArrayList<>();
    private String mUserEmail;
    private Realm mRealm;

    public SearchListAdapter(Realm realm) {
        this.mRealm = realm;
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            mUserEmail = currentUser.getEmail();
        }
    }

    public void addDataAll(List<RetrofitResponse.Documents> dataList) {
        mRestApiImageData.clear();
        mRestApiImageData.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mRestApiImageData.size();
    }

    @NonNull
    @Override
    public SearchListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        ListItemImageBinding binding = ListItemImageBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new SearchListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchListAdapter.SearchListViewHolder holder, int position) {
        RetrofitResponse.Documents documents = mRestApiImageData.get(position);
        if (checkBookmarkUrl(documents.image_url)) {
            holder.binding.eheart.setImageResource(R.drawable.heart);
        } else {
            holder.binding.eheart.setImageResource(R.drawable.eheart);
        }
        Glide.with(holder.binding.getRoot()).load(documents.image_url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.ivImage);
    }

    /**
     * DB 북마크 URL 데이터와 현재 onBind된 URL 데이터 비교
     *
     * @return 동일한것을 찾으면 TRUE , 없으면 FALSE 반환
     */
    private boolean checkBookmarkUrl(String bindUrl) {
        if (mRealm == null) return false;

        Log.d(TAG, "checkBookmarkUrl : " + bindUrl);

        // 현재 유저에 해당하는 DB데이터 모두 가져오기
        RealmResults<PictureData> pictureDataList = mRealm.where(PictureData.class)
                .equalTo("name", mUserEmail).findAll();

        // 가져온 DB데이터에서 반복하면서 bindUrl 과 비교 해서 동일한것을 찾으면 TRUE 반환
        for (PictureData pd : pictureDataList) {
            if (TextUtils.equals(bindUrl, pd.getImage_url())) {
                Log.d(TAG, "checkBookmarkUrl : TRUE");
                return true;
            }
        }

        Log.d(TAG, "checkBookmarkUrl : FALSE");
        return false;
    }

    class SearchListViewHolder extends RecyclerView.ViewHolder {
        ListItemImageBinding binding;

        SearchListViewHolder(ListItemImageBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            initClickListener();
        }

        private void initClickListener() {
            binding.clickheart.setOnClickListener(this::bookmarkIconClick);
            // 이미지 사진영역을 클릭하면 자세히 보기 화면으로 새액티비티가 나타나
            // 리스트가 아닌 싱글로 사진을 보여준다
            binding.ivImage.setOnClickListener(this::imageDetailView);
        }

        /**
         * 하트 아이콘 클릭 이벤트 메서드
         */
        private void bookmarkIconClick(View view) {
            int position = getAdapterPosition();
            final String currentImageUrl = mRestApiImageData.get(position).image_url;

            if (checkBookmarkUrl(currentImageUrl)) { // DB에 있음
                deleteBookmarkUrl(currentImageUrl);
            } else { // DB에 없음
                saveBookmarkUrl(currentImageUrl);
            }

            notifyDataSetChanged();
        }

        /**
         * 이미지 클릭시 상세뷰 액티비티로 이동 메서드
         */
        private void imageDetailView(View view) {
            //온어탭터뷰클릭 순간에 Url을 담는
            int position = getAdapterPosition();
            RetrofitResponse.Documents documents = mRestApiImageData.get(position);
            final String url = documents.image_url;

            //최근 본목록에 사진을 저장할 메서드 호출
            currentUerPicSave(url);

            Log.d("image", url);

            //그 URL을 Intent 에 담아서 디테일 액티비티로 보낸다
            Context context = view.getContext();
            Intent intent = new Intent(context, ViewTwoStepActivity.class);
            intent.putExtra(EXTRA_KEY_IMAGE_URL, url);
            context.startActivity(intent);
        }

        private void currentUerPicSave(String currentUrl) {

            Log.d("currentUserPic_Ulr","currentUserPic_Ulr :" + currentUrl);

            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                     CurrentUserPicData currentUserPicData = realm.createObject(CurrentUserPicData.class);
                     currentUserPicData.setCurrent_url(currentUrl);
                }
            });
        }

        /**
         * 하트 클릭이벤트 체인저의 URL파라미터를 DB에 저장하는 메서드
         */
        private void saveBookmarkUrl(String url) {
            Log.d(TAG, "saveBookmarkUrl : " + url);
            PictureData data = new PictureData();
            data.setImage_url(url);
            data.setName(mUserEmail);
            mRealm.executeTransaction(realm -> realm.copyToRealm(data));
        }

        /**
         * 해당하는 URL을 DB에서 삭제하는 메서드
         */
        private void deleteBookmarkUrl(String url) {
            Log.d(TAG, "deleteBookmarkUrl : " + url);
            RealmResults<PictureData> imageUrlList = mRealm.where(PictureData.class)
                    .equalTo("image_url", url).findAll();
            mRealm.executeTransaction(realm -> imageUrlList.deleteAllFromRealm());
        }
    }
}