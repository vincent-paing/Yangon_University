package aungkyawpaing.yangonuniversity.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import aungkyawpaing.yangonuniversity.BuildConfig;
import aungkyawpaing.yangonuniversity.models.Department;
import aungkyawpaing.yangonuniversity.R;
import aungkyawpaing.yangonuniversity.utils.Util;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by Vincent on 14-May-15.
 */
public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.ViewHolder> {

  private ArrayList<Department> mDataSet = new ArrayList<>();
  private Context mContext;
  OnItemClickListener mItemClickListener;

  public DepartmentAdapter(ArrayList<Department> mDataSet, Context context) {
    this.mDataSet.addAll(mDataSet);
    mContext = context;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_department, parent, false);
    DepartmentAdapter.ViewHolder viewHolder = new ViewHolder(view);
    return viewHolder;
  }

  @Override public int getItemCount() {
    return mDataSet.size();
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    final Department department = mDataSet.get(position);

    holder.mDeptName.setText(department.getDept_name());
    holder.mDeptDetail.setText(
        Util.unescape(department.getDept_info().substring(0, 125) + "....."));

    int resID = mContext.getResources()
        .getIdentifier(department.getDept_img(), "drawable", BuildConfig.APPLICATION_ID);

    Picasso.with(mContext)
        .load(
            "https://firebasestorage.googleapis.com/v0/b/windy-watch-94309.appspot.com/o/departments%2Fanthropology.png?alt=media&token=97fdd5ca-9697-440d-9de3-3553fcd4e6c8")
        .into(holder.mDeptImage);
  }

  public interface OnItemClickListener {
    public void onItemClick(View view, int position);
  }

  public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
    this.mItemClickListener = mItemClickListener;
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @Bind(R.id.item_department_image) ImageView mDeptImage;
    @Bind(R.id.item_department_name) TextView mDeptName;
    @Bind(R.id.item_department_detail) TextView mDeptDetail;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    @Override public void onClick(View view) {
      if (mItemClickListener != null) {
        mItemClickListener.onItemClick(view, getPosition());
      }
    }
  }
}
