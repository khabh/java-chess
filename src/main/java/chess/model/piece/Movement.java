package chess.model.piece;

import chess.model.board.Position;

import java.util.ArrayList;
import java.util.List;

public class Movement {
    private final Position source;
    private final Position destination;

    public Movement(Position source, Position destination) {
        if (source == destination) {
            throw new IllegalArgumentException("출발지와 도착지가 동일하다????");
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

    public boolean isStraight() {
        return getFileGap() == 0 || getRankGap() == 0;
    }

    public boolean isVerticalMove() {
        return this.getFileGap() == 0;
    }


    public boolean isHorizontalMove() {
        return this.getRankGap() == 0;
    }

    public boolean isDiagonal() {
        return Math.abs(getFileGap()) == Math.abs(getRankGap());
    }

    public List<Position> getIntermediatePositions() {
        List<Position> positions = new ArrayList<>();
        int fileStep = getFileStep();
        int rankStep = getRankStep();
        Position currentPosition = source.move(fileStep, rankStep);
        while (!currentPosition.equals(destination)) {
            positions.add(currentPosition);
            currentPosition = currentPosition.move(fileStep, rankStep);
        }
        return positions;
    }

    private int getFileStep() {
        return Integer.signum(getFileGap());
    }

    private int getRankStep() {
        return Integer.signum(getRankGap());
    }

    public boolean isSourceRankMatch(int rank) {
        return source.isRankEquals(rank);
    }
}
