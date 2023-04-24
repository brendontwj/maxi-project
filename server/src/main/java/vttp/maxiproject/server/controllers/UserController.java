package vttp.maxiproject.server.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import vttp.maxiproject.server.models.FavouriteList;
import vttp.maxiproject.server.models.Show;
import vttp.maxiproject.server.models.User;
import vttp.maxiproject.server.services.EmailService;
import vttp.maxiproject.server.services.UserService;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class UserController {
    
    @Autowired
    private UserService userService;

    @Autowired
	private EmailService emailService;

    @PostMapping(path = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerUser(
        @RequestPart String username, 
        @RequestPart String password,
        @RequestPart String email
    ) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        boolean added;

        try {
           added = userService.createUser(user);
        } catch (DuplicateKeyException e) {
            JsonObject resp = Json.createObjectBuilder()
                .add("message", "Username has been taken")
                .build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(resp.toString());
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject resp = Json.createObjectBuilder()
                .add("message", "Error when creating user")
                .add("error", e.getMessage())
                .build();
            return ResponseEntity.badRequest().body(resp.toString());
        }

        if (!added) {
            JsonObject resp = Json.createObjectBuilder()
                .add("message", "User not added")
                .build();
            return ResponseEntity.badRequest().body(resp.toString());
        } else {
            emailService.sendEmail(username, email, "Registration at TMDB");
            return ResponseEntity.ok().body(
                Json.createObjectBuilder()
                    .add("message", "user registered")
                    .build().toString()
            );
        }
    }

    @PostMapping(path = "/login", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(
        @RequestPart String username,
        @RequestPart String password
    ) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        
        try {
            if (userService.authUser(user)) {
                user = userService.getUserByCreds(user);
                return ResponseEntity.ok().body(user.toJson().toString());
            } else {
                JsonObject response = Json.createObjectBuilder()
                    .add("message", "Incorrect credentials")
                    .build();
                return ResponseEntity.badRequest().body(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject resp = Json.createObjectBuilder()
                .add("message", "Error occured when attempting to log in")
                .add("error", e.getMessage())
                .build();
            return ResponseEntity.badRequest().body(resp.toString());
        }
    }

    @PostMapping(path = "/post/{username}/favourites", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addToFavourites(
        @RequestPart String username,
        @RequestPart String name,
        @RequestPart String poster_path,
        @RequestPart String release_date,
        @RequestPart String vote_average,
        @RequestPart String mediaType,
        @RequestPart String mediaId
    ) {
        Optional<FavouriteList> fListOpt = userService.getFavouriteList(username);
        if (!fListOpt.isPresent()) {
            JsonObject response = Json.createObjectBuilder()
                    .add("message", "No favourite list for that user found")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response.toString());
        }
        FavouriteList fList = fListOpt.get();
        String listId = fList.getId();
        Show s = new Show();
        s.setId(Integer.parseInt(mediaId));
        s.setMedia_type(mediaType);
        s.setName(name);
        s.setPoster_path(poster_path);
        s.setRelease_date(release_date);
        s.setVote_average(Double.parseDouble(vote_average));

        boolean addedListItem = userService.insertFavouriteListItem(s, listId);

        if (addedListItem) {
            return ResponseEntity.ok().body(
                Json.createObjectBuilder()
                    .add("message", "list item added successfully")
                    .build().toString()
            );
        }
        return ResponseEntity.internalServerError().body(
                Json.createObjectBuilder()
                    .add("message", "unsuccessful")
                    .build().toString()
        );
    }

    @DeleteMapping(path = "/delete/{username}/favourites", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteShow(
        @PathVariable String username,
        @RequestBody Show show
    ) {
        Optional<FavouriteList> fListOpt = userService.getFavouriteList(username);
        if (!fListOpt.isPresent()) {
            JsonObject response = Json.createObjectBuilder()
                    .add("message", "No favourite list for that user found")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response.toString());
        }
        FavouriteList fList = fListOpt.get();
        String listId = fList.getId();
        // Show s = new Show();
        // s.setId(Integer.parseInt(body.getFirst("mediaId")));
        // s.setMedia_type(body.getFirst("mediaType"));
        // s.setName(body.getFirst("name"));
        // s.setPoster_path(body.getFirst("poster_path"));
        // s.setRelease_date(body.getFirst("release_date"));
        // s.setVote_average(Double.parseDouble(body.getFirst("vote_average")));

        boolean isDeleted = userService.deleteShow(show, listId);
        if (isDeleted) {
            return ResponseEntity.ok().body(
                Json.createObjectBuilder()
                    .add("message", "show deleted successfully")
                    .build().toString()
            );
        }
        return ResponseEntity.internalServerError().body(
                Json.createObjectBuilder()
                    .add("message", "unsuccessful")
                    .build().toString()
        );
    }

    @GetMapping(path = "/{username}/favourites", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getFavourites(@PathVariable String username) {
        Optional<FavouriteList> fListOpt = userService.getFavouriteList(username);
        if (!fListOpt.isPresent()) {
            return ResponseEntity.ok().body(
                Json.createArrayBuilder()
                    .build().toString()
            );
        } 
        FavouriteList fList = fListOpt.get();
        String listId = fList.getId();

        List<Show> favouriteListItems = userService.getFavouriteListItems(listId);
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for(Show s : favouriteListItems) {
            jab.add(s.toJson());
        }
        return ResponseEntity.ok().body(jab.build().toString());
    }
}
