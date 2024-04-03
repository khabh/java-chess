package chess.model.piece;

import chess.model.board.History;
import chess.model.board.MovementAnalysis;
import chess.model.board.MovementValidator;
import chess.model.position.Movement;
import chess.model.position.Position;

import java.util.Map;

public class Bishop extends Piece {
    private static final Piece BLACK_BISHOP = new Bishop(Color.BLACK);
    private static final Piece WHITE_BISHOP = new Bishop(Color.WHITE);

    private Bishop(Color color) {
        super(color, 3.0);
    }

    public static Piece from(Color color) {
        if (Color.BLACK == color) {
            return BLACK_BISHOP;
        }
        return WHITE_BISHOP;
    }

    @Override
    public MovementAnalysis analyze(Movement movement, History history) {
        validateCurrentColor(history);
        MovementValidator movementValidator = getDefaultValidator(movement);
        Map<Position, Piece> changes = getDefaultChanges(movement);
        return new MovementAnalysis(movementValidator, changes);
    }

    @Override
    public boolean canMove(Movement movement, Piece target) {
        validateTargetColor(target);
        return movement.isDiagonal();
    }
}
