package codes.anb.mchat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class LogWriter {
  private static LogWriter instance;
  private Vector<String> toWrite = new Vector<>();
  public File file;

  public void init() {
    try {
      file = new File(WorldInvestigator.get().getDataFolder() + "/log.txt");
      if (!file.exists()) {
        file.createNewFile();
      }
    } catch (IOException e) {
      Logger.get().error("Failed to access/create log file", e);
    }

  }

  public static LogWriter get() {
    if (LogWriter.instance == null) {
      LogWriter.instance = new LogWriter();
    }

    return LogWriter.instance;
  }

  private LogWriter() {
  }

  public void write(String player, String action, String object) {
    synchronized (toWrite) {
      toWrite.add("[" + java.time.LocalDateTime.now() + "]" + " " + player + ", " + action + ", " + object + "\n");
    }
  }

  public void commit() {
    try {
      FileWriter myWriter = new FileWriter(file, true);
      String toWriteString = "";

      synchronized (toWrite) {
        for (int i = 0; i < toWrite.size(); i++) {
          toWriteString += toWrite.get(i);
        }
        toWrite.clear();
      }

      myWriter.write(toWriteString);
      myWriter.close();
    } catch (IOException e) {
      Logger.get().error("Error writing to file");
    }
  }
}
