package chess.model.piece;

import chess.model.board.History;
import chess.model.board.MovementAnalysis;
import chess.model.board.MovementValidator;
import chess.model.position.Movement;
import chess.model.position.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Piece {
    private final Color color;
    private final double score;

    Piece(Color color, double score) {
        this.color = color;
        this.score = score;
    }

    public abstract MovementAnalysis analyze(Movement movement, History history);

    public abstract boolean canMove(Movement movement, Piece target);

    protected void validateCurrentColor(History history) {
        if (history.getCurrentColor() == color) {
            return;
        }
        throw new IllegalArgumentException("현재 턴에 맞는 기물을 선택해 주세요.");
    }

    protected void validateTargetColor(Piece target) {
        if (target.hasColor(color)) {
            throw new IllegalArgumentException("동일한 색상의 기물이 있는 위치로 움직일 수 없습니다.");
        }
    }

    protected MovementValidator getDefaultValidator(Movement movement) {
        List<Position> positions = movement.getIntermediatePositions();
        return MovementValidator.createWithEmptyColor(positions);
    }

    protected Map<Position, Piece> getDefaultChanges(Movement movement) {
        Map<Position, Piece> changes = new HashMap<>();
        changes.put(movement.getSource(), Empty.getInstance());
        changes.put(movement.getTarget(), this);
        return new HashMap<>(changes);
    }

    public Color getOppositeColor() {
        return color.getOpposite();
    }

    public boolean hasSameColorWith(Piece piece) {
        return color == piece.color;
    }

    protected boolean hasOppositeColorWith(Piece piece) {
        return color.getOpposite() == piece.color;
    }

    public boolean isEmpty() {
        return equals(Empty.getInstance());
    }

    public boolean hasColor(Color color) {
        return this.color == color;
    }

    public double getScore() {
        return score;
    }
}
