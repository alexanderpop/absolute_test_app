package software.absolute.test_app.helpers;

import com.activeandroid.query.Select;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import software.absolute.test_app.models.MarkerModel;


/**
 * Created by Pop Alex-Cristian on 10/12/2015.
 */
public class DatabaseHelper {
    private static DatabaseHelper instance;

    public static DatabaseHelper get() {
        if (instance == null) instance = getSync();
        return instance;
    }

    private static synchronized DatabaseHelper getSync() {
        if (instance == null) instance = new DatabaseHelper();
        return instance;
    }

    public void addMarker(final MarkerModel markerModel) {
        markerModel.save();
    }

    public MarkerModel getMarker(final String title) {
        MarkerModel markerModel = new Select().from(MarkerModel.class)
                .where("title = ?", title)
                .executeSingle();

        if (markerModel == null)
            return null;

        return markerModel;
    }

    public List<MarkerModel> getMarkers() {
        List<MarkerModel> markerModels;

        markerModels = new Select().from(MarkerModel.class).execute();

        if (markerModels != null && markerModels.size() > 0)
            return markerModels;
        else
            return null;
    }

    public int getMarkerCount() {
        return new Select().from(MarkerModel.class).count();
    }

    public List<MarkerModel> getMarkers(final String timePeriod) {

        List<MarkerModel> markerModels = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        calendar.setTimeInMillis(0);
        calendar.set(year, month, day, 0, 0, 0);
        Date date = calendar.getTime();

        if (timePeriod.equals("day_filter"))
            markerModels = new Select().from(MarkerModel.class).where("timestamp = ?", date.getTime()).execute();
        else if (timePeriod.equals("week_filter")) {
            Calendar weekCalendar = Calendar.getInstance();
            weekCalendar.add(Calendar.DAY_OF_MONTH, 7);
            markerModels = new Select().from(MarkerModel.class).where("timestamp >= ? and timestamp <= ?", date.getTime(), weekCalendar.getTime().getTime()).execute();
        } else if (timePeriod.equals("month_filter")) {
            Calendar monthCalendar = Calendar.getInstance();
            monthCalendar.add(Calendar.MONTH, 1);
            markerModels = new Select().from(MarkerModel.class).where("timestamp >= ? and timestamp <= ?", date.getTime(), monthCalendar.getTime().getTime()).execute();
        }

        if (markerModels != null && markerModels.size() > 0)
            return markerModels;
        else
            return null;
    }

    public String listToString(List<String> hashtags) {
        return new Gson().toJson(hashtags);
    }

    public List<String> stringToList(String str) {
        return new Gson().fromJson(str, new TypeToken<List<String>>() {
        }.getType());
    }
}
