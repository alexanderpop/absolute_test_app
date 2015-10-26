package software.absolute.test_app.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Pop Alex-Cristian on 10/12/2015.
 */
@Table(name = "markers")

public class MarkerModel extends Model {

    @Column
    private String markerId;
    @Column
    private String latitude;
    @Column
    private String longitude;
    @Column
    private String color;
    @Column
    private int day;
    @Column
    private int month;
    @Column
    private int year;
    @Column
    private long timestamp;
    @Column
    private String title;
    @Column
    private String hashtags;


    public MarkerModel() {
    }

    public void setHashtags(String hashtags) {
        this.hashtags = hashtags;
    }

    public String getHashtags() {
        return hashtags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getMarkerId() {
        return markerId;
    }

    public void setMarkerId(String markerId) {
        this.markerId = markerId;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}