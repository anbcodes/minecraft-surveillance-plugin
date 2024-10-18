package codes.anb.mcsurveillance;

public class Logger {
  private static Logger instance;
  public int level = 10;

  public static Logger get() {
    if (Logger.instance == null) {
      Logger.instance = new Logger();
    }

    return Logger.instance;
  }

  private Logger() {
  }

  public void debug(String message) {
    log(0, "Debug: " + message);
  }

  public void info(String message) {
    log(5, message);
  }

  public void warn(String message) {
    log(10, message);
  }

  public void error(String message) {
    log(15, message);
  }

  public void error(String message, Exception error) {
    log(15, message + ": " + error.getMessage());
  }

  public void log(int atLevel, String message) {
    if (atLevel >= level) {
      if (atLevel >= 10) {
        WorldInvestigator.get().getLogger().warning(message);
      } else {
        WorldInvestigator.get().getLogger().info(message);
      }
    }
  }
}
