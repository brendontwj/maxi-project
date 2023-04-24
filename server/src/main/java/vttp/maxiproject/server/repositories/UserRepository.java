package vttp.maxiproject.server.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import static vttp.maxiproject.server.repositories.Queries.*;

import vttp.maxiproject.server.models.Show;
import vttp.maxiproject.server.models.User;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean createUser(User user) {
        return jdbcTemplate.update(SQL_CREATE_USER, user.getId(), user.getUsername(), user.getPassword(), user.getEmail()) > 0;
    }

    public boolean checkUserCreds(User user) {
        final SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_AUTHENTICATE_USER, user.getUsername(), user.getPassword());
        return rs.next();
    }

    public User getUserByCreds(User user) {
        final SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_GET_USER_BY_CREDS, user.getUsername(), user.getPassword());
        rs.next();
        user.setId(rs.getString("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        return user;
    }

    public boolean createFavouriteList(String id, String username) {
        return jdbcTemplate.update(SQL_CREATE_FAVOURITES_LIST, id, username) > 0;
    }

    public SqlRowSet getFavouriteList(String username) {
        return jdbcTemplate.queryForRowSet(SQL_GET_LIST_FROM_USERNAME, username);
    }

    public SqlRowSet getFavouriteListItems(String listId) {
        return jdbcTemplate.queryForRowSet(SQL_GET_ITEMS_FOR_FLIST, listId);
    }

    public boolean insertFavouriteListItem(Show s, String listId) {
        return jdbcTemplate.update(
            SQL_INSERT_LIST_ITEM,
            s.getName(),
            s.getPoster_path(),
            s.getVote_average(),
            s.getMedia_type(),
            s.getId(),
            listId,
            s.getRelease_date())
            > 0;
    }

    public boolean deleteShow(Show s, String listId) {
        return jdbcTemplate.update(
            SQL_DELETE_SHOW_FROM_FAVOURITES,
            s.getName(),
            s.getPoster_path(),
            s.getVote_average(),
            s.getMedia_type(),
            s.getId(),
            listId,
            s.getRelease_date())
            > 0;
    } 
}
