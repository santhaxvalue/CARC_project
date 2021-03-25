package constants;

import java.util.ArrayList;

/**
 * Created by sureshkumar on 1/10/2017.
 */
public class GeoConstants {

    String id;
    String name;
    String latitude;
    String longitude;
    String radius;
    String tag_string;
    ArrayList<String> tags;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getTag_string() {
        return tag_string;
    }

    public void setTag_string(String tag_string) {
        this.tag_string = tag_string;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
}
