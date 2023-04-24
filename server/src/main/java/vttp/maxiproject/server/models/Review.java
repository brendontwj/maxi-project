package vttp.maxiproject.server.models;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Review {
    
    private String username;
    private Integer rating;
    private String comment;
    private String mediaType;
    private String mediaId;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Integer getRating() {
        return rating;
    }
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getMediaType() {
        return mediaType;
    }
    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
    public String getMediaId() {
        return mediaId;
    }
    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("username", username)
            .add("rating", rating)
            .add("comment", comment)
            .add("mediaType", mediaType)
            .add("mediaId", mediaId)
            .build();
    }
}
