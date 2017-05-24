package tv.accedo.colourmemory.database;

public class DBConstants {

    public static final String TABLE_PLAYERS = "players";

    public static final String COLUMN_TABLE_ID     = "columnId";
    public static final String COLUMN_NAME         = "name";
    public static final String COLUMN_SCORE        = "score";


    public static final String TABLE_CREATE_PLAYERS =
            "CREATE TABLE " + TABLE_PLAYERS + " (" +
                    COLUMN_TABLE_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME        + " TEXT, " +
                    COLUMN_SCORE       + " FLOAT " +
                    ")";

    public static final String[] allPlayersColumns = {
        COLUMN_TABLE_ID,
        COLUMN_NAME,
        COLUMN_SCORE
    };

}
