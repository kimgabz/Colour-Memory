package tv.accedo.colourmemory.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import tv.accedo.colourmemory.Player;


public class DataSource {

    private static final String TAG = DataSource.class.getSimpleName();

    private DBHelper dBHelper;
    private SQLiteDatabase database;

    public DataSource(Context context) {
        dBHelper = new DBHelper(context);
    }

    public void open() {
        Log.i(TAG, "Database opened");
        database = dBHelper.getWritableDatabase();
    }

    public void close() {
        Log.i(TAG, "Database closed");
        if (database.isOpen()) {
            database.close();
        }
    }

    public boolean isOpen() {
        return database.isOpen();
    }

    public void insertTasks(List<Player> dataList) {
        if (!database.isOpen()) {
            return;
        }
        for (int x = 0; x < dataList.size(); x++) {
            createTaskData(dataList.get(x));
        }
    }

    public Player createTaskData(Player player) {
        ContentValues values = new ContentValues();

        values.put(DBConstants.COLUMN_NAME,  player.getName());
        values.put(DBConstants.COLUMN_SCORE, player.getScore());

        database.insert(DBConstants.TABLE_PLAYERS, null, values);
        return player;

    }

//    public void editItem(Task task) {
//        ContentValues values = new ContentValues();
//
//        values.put(DBConstants.COLUMN_ID,           task.getId());
//        values.put(DBConstants.COLUMN_TITLE,        task.getTitle());
//        values.put(DBConstants.COLUMN_DETAILS,      task.getDetails());
//
//        database.update(DBConstants.TABLE_TASKS, values, "columnId = ?", new String[]{task.getId()+""});
//    }

    public ArrayList<Player> findAllTasks() {
        Cursor cursor = database.query(DBConstants.TABLE_PLAYERS, DBConstants.allPlayersColumns,
                null, null, null, null, null);

        Log.i(TAG, "Returned " + cursor.getCount() + " rows");
        return cursorToTaskList(cursor);
    }

    private ArrayList<Player> cursorToTaskList(Cursor cursor) {
        ArrayList<Player> players = new ArrayList<>();
        if (cursor.getCount() > 0) while (cursor.moveToNext()) {
            Player player = new Player();

            player.setName(cursor.getString(cursor.getColumnIndex(DBConstants.COLUMN_NAME)));
            player.setScore(cursor.getInt(cursor.getColumnIndex(DBConstants.COLUMN_SCORE)));
            players.add(player);
        }
        return players;
    }

    public boolean deleteAllTable(String TABLE_NAME) {
        int doneDelete = 0;
        doneDelete = database.delete(TABLE_NAME, null, null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }

}
