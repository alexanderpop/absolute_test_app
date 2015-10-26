package software.absolute.test_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import software.absolute.test_app.R;
import software.absolute.test_app.models.MarkerModel;


public class FilterAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private List<MarkerModel> markers;

    public FilterAdapter(Context context, List<MarkerModel> markerModels) {
        this.mContext = context;
        this.markers = markerModels;
    }

    @Override
    public int getCount() {
        return markers.size();
    }

    @Override
    public Object getItem(int position) {
        return markers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = View.inflate(mContext, R.layout.category_filter_item, null);
        TextView title_area = (TextView) v.findViewById(R.id.title_area);
        TextView hashtag_area = (TextView) v.findViewById(R.id.hashtag_area);
        title_area.setText(markers.get(position).getTitle());
        hashtag_area.setText(markers.get(position).getHashtags());

        return v;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();

                if (constraint.length() == 0) {
                    results.values = markers;
                    results.count = markers.size();
                } else {
                    List<MarkerModel> suggestions = new ArrayList<>();
                    for (MarkerModel item : markers) {
                        if (item.getTitle().toLowerCase().contains(constraint.toString().toLowerCase()) || item.getHashtags().toLowerCase().contains(constraint.toString().toLowerCase()))
                            suggestions.add(item);
                    }

                    results.values = suggestions;
                    results.count = suggestions.size();
                }

                return results;
            }


            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (results.count == 0) {
                    notifyDataSetInvalidated();
                } else {
                    markers = (ArrayList<MarkerModel>) results.values;
                    notifyDataSetChanged();
                }
            }
        };
    }
}
