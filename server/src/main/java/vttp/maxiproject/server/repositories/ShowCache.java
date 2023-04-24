// package vttp.maxiproject.server.repositories;

// import java.io.StringReader;
// import java.time.Duration;
// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.stereotype.Repository;

// import jakarta.json.Json;
// import jakarta.json.JsonObject;
// import jakarta.json.JsonReader;

// @Repository
// public class ShowCache {
    
//     @Autowired
//     @Qualifier("SHOW_CACHE")
//     private RedisTemplate<String, String> redisTemplate;

//     public void saveDetails(String mediaType, String mediaId, JsonObject json) {
//         redisTemplate.opsForValue().set(mediaType+mediaId, json.toString(), Duration.ofHours(1));
//     }

//     public Optional<JsonObject> getShowDetails(String media_type, String id) {
//         System.out.println("inside cache retrieval");
//         String key = media_type + id;
//         System.out.println(key);
//         String value = redisTemplate.opsForValue().get(key);

//         if (value == null) {
//             return Optional.empty();
//         }

//         JsonReader reader = Json.createReader(new StringReader(value));
//         JsonObject json = reader.readObject();
//         return Optional.of(json);
//     }
// }
