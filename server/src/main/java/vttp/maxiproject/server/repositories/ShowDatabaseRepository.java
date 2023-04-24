package vttp.maxiproject.server.repositories;

import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Repository
public class ShowDatabaseRepository {

    @Value("${API_KEY}") private String api_key;

    public static final String baseApiURl = "https://api.themoviedb.org/3";
    public static final String baseImageUrl = "https://api.themoviedb.org/t/p";

    public Optional<JsonObject> getTrendingMovies() {
        String uri = UriComponentsBuilder.fromUriString(baseApiURl)
            .path("/trending")
            .path("/all")
            .path("/week")
            .queryParam("api_key", api_key)
            .toUriString();
        // System.out.println(">>>>>> uri: %s".formatted(uri));

        return callApiWithUrl(uri);
    }

    public Optional<JsonObject> searchShows(String query) {
        System.out.println(">>>>> query: " + query);
        String uri = UriComponentsBuilder.fromUriString(baseApiURl)
            .path("/search")
            .path("/multi")
            .queryParam("api_key", api_key)
            .queryParam("query", query)
            .queryParam("language", "en-US")
            .queryParam("page", 1)
            .queryParam("include_adult", false)
            .toUriString();
        System.out.println(">>>>>> uri: %s".formatted(uri));

        return callApiWithUrl(uri);
    }

    public Optional<JsonObject> getShowById(String media_type, Integer id) {
        String uri = UriComponentsBuilder.fromUriString(baseApiURl)
            .path("/%s".formatted(media_type))
            .path("/%s".formatted(id.toString()))
            .queryParam("api_key", api_key)
            .queryParam("append_to_response", "videos")
            .toUriString();
        // System.out.println(">>>>>> uri: %s".formatted(uri));

        return callApiWithUrl(uri);
    }

    private Optional<JsonObject> callApiWithUrl(String uri) {
        RequestEntity<Void> request = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON).build();
        // System.out.println(request.toString());
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = new ResponseEntity<>(HttpStatus.OK);
        try {
            resp = template.getForEntity(new URI(uri), String.class);
        } catch (URISyntaxException e) {
            resp = template.exchange(request, String.class);
        }
        // System.out.println(resp.toString());
        JsonReader reader = Json.createReader(new StringReader(resp.getBody()));
        JsonObject results = reader.readObject();

        if(null == results)
            return Optional.empty();
        return Optional.of(results);
    }
}
