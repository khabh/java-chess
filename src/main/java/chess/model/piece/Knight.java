package chess.model.piece;

import chess.model.board.History;
import chess.model.board.MovementAnalysis;
import chess.model.board.MovementValidator;
import chess.model.position.Movement;
import chess.model.position.Position;

import java.util.Collections;
import java.util.Map;

public class Knight extends Piece {
    private static final int LONG_MOVE_DISTANCE = 2;
    private static final int SHORT_MOVE_DISTANCE = 1;
    private static final Piece BLACK_KING = new Knight(Color.BLACK);
    private static final Piece WHITE_KING = new Knight(Color.WHITE);

    private Knight(Color color) {
        super(color, 2.5);
    }

    public static Piece from(Color color) {
        if (Color.BLACK == color) {
            return BLACK_KING;
        }
        return WHITE_KING;
    }

    @Override
    public MovementAnalysis analyze(Movement movement, History history) {
        validateCurrentColor(history);
        MovementValidator movementValidator = new MovementValidator(Collections.emptyMap());
        Map<Position, Piece> changes = getDefaultChanges(movement);
        return new MovementAnalysis(movementValidator, changes);
    }

    @Override
    public boolean canMove(Movement movement, Piece target) {
        validateTargetColor(target);
        int fileDistance = movement.getFileDistance();
        int rankDistance = movement.getRankDistance();
        return (fileDistance == LONG_MOVE_DISTANCE && rankDistance == SHORT_MOVE_DISTANCE)
                || (fileDistance == SHORT_MOVE_DISTANCE && rankDistance == LONG_MOVE_DISTANCE);
    }
}
