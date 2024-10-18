package codes.anb.mcsurveillance;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DB {
  private static Connection instance;

  public static boolean init() {
    try {
      if (DB.instance == null) {
        DB.instance = DriverManager.getConnection("jdbc:sqlite:" + Paths.get(WorldInvestigator.get().getDataFolder().getAbsolutePath(), "log.sqlite").toAbsolutePath());
        DB.instance.createStatement().executeUpdate("""
          CREATE TABLE IF NOT EXISTS events(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            time INTEGER DEFAULT (strftime('%s','now')),
            name TEXT NOT NULL,
            x REAL NOT NULL,
            y REAL NOT NULL,
            z REAL NOT NULL,
            target TEXT,
            event_id INTEGER,
            context TEXT
        );
        """);
        DB.instance.createStatement().executeUpdate("""
          DROP TABLE IF EXISTS event_ids;
        """);
        DB.instance.createStatement().executeUpdate("""
          CREATE TABLE if not exists event_ids (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT
          );
        """);
        PreparedStatement s = DB.instance.prepareStatement("INSERT INTO event_ids(id, name) VALUES(?, ?)");
        for (DBEvent event : DBEvent.values()) {
          s.setInt(1, event.getValue()); // Insert the event id
          s.setString(2, event.name()); // Insert the event name
          s.executeUpdate(); // Execute the insert
       }

      }
    } catch (SQLException e) {
      Logger.get().error("Error initalizing the db", e);
      return false;
    }

    return true;
  }

  public static Connection get() {

    return DB.instance;
  }

  public static void insert(String name, DBEvent event, double x, double y, double z, String target, String metadata) {
    try {
      PreparedStatement s = DB.instance.prepareStatement("INSERT INTO events(name, x, y, z, target, event_id, context) VALUES(?, ?, ?, ?, ?, ?, ?)");
      s.setString(1, name); // name
      s.setDouble(2, x);          // x
      s.setDouble(3, y);          // y
      s.setDouble(4, z);          // z
      s.setString(5, target);      // target
      s.setInt(6, event.getValue());               // event_id
      s.setString(7, metadata); // context
      s.executeUpdate();
    } catch (SQLException e) {
      Logger.get().error("Failed to insert", e);
    }
  }

  private DB() {
  }
}
