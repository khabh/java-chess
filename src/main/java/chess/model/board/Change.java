package chess.model.board;

import chess.model.piece.Color;
import chess.model.piece.Piece;
import chess.model.piece.Type;
import chess.model.position.Movement;
import chess.model.position.Position;

import java.util.List;

public class Change {

    private final Movement movement;
    private final Piece piece;

    public Change(Movement movement, Piece piece) {
        this.movement = movement;
        this.piece = piece;
    }

    public Color getOppositeColor() {
        return piece.getOppositeColor();
    }

    public Position getSourcePosition() {
        return movement.getSource();
    }

    public boolean matchEnPassant(Movement movement) {
        if (isNotPawnType()) return false;
        List<Position> positions = this.movement.getIntermediatePositions();
        if (positions.isEmpty()) {
            return false;
        }
        Position validEnPassantPosition = positions.get(0);
        return validEnPassantPosition.equals(movement.getTarget());
    }

    private boolean isNotPawnType() {
        Type pieceType = Type.from(piece);
        return pieceType != Type.BLACK_PAWN && pieceType != Type.WHITE_PAWN;
    }
}
