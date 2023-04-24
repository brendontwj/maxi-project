package vttp.maxiproject.server.models;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Show {
    private Integer id;
    private String name;
    private String poster_path;
    private String media_type;
    private Double vote_average;
    private String release_date;

    public int getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPoster_path() {
        return poster_path;
    }
    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }
    public String getMedia_type() {
        return media_type;
    }
    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }
    public Double getVote_average() {
        return vote_average;
    }
    public void setVote_average(Double double1) {
        this.vote_average = double1;
    }
    public String getRelease_date() {
        return release_date;
    }
    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
    public static Show createFromJson(JsonObject jo) {
        Show s = new Show();
        s.setId(jo.getInt("id"));
        s.setMedia_type(jo.getString("media_type"));
        try {
            s.setName(jo.getString("name"));
        } catch (Exception e) {
            s.setName(jo.getString("title"));
        }
        try {
            s.setPoster_path("http://image.tmdb.org/t/p/w500%s".formatted(jo.getString("poster_path")));
        } catch (Exception e) {
            s.setPoster_path("https://deadmanfred.sgp1.digitaloceanspaces.com/myobjects/no-image-icon.png");
        }
        try {
            s.setRelease_date(jo.getString("release_date"));
        } catch (Exception e) {
            try {
                s.setRelease_date(jo.getString("first_air_date"));
            } catch (Exception f) {
                s.setRelease_date("");
            }
         }   
        
        try{
            s.setVote_average(jo.getJsonNumber("vote_average").doubleValue());
        } catch (Exception e) {
            s.setVote_average(0.0);
        }
        return s;
    }

    public JsonObject toJson() {
        JsonObject jo = Json.createObjectBuilder()
            .add("id", id)
            .add("name", name)
            .add("poster_path", defaultValue(poster_path, "https://deadmanfred.sgp1.digitaloceanspaces.com/myobjects/no-image-icon.png"))
            .add("media_type", media_type)
            .add("vote_average", vote_average)
            .add("release_date", release_date)
            .build();
        return jo;
    }

    public <T> T defaultValue(T Value, T defValue) {
        if (null != Value) 
            return Value;
        else
            return defValue;
    }
}
