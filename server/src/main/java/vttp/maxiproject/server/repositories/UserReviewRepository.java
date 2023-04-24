package vttp.maxiproject.server.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import static vttp.maxiproject.server.repositories.Queries.*;
import vttp.maxiproject.server.models.Review;

@Repository
public class UserReviewRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public boolean addReview(Review review) {
        return jdbcTemplate.update(SQL_INSERT_REVIEW, review.getUsername(), review.getRating(), review.getComment(), review.getMediaType(), review.getMediaId()) > 0;
    }

    public SqlRowSet getComments(String mediaType, String mediaId) {
        return jdbcTemplate.queryForRowSet(SQL_GET_REVIEWS, mediaType, mediaId);
    }

    public boolean deleteReview(String username, String mediaType, String mediaId) {
        return jdbcTemplate.update(SQL_DELETE_REVIEW, username, mediaType, mediaId) > 0;
    }
}
