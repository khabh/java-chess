package chess.model.position;

import java.util.ArrayList;
import java.util.List;

public class Movement {
    private final Position source;
    private final Position destination;

    public Movement(Position source, Position destination) {
        if (source == destination) {
            throw new IllegalArgumentException("출발지와 도착지가 동일하면 움직일 수 없습니다.");
        }
        this.source = source;
        this.destination = destination;
    }

    public int getFileGap() {
        return source.getFileGap(destination);
    }

    public int getRankGap() {
        return source.getRankGap(destination);
    }

    public int getFileDistance() {
        return Math.abs(getFileGap());
    }

    public int getRankDistance() {
        return Math.abs(getRankGap());
    }

    public boolean isSameFileOrRank() {
        return isSameFile() || isSameRank();
    }

    public boolean isSameFile() {
        return source.isSameFile(destination);
    }

    private boolean isSameRank() {
        return source.isSameRank(destination);
    }

    public boolean isDiagonal() {
        return Math.abs(getFileGap()) == Math.abs(getRankGap());
    }

    public List<Position> getIntermediatePositions() {
        List<Position> positions = new ArrayList<>();
        if (!isSameFileOrRank() && !isDiagonal()) {
            return positions;
        }
        Position currentPosition = source.moveToTargetByStep(destination);
        while (!currentPosition.equals(destination)) {
            positions.add(currentPosition);
            currentPosition = currentPosition.moveToTargetByStep(destination);
        }
        return positions;
    }

    public boolean isSourceRankMatch(int rank) {
        return source.isOnRank(rank);
    }

    public Position getSource() {
        return source;
    }

    public Position getDestination() {
        return destination;
    }
}
