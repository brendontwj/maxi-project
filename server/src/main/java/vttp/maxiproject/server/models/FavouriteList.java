package vttp.maxiproject.server.models;

import java.util.List;

public class FavouriteList {
    
    private String id;
    private String username;
    private List<Show> listOfShows;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public List<Show> getListOfShows() {
        return listOfShows;
    }
    public void setListOfShows(List<Show> listOfShows) {
        this.listOfShows = listOfShows;
    }
}
