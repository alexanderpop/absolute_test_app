package software.absolute.test_app.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import software.absolute.test_app.R;
import software.absolute.test_app.helpers.DatabaseHelper;
import software.absolute.test_app.models.MarkerModel;


public class MarkerActivity extends AppCompatActivity {

    private TextView titleTv, hashtagTv, dateTv, colorTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker);

        titleTv = (TextView) findViewById(R.id.title_info);
        hashtagTv = (TextView) findViewById(R.id.hashtag_info);
        dateTv = (TextView) findViewById(R.id.date_info);
        colorTv = (TextView) findViewById(R.id.color_info);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("marker_title")) {
            String marker_title = bundle.getString("marker_title");
            if (marker_title != null) {
                MarkerModel markerModel = DatabaseHelper.get().getMarker(marker_title);

                titleTv.setText("Title: " + markerModel.getTitle());
                dateTv.setText("Date: " + markerModel.getDay() + "/" + markerModel.getMonth() + "/" + markerModel.getYear());
                hashtagTv.setText("Hashtags: " + markerModel.getHashtags());
                colorTv.setText("Color: " + markerModel.getColor());

            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
