package codes.anb.mcsurveillance;

public enum DBEvent {
  JOIN(0),
  LEAVE(1),
  PLACE(2),
  DESTROY(3),
  KILL(4),
  PICKUP(5),
  DROP(6),
  OPEN_INV(7),
  CLOSE_INV(8),
  SHOT(9),
  HURT_BY(10),
  HURT(11),
  EXPLODED(12),
  INTERACT(13),
  MOVE(14),
  CHAT(15),
  BURNED(16),
  DEATH(17);

  private final int value;

  DBEvent(final int newValue) {
      value = newValue;
  }

  public int getValue() { return value; }
}
