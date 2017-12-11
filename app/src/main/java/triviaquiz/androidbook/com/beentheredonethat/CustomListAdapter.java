package triviaquiz.androidbook.com.beentheredonethat;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Alexander on 10/17/17.
 */

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;

    public CustomListAdapter(Activity context, String[] itemname, Integer[] imgid) {
        super(context, R.layout.menu_item, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.menu_item, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.list_text_view);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.list_image_view);
        ImageView imageView2 = (ImageView) rowView.findViewById(R.id.list_image_view2);

        txtTitle.setText(itemname[position]);
        imageView.setImageResource(imgid[position]);
        imageView2.setImageResource(imgid[position]);
        return rowView;

    };
}