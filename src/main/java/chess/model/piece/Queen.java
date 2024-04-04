package chess.model.piece;

import chess.model.board.History;
import chess.model.board.MovementAnalysis;
import chess.model.board.MovementValidator;
import chess.model.position.Movement;
import chess.model.position.Position;

import java.util.Map;

public class Queen extends Piece {
    private static final Piece BLACK_QUEEN = new Queen(Color.BLACK);
    private static final Piece WHITE_QUEEN = new Queen(Color.WHITE);

    private Queen(Color color) {
        super(color, 9.0);
    }

    public static Piece from(Color color) {
        if (Color.BLACK == color) {
            return BLACK_QUEEN;
        }
        return WHITE_QUEEN;
    }

    @Override
    public MovementAnalysis analyze(Movement movement, History history) {
        MovementValidator movementValidator = getDefaultValidator(movement);
        Map<Position, Piece> changes = getDefaultChanges(movement);
        return new MovementAnalysis(movementValidator, changes);
    }

    @Override
    public boolean canMove(Movement movement) {
        return movement.isDiagonal() || movement.isSameFileOrRank();
    }
}
