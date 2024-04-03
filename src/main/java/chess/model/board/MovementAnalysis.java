package chess.model.board;

import chess.model.piece.Color;
import chess.model.piece.Piece;
import chess.model.position.Position;

import java.util.Collections;
import java.util.Map;

public class MovementAnalysis {
    private final MovementValidator movementValidator;
    private final Map<Position, Piece> changes;

    public MovementAnalysis(MovementValidator movementValidator, Map<Position, Piece> changes) {
        this.movementValidator = movementValidator;
        this.changes = changes;
    }

    public Map<Position, Color> getValidColors() {
        return movementValidator.getPositionsColor();
    }

    public Map<Position, Piece> getChanges() {
        return Collections.unmodifiableMap(changes);
    }
}
