package com.anklebreaker.basketball.tw.recordboard;

import java.util.ArrayList;
import com.anklebreaker.basketball.tw.R;
import com.anklebreaker.basketball.tw.summary.RecordBoardBtn;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * adapter class for player's panel
 * */
public class RecordGridViewAdapter extends ArrayAdapter<RecordBoardBtn>{

    Context context;
    int layoutResourceId;
    ArrayList<RecordBoardBtn> data = new ArrayList<RecordBoardBtn>();

    /**
     * constructor
     * layoutResourceId:layout's id(row_grid.xml)
     * data:ArrayList of images
     * */
    public RecordGridViewAdapter(Context context, int layoutResourceId, ArrayList<RecordBoardBtn> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    /**
     * method for init player's gridview
     * @return inited gridview of player's panel
     * */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new RecordHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.item_text);
            holder.imageItem = (ImageView) row.findViewById(R.id.item_image);
            //set record board's icon size
            //row.getLayoutParams().height = MultiDevInit.pH;
            //row.getLayoutParams().width = MultiDevInit.pW;

            row.setPadding(0, 0, 0, 0);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        RecordBoardBtn item = data.get(position);
        holder.txtTitle.setText(item.getTitle());
        holder.imageItem.setImageBitmap(item.getImage());
        return row;
    }

    static class RecordHolder {
        TextView txtTitle;
        ImageView imageItem;
    }
}
