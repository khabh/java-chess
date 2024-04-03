package chess.model.piece;

import chess.model.board.History;
import chess.model.board.MovementAnalysis;
import chess.model.board.MovementValidator;
import chess.model.position.Movement;
import chess.model.position.Position;

import java.util.Map;

public class Rook extends Piece {
    private static final Piece BLACK_ROOK = new Rook(Color.BLACK);
    private static final Piece WHITE_ROOK = new Rook(Color.WHITE);

    private Rook(Color color) {
        super(color, 5.0);
    }

    public static Piece from(Color color) {
        if (Color.BLACK == color) {
            return BLACK_ROOK;
        }
        return WHITE_ROOK;
    }

    @Override
    public MovementAnalysis analyze(Movement movement, History history) {
        validateCurrentColor(history);
        MovementValidator movementValidator = getDefaultValidator(movement);
        Map<Position, Piece> changes = getDefaultChanges(movement);
        return new MovementAnalysis(movementValidator, changes);
    }

    @Override
    public boolean canMove(Movement movement, Piece piece) {
        validateTargetColor(piece);
        return movement.isSameFileOrRank();
    }
}
