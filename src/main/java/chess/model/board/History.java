package chess.model.board;

import chess.model.piece.Color;
import chess.model.position.Movement;
import chess.model.position.Position;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class History {
    private static final Color START_COLOR = Color.WHITE;

    private final Set<Position> changedSquares = new HashSet<>();
    private Change prevChange;

    public void addChange(Change change) {
        prevChange = change;
        changedSquares.add(change.getSourcePosition());
    }

    public Color getCurrentColor() {
        if (Objects.isNull(prevChange)) {
            return START_COLOR;
        }
        return prevChange.getOppositeColor();
    }

    public boolean isEnPassant(Movement movement) {
        return prevChange.matchEnPassant(movement);
    }

    public boolean isChanged(Position position) {
        return changedSquares.contains(position);
    }
}
