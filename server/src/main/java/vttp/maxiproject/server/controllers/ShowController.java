package vttp.maxiproject.server.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import vttp.maxiproject.server.models.Review;
import vttp.maxiproject.server.models.Show;
import vttp.maxiproject.server.services.ShowDatabaseService;
import vttp.maxiproject.server.services.UserReviewService;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ShowController {

    @Autowired
    private ShowDatabaseService showService;

    @Autowired
    private UserReviewService userReviewService;

    @GetMapping(path = "/trending", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getTrendingShows() {
        List<Show> showList = showService.getTrendingShows();
        if(showList.isEmpty()) {
            JsonObject response = Json.createObjectBuilder()
                .add("Error", "Did not receive trending shows")
                .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response.toString());
        }
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for(Show s : showList) {
            jab.add(s.toJson());
        }
        return ResponseEntity.ok().body(jab.build().toString());
    }

    @GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> searchShows(@RequestParam String query) {
        System.out.println(">>>>>>>>>>>>>>>> query: " + query);
        List<Show> showList = showService.searchForShow(query);
        if(showList.isEmpty()) {
            JsonObject response = Json.createObjectBuilder()
                .add("Matches not found", "Did not find any matching shows")
                .build();
            return ResponseEntity.status(HttpStatus.OK).body(response.toString());
        }
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for(Show s : showList)
            jab.add(s.toJson());
        return ResponseEntity.ok().body(jab.build().toString());
    }

    @GetMapping(path = "/{media_type}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getShowById(@PathVariable String media_type, @PathVariable String id) {
        Optional<JsonObject> opt = showService.getShowById(media_type, Integer.parseInt(id));
        if(!opt.isPresent()) {
            JsonObject response = Json.createObjectBuilder()
                .add("Error", "Did not find show with matching ID")
                .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response.toString());
        }
        return ResponseEntity.ok().body(opt.get().toString());
    }

    @GetMapping(path = "/{mediaType}/{mediaId}/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getReviews(@PathVariable String mediaType, @PathVariable String mediaId) {
        List<Review> reviewList = userReviewService.getReviews(mediaType, mediaId);
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for(Review r : reviewList) {
            jab.add(r.toJson());
        }
        return ResponseEntity.ok().body(jab.build().toString());
    }

    @PostMapping(path = "/{mediaType}/{mediaId}/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteReview(
        @PathVariable String mediaType,
        @PathVariable String mediaId,
        @RequestPart String username,
        @RequestPart String rating,
        @RequestPart String comment
    ) {
        if(userReviewService.deleteReview(username, mediaType, mediaId)) {
            return ResponseEntity.ok().body(
                Json.createObjectBuilder()
                    .add("message", "review deleted")
                    .build().toString()
            );
        }
        
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(
                Json.createObjectBuilder()
                    .add("message", "review not deleted")
                    .build().toString()
        );
    }

    @PostMapping(path = "/post/review", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postReview(
        @RequestPart String username,
        @RequestPart String rating,
        @RequestPart String comment,
        @RequestPart String mediaType,
        @RequestPart String mediaId
    ) {
        Review review = new Review();
        System.out.println("username: " + username);
        System.out.println("rating: " + rating);
        System.out.println("comment: " + comment);
        System.out.println("mediaType: " + mediaType);
        System.out.println("mediaId: " + mediaId);

        review.setUsername(username);
        review.setRating(Integer.parseInt(rating));
        review.setComment(comment);
        review.setMediaType(mediaType);
        review.setMediaId(mediaId);

        System.out.println(review.toString());

        try {
            if (userReviewService.insertReview(review)) {
                return ResponseEntity.ok().body(
                    Json.createObjectBuilder()
                        .add("message", "Review successfully posted")
                        .build().toString()
                );
            }
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(
                Json.createObjectBuilder()
                    .add("message", "Review not successfully posted")
                    .build().toString()
            );
        } catch (DuplicateKeyException e) {
            System.out.println("inside duplicate catch");;
            return ResponseEntity.badRequest().body(
                Json.createObjectBuilder()
                    .add("message", "User already left a review for this movie")
                    .build().toString()
            );
        }
        
        
    }
}
