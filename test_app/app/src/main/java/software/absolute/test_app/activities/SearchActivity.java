package software.absolute.test_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import software.absolute.test_app.R;
import software.absolute.test_app.adapters.FilterAdapter;
import software.absolute.test_app.helpers.DatabaseHelper;
import software.absolute.test_app.models.MarkerModel;

public class SearchActivity extends AppCompatActivity {

    private ListView listView;
    private EditText searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        listView = (ListView) findViewById(R.id.list_view);
        searchView = (EditText) findViewById(R.id.search_view);

        final List<MarkerModel> markers = DatabaseHelper.get().getMarkers();
        if (markers != null && markers.size() > 0) {
            listView.setAdapter(new FilterAdapter(getApplicationContext(), markers));
        }

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                FilterAdapter adapter = (FilterAdapter) listView.getAdapter();
                if (adapter != null) {
                    if (charSequence.toString().length() > 0 && !charSequence.toString().equals("")) {
                        adapter.getFilter().filter(charSequence.toString());
                        adapter.notifyDataSetChanged();
                    } else {
                        listView.setAdapter(new FilterAdapter(getApplicationContext(), markers));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MarkerModel markerModel = (MarkerModel) adapterView.getItemAtPosition(i);
                if (markerModel != null) {
                    startActivity(new Intent(SearchActivity.this, MarkerActivity.class).putExtra("marker_title", markerModel.getTitle()));
                }
            }
        });
    }


}
