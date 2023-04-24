package vttp.maxiproject.server.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import vttp.maxiproject.server.models.Show;
import vttp.maxiproject.server.models.ShowDetails;
// import vttp.maxiproject.server.repositories.ShowCache;
import vttp.maxiproject.server.repositories.ShowDatabaseRepository;

@Service
public class ShowDatabaseService {

    @Autowired
    private ShowDatabaseRepository showRepo;

    // @Autowired
    // private ShowCache showCache;

    public List<Show> getTrendingShows() {
        List<Show> showList = new LinkedList<>();
        Optional<JsonObject> results = showRepo.getTrendingMovies();
        if(results.isEmpty())
            return showList;
        // System.out.println(">>>>>>>>>>>>>>> optional: %s".formatted(results.toString()));
        JsonArray shows = results.get().getJsonArray("results");
        // System.out.println(">>>>>>>>>>>>>>> jsonarray: " + shows.toString());
        showList = shows.stream().map(jv -> jv.asJsonObject()).map(jo -> Show.createFromJson(jo)).toList();
        // for(Show o : showList) {
        //     System.out.println(o.toString());
        // }
        return showList;
    }
    
    public List<Show> searchForShow(String query) {
        List<Show> showList = new LinkedList<>();
        Optional<JsonObject> results = showRepo.searchShows(query);
        System.out.println(results.get().toString());
        if(results.isEmpty())
            return showList;
        // System.out.println(">>>>>>>>>>>>>>> optional: %s".formatted(results.toString()));
        JsonArray shows = results.get().getJsonArray("results");
        // System.out.println(">>>>>>>>>>>>>>> jsonarray: " + shows.toString());
        showList = shows.stream().map(jv -> jv.asJsonObject()).map(jo -> Show.createFromJson(jo)).toList();
        for(Show o : showList) {
            System.out.println(o.toString());
        }
        return showList;
    }

    public Optional<JsonObject> getShowById(String media_type, Integer id) {
        // Optional<JsonObject> result = showCache.getShowDetails(media_type, String.valueOf(id));
        // if(result.isPresent()) {
        //     return result;
        // }
        Optional<JsonObject> result = showRepo.getShowById(media_type, id);
        if(!result.isPresent())
            return Optional.empty();

        JsonObject json = result.get();
        ShowDetails sd = ShowDetails.createFromJson(json, media_type);
        JsonObject sdJson = sd.toJson();
        // showCache.saveDetails(media_type, String.valueOf(id), sdJson);
        return Optional.of(sdJson);
    }
}
