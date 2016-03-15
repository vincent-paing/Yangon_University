package aungkyawpaing.yangonuniversity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import aungkyawpaing.yangonuniversity.models.MarkerData;
import aungkyawpaing.yangonuniversity.R;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Vincent on 19-May-15.
 */
public class SearchListAdapter extends ArrayAdapter<MarkerData> {

  private Context mContext;

  public SearchListAdapter(Context context, ArrayList<MarkerData> data_list) {
    super(context, R.layout.list_item_search_result, data_list);
    mContext = context;
    // TODO Auto-generated constructor stub
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    final ViewHolder viewHolder;
    if (convertView != null) {
      viewHolder = (ViewHolder) convertView.getTag();
    } else {
      LayoutInflater inflater =
          (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView = inflater.inflate(R.layout.list_item_search_result, parent, false);
      viewHolder = new ViewHolder(convertView);
      convertView.setTag(viewHolder);
    }

    final MarkerData data = this.getItem(position);

    viewHolder.mSearchTitle.setText(data.getTitle());
    int resID = data.getIconres(mContext);
    viewHolder.mSearchImage.setImageResource(resID);
    Picasso.with(mContext).load(resID).into(viewHolder.mSearchImage);
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
