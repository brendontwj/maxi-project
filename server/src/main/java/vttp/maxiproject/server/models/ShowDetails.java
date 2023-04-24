package vttp.maxiproject.server.models;

import java.util.LinkedList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class ShowDetails {
    private Integer id;
    private String name;
    private String poster_path;
    private String media_type;
    private Double vote_average;
    private String release_date;
    private String overview;
    private List<String> cast;
    private List<String> creators;
    private List<String> genres;

    public List<String> getCast() {
        return cast;
    }
    public void setCast(List<String> cast) {
        this.cast = cast;
    }
    public List<String> getCreators() {
        return creators;
    }
    public void setCreators(List<String> creators) {
        this.creators = creators;
    }
    public List<String> getGenres() {
        return genres;
    }
    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
    public String getRelease_date() {
        return release_date;
    }
    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
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
    public String getOverview() {
        return overview;
    }
    public void setOverview(String overview) {
        this.overview = overview;
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
    public static ShowDetails createFromJson(JsonObject jo, String media_type) {
        ShowDetails s = new ShowDetails();
        s.setId(jo.getInt("id"));
        try {s.setName(jo.getString("name"));} 
        catch (Exception e) {s.setName(jo.getString("title"));}
        try {
            s.setPoster_path("http://image.tmdb.org/t/p/original%s".formatted(jo.getString("poster_path")));
        } catch (Exception e) {
            s.setPoster_path("https://deadmanfred.sgp1.digitaloceanspaces.com/myobjects/no-image-icon.png");
        }
        s.setMedia_type(media_type);
        s.setVote_average(jo.getJsonNumber("vote_average").doubleValue());
        try {s.setRelease_date(jo.getString("release_date"));} 
        catch (Exception e) {s.setRelease_date(jo.getString("first_air_date"));}
        s.setOverview(jo.getString("overview"));
        JsonArray videosArr = jo.getJsonObject("videos").getJsonArray("results");
        try {
            s.cast = videosArr.stream()
                .map(jasonValue -> jasonValue.asJsonObject())
                .map(jsonObj -> "www.youtube.com/watch?v=%s".formatted(jsonObj.getString("name")))
                .toList();
        } catch (Exception e) {
            s.cast.clear();
        }
        try {
            JsonArray creatorsArr = jo.getJsonArray("created_by");
            s.creators = creatorsArr.stream()
                .map(jasonValue -> jasonValue.asJsonObject())
                .map(jsonObj -> jsonObj.getString("name"))
                .toList();
        } catch (Exception e) {
            s.setCreators(new LinkedList<>());
        }
        JsonArray genresArr = jo.getJsonArray("genres");
        s.genres = genresArr.stream()
            .map(jasonValue -> jasonValue.asJsonObject())
            .map(jsonObj -> jsonObj.getString("name"))
            .toList();
        return s;
    }

    public JsonObject toJson() {
        JsonArrayBuilder castArr = Json.createArrayBuilder();
        for(String cast: cast)
            castArr.add(cast);
        JsonArrayBuilder creatorsArr = Json.createArrayBuilder();
        for(String creator: creators)
            creatorsArr.add(creator);
        JsonArrayBuilder genresArr = Json.createArrayBuilder();
        for(String genre: genres)
            genresArr.add(genre);
        
        JsonObject jo = Json.createObjectBuilder()
            .add("id", id)
            .add("name", name)
            .add("poster_path", poster_path)
            .add("media_type", media_type)
            .add("vote_average", vote_average)
            .add("release_date", release_date)
            .add("overview", overview)
            .add("cast", castArr.build())
            .add("creators", creatorsArr.build())
            .add("genres", genresArr.build())
            .build();
        return jo;
    }
}
