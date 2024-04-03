package chess.model.piece;

import chess.model.board.History;
import chess.model.board.MovementAnalysis;
import chess.model.board.MovementValidator;
import chess.model.position.FileDirection;
import chess.model.position.Movement;
import chess.model.position.Position;

import java.util.List;
import java.util.Map;

public class King extends Piece {
    private static final int CASTLING_DISTANCE = 2;
    private static final int COMMON_MOVE_DISTANCE = 1;
    private static final Piece BLACK_KING = new King(Color.BLACK, Position.of(5, 8));
    private static final Piece WHITE_KING = new King(Color.WHITE, Position.of(5, 1));

    private final Position startPosition;

    private King(Color color, Position startPosition) {
        super(color, 0.0);
        this.startPosition = startPosition;
    }

    public static Piece from(Color color) {
        if (Color.BLACK == color) {
            return BLACK_KING;
        }
        return WHITE_KING;
    }

    @Override
    public MovementAnalysis analyze(Movement movement, History history) {
        if (isCommonMove(movement)) {
            return new MovementAnalysis(MovementValidator.createEmpty(), getDefaultChanges(movement));
        }
        validateCastling(movement, history);
        return new MovementAnalysis(getCastlingValidator(movement), getCastlingChanges(movement));
    }

    private boolean isCommonMove(Movement movement) {
        return movement.getFileGap() == COMMON_MOVE_DISTANCE || movement.getRankGap() == COMMON_MOVE_DISTANCE;
    }

    private void validateCastling(Movement movement, History history) {
        Position rookPosition = getCastlingRook(movement);
        if (history.isChanged(rookPosition) || history.isChanged(startPosition)) {
            throw new IllegalArgumentException("잘못된 캐슬링");
        }
    }

    private Map<Position, Piece> getCastlingChanges(Movement movement) {
        Position currentRookPosition = getCastlingRook(movement);
        Position changedRookPosition = movement.getIntermediatePositions().get(0);
        Movement rookMove = new Movement(currentRookPosition, changedRookPosition);
        Map<Position, Piece> changes = getDefaultChanges(movement);
        changes.putAll(getDefaultChanges(rookMove));
        return changes;
    }

    private MovementValidator getCastlingValidator(Movement movement) {
        Position rookPosition = getCastlingRook(movement);
        List<Position> positions = new Movement(movement.getSource(), rookPosition)
                .getIntermediatePositions();
        return MovementValidator.createWithEmptyColor(positions);
    }

    private Position getCastlingRook(Movement movement) {
        Position targetPosition = movement.getTarget();
        int targetFile = targetPosition.getFile();
        int targetRank = targetPosition.getRank();
        if (FileDirection.LEFT.match(movement)) {
            return Position.of(targetFile - 2, targetRank);
        }
        return Position.of(targetFile + 1, targetFile);
    }

    @Override
    public boolean canMove(Movement movement, Piece target) {
        validateTargetColor(target);
        if (isCommonMove(movement)) {
            return true;
        }
        return movement.isSameRank() && movement.getFileDistance() == CASTLING_DISTANCE;
    }
}
