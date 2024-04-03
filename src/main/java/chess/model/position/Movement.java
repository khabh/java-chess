package chess.model.position;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public boolean isSameRank() {
        return source.isSameRank(target);
    }

    public boolean isDiagonal() {
        return Math.abs(getFileGap()) == Math.abs(getRankGap());
    }

    public List<Position> getIntermediatePositions() {
        validateCanGetIntermediatePositions();
        List<Position> positions = new ArrayList<>();
        Position currentPosition = source.moveToTargetByStep(target);
        while (!currentPosition.equals(target)) {
            positions.add(currentPosition);
            currentPosition = currentPosition.moveToTargetByStep(target);
        }
        return positions;
    }

    private void validateCanGetIntermediatePositions() {
        if (!isSameFileOrRank() && !isDiagonal()) {
            throw new IllegalStateException("직선 경로를 반환할 수 없습니다.");
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movement movement = (Movement) o;
        return Objects.equals(source, movement.source) && Objects.equals(target, movement.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }
}
