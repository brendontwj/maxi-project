package vttp.maxiproject.server.services;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import vttp.maxiproject.server.models.Review;
import vttp.maxiproject.server.repositories.UserReviewRepository;

@Service
public class UserReviewService {

    @Autowired
    private UserReviewRepository userReviewRepository;

    public boolean insertReview(Review review) {
        return userReviewRepository.addReview(review);
    }
 
    public List<Review> getReviews(String mediaType, String mediaId) {
        SqlRowSet rs = userReviewRepository.getComments(mediaType, mediaId);
        List<Review> reviewList = new LinkedList<>();
        while(rs.next()) {
            Review review = new Review();
            review.setUsername(rs.getString("username"));
            review.setRating(rs.getInt("rating"));
            review.setComment(rs.getString("comment"));
            review.setMediaType(rs.getString("mediaType"));
            review.setMediaId(rs.getString("mediaId"));
            System.out.println(review.toString());
            reviewList.add(review);
        }
        return reviewList;
    }

    public boolean deleteReview(String username, String mediaType, String mediaId) {
        return userReviewRepository.deleteReview(username, mediaType, mediaId);
    }
}
