package vttp.maxiproject.server.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vttp.maxiproject.server.models.FavouriteList;
import vttp.maxiproject.server.models.Show;
import vttp.maxiproject.server.models.User;
import vttp.maxiproject.server.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Transactional
    public boolean createUser(User user) {
        UUID uuid = UUID.randomUUID();
        user.setId(uuid.toString());
        boolean userCreated =  userRepo.createUser(user);
        boolean favouriteCreated = userRepo.createFavouriteList(user.getId(), user.getUsername());
        return userCreated && favouriteCreated;
    }

    public boolean authUser(User user) {
        return userRepo.checkUserCreds(user);
    }

    public User getUserByCreds(User user) {
        return userRepo.getUserByCreds(user);
    }

    public Optional<FavouriteList> getFavouriteList(String username) {
        SqlRowSet rs = userRepo.getFavouriteList(username);
        if(rs.next()) {
            FavouriteList flist = new FavouriteList();
            flist.setId(rs.getString("id"));
            flist.setUsername(rs.getString("username"));
            return Optional.of(flist);
        } 

        return Optional.empty();
    }

    public List<Show> getFavouriteListItems(String listId) {
        SqlRowSet rs = userRepo.getFavouriteListItems(listId);
        List<Show> fListItems = new LinkedList<>();
        while(rs.next()) {
            Show s = new Show();
            s.setId(Integer.parseInt(rs.getString("mediaId")));
            s.setMedia_type(rs.getString("mediaType"));
            s.setName(rs.getString("name"));
            s.setPoster_path(rs.getString("poster_path"));
            s.setRelease_date(rs.getString("release_date"));
            s.setVote_average(rs.getDouble("vote_average"));
            fListItems.add(s);
        }

        return fListItems;
    }

    public boolean insertFavouriteListItem(Show s, String listId) {
        return userRepo.insertFavouriteListItem(s, listId);
    }

    public boolean deleteShow(Show s, String listId) {
        return userRepo.deleteShow(s, listId);
    }
}
