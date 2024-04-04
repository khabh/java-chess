package chess.model.piece;

import chess.model.board.History;
import chess.model.board.MovementAnalysis;
import chess.model.board.MovementValidator;
import chess.model.position.Movement;
import chess.model.position.Position;
import chess.model.position.RankDirection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pawn extends Piece {
    private static final Piece WHITE_PAWN = new Pawn(Color.WHITE, RankDirection.UP, 2);
    private static final Piece BLACK_PAWN = new Pawn(Color.BLACK, RankDirection.DOWN, 7);
    private static final int START_JUMP_DISTANCE = 2;
    private static final int COMMON_RANK_DISTANCE = 1;

    private final RankDirection validRankDirection;
    private final int startRank;

    private Pawn(Color color, RankDirection validRankDirection, int startRank) {
        super(color, 1.0);
        this.validRankDirection = validRankDirection;
        this.startRank = startRank;
    }

    public static Piece from(Color color) {
        if (Color.BLACK == color) {
            return BLACK_PAWN;
        }
        return WHITE_PAWN;
    }

    @Override
    public MovementAnalysis analyze(Movement movement, History history) {
        if (movement.isSameFile()) {
            return analyzeCommonMovement(movement);
        }
        return analyzeAttackMovement(movement, history);
    }

    private MovementAnalysis analyzeCommonMovement(Movement movement) {
        List<Position> positions = movement.getIntermediatePositions();
        positions.add(movement.getTarget());
        MovementValidator movementValidator = MovementValidator.createWithEmptyColor(positions);
        Map<Position, Piece> changes = getDefaultChanges(movement);
        return new MovementAnalysis(movementValidator, changes);
    }

    private MovementAnalysis analyzeAttackMovement(Movement movement, History history) {
        Map<Position, Color> positionsColor = new HashMap<>();
        Map<Position, Piece> changes = getDefaultChanges(movement);
        positionsColor.put(movement.getTarget(), getOppositeColor());
        if (history.isEnPassant(movement)) {
            positionsColor.put(movement.getTarget(), Color.NONE);
            changes.put(getEnPassantTarget(movement), Empty.getInstance());
        }
        return new MovementAnalysis(new MovementValidator(positionsColor), changes);
    }

    private Position getEnPassantTarget(Movement movement) {
        Position source = movement.getSource();
        Position target = movement.getTarget();
        return Position.of(target.getFile(), source.getRank());
    }

    @Override
    public boolean canMove(Movement movement, Piece target) {
        validateTargetColor(target);
        if (!isValidDirection(movement)) {
            return false;
        }
        if (movement.isDiagonal()) {
            return canMoveDiagonally(movement);
        }
        return isValidVerticalMove(movement, target);
    }

    private boolean isValidDirection(Movement movement) {
        return validRankDirection.match(movement);
    }

    private boolean canMoveDiagonally(Movement movement) {
        return movement.getRankDistance() == COMMON_RANK_DISTANCE;
    }

    private boolean isValidVerticalMove(Movement movement, Piece target) {
        if (!movement.isSameFile() || !target.isEmpty()) {
            return false;
        }
        int rankDistance = movement.getRankDistance();
        return rankDistance == COMMON_RANK_DISTANCE ||
                (rankDistance == START_JUMP_DISTANCE && movement.isSourceRankMatch(startRank));
    }
}
