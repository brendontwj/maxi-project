package vttp.maxiproject.server.repositories;

public class Queries {
    
    public static final String SQL_CREATE_USER = "insert into users(id, username, password, email) values (?,?,sha(?),?)";

    public static final String SQL_AUTHENTICATE_USER = "select * from users where username = ? and password = sha(?)";

    public static final String SQL_GET_USER_BY_CREDS = "select * from users where username = ? and password = sha(?)";

    public static final String SQL_INSERT_REVIEW = "insert into reviews(username, rating, comment, mediaType, mediaId) values (?,?,?,?,?)";

    public static final String SQL_GET_REVIEWS = "select * from reviews where mediaType = ? and mediaId = ?";

    public static final String SQL_DELETE_REVIEW = "delete from reviews where username = ? and mediaType = ? and mediaId = ?";

    public static final String SQL_CREATE_FAVOURITES_LIST = "insert into favouriteLists(id, username) values(?,?)";

    public static final String SQL_GET_LIST_FROM_USERNAME = "select * from favouriteLists where username = ?";

    public static final String SQL_INSERT_LIST_ITEM = "insert into favouriteListItem(name, poster_path, vote_average, mediaType, mediaId, listId, release_date) values (?,?,?,?,?,?,?)";

    public static final String SQL_GET_ITEMS_FOR_FLIST = "select * from favouriteListItem where listId = ?";

    public static final String SQL_DELETE_SHOW_FROM_FAVOURITES = "delete from favouriteListItem where name = ? and poster_path = ? and vote_average = ? and mediaType = ? and mediaId = ? and listId = ? and release_date = ? limit 1";
}
