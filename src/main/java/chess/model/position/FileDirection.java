package chess.model.position;

public enum FileDirection {
    LEFT(-1),
    RIGHT(1);

    private final int value;

    FileDirection(int value) {
        this.value = value;
    }

    public boolean match(Movement movement) {
        int movementRankDirection = Integer.signum(movement.getFileGap());
        return value == movementRankDirection;
    }
}
