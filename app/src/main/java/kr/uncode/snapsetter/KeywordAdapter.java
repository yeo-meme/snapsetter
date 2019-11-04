package kr.uncode.snapsetter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class KeywordAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
    private LayoutInflater inflater;
    private KeywordViewHolder keywordViewHolder;


    public KeywordAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_listview, null);

            keywordViewHolder = new KeywordViewHolder();
            keywordViewHolder.label = convertView.findViewById(R.id.label);

            convertView.setTag(keywordViewHolder);
        } else {
            keywordViewHolder =(KeywordViewHolder) convertView.getTag();
        }
        keywordViewHolder.label.setText(list.get(position));
        return convertView;
    }

    class KeywordViewHolder {
        public TextView label;
    }
}
