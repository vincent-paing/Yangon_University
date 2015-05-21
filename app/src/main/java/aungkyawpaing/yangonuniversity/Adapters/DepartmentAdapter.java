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
import aungkyawpaing.yangonuniversity.R;
import aungkyawpaing.yangonuniversity.Utils.Util;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Vincent on 14-May-15.
 */
public class DepartmentAdapter extends ArrayAdapter<Department> {

    public DepartmentAdapter(Context context, ArrayList<Department> list) {
        super(context, R.layout.item_department, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView!=null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_department, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        final Department department = getItem(position);

        viewHolder.mDeptName.setText(department.getDept_name());
        viewHolder.mDeptDetail.setText(Util.unescape(department.getDept_info().substring(0, 125) + "....."));

        int resID = convertView.getResources().getIdentifier(department.getDept_img(), "drawable", "aungkyawpaing.yangonuniversity");
        Picasso.with(convertView.getContext()).load(resID)
                .resize(200, 240)
                .into(viewHolder.mDeptImage);

        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.item_department_image) ImageView mDeptImage;
        @InjectView(R.id.item_department_name) TextView mDeptName;
        @InjectView(R.id.item_department_detail) TextView mDeptDetail;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

    }


}
