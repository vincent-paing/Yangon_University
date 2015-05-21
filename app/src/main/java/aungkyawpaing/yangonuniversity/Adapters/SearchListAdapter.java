package aungkyawpaing.yangonuniversity.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import aungkyawpaing.yangonuniversity.ClassModels.Department;
import aungkyawpaing.yangonuniversity.ClassModels.MarkerData;
import aungkyawpaing.yangonuniversity.R;
import aungkyawpaing.yangonuniversity.Utils.Util;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Vincent on 19-May-15.
 */
public class  SearchListAdapter extends ArrayAdapter<MarkerData> {

    public SearchListAdapter(Context context, ArrayList<MarkerData> data_list) {
        super(context, R.layout.item_search, data_list);
        // TODO Auto-generated constructor stub
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView!=null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_search, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        final MarkerData data = this.getItem(position);

        viewHolder.mSearchTitle.setText(data.getTitle());
        int resID = data.getIconres(convertView.getContext());
        Picasso.with(convertView.getContext()).load(resID).into(viewHolder.mSearchImage);
        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.item_search_image) ImageView mSearchImage;
        @InjectView(R.id.item_search_title) TextView mSearchTitle;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

    }
}
