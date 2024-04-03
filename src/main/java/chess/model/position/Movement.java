package chess.model.position;

import java.util.ArrayList;
import java.util.List;

public class Movement {
    private final Position source;
    private final Position target;

    public Movement(Position source, Position target) {
        if (source == target) {
            throw new IllegalArgumentException("출발지와 도착지가 동일하면 움직일 수 없습니다.");
        }
        this.source = source;
        this.target = target;
    }

    public int getFileGap() {
        return source.getFileGap(target);
    }

    public int getRankGap() {
        return source.getRankGap(target);
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
        return source.isSameFile(target);
    }

    private boolean isSameRank() {
        return source.isSameRank(target);
    }

    public boolean isDiagonal() {
        return Math.abs(getFileGap()) == Math.abs(getRankGap());
    }

    public List<Position> getIntermediatePositions() {
        if (!isSameFileOrRank() && !isDiagonal()) {
            return new ArrayList<>();
        }
        List<Position> positions = new ArrayList<>();
        Position currentPosition = source.moveToTargetByStep(target);
        while (!currentPosition.equals(target)) {
            positions.add(currentPosition);
            currentPosition = currentPosition.moveToTargetByStep(target);
        }
        return positions;
    }

    public boolean isSourceRankMatch(int rank) {
        return source.isOnRank(rank);
    }

    public Position getSource() {
        return source;
    }

    public Position getTarget() {
        return target;
    }
}
