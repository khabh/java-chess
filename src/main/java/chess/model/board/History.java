package chess.model.board;

import chess.model.piece.Color;
import chess.model.position.Movement;
import chess.model.position.Position;

import java.util.ArrayDeque;
import java.util.Deque;

public class History {
    private static final Color START_COLOR = Color.WHITE;

    private final Deque<Change> prevChanges = new ArrayDeque<>();

    public void addChange(Change change) {
        prevChanges.addLast(change);
    }

    public Color getCurrentColor() {
        if (prevChanges.isEmpty()) {
            return START_COLOR;
        }
        return prevChanges.getLast().getOppositeColor();
    }

    public boolean isEnPassant(Movement movement) {
        if (prevChanges.isEmpty()) {
            return false;
        }
        return prevChanges.peekLast()
                .matchEnPassant(movement);
    }

    public boolean isChanged(Position position) {
        return prevChanges.stream()
                .anyMatch(prevChange -> prevChange.isSource(position));
    }

    public void removeLast() {
        if (prevChanges.isEmpty()) {
            throw new IllegalStateException("invalid remove");
        }
        prevChanges.removeLast();
    }
}
