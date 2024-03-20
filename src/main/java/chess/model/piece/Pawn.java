package chess.model.piece;

public class Pawn extends Piece {
    private static final int START_JUMP_GAP = 2;

    private final int moveDirection;
    private final int startRank;

    private Pawn(Color color, int moveDirection, int startRank) {
        super(color, Type.PAWN);
        this.moveDirection = moveDirection;
        this.startRank = startRank;
    }

    public static Pawn createBlackPawn() {
        return new Pawn(Color.BLACK, 1, 2);
    }

    public static Pawn createWhitePawn() {
        return new Pawn(Color.WHITE, -1, 7);
    }

    @Override
    void validateMovement(Movement movement, Piece target) {
        validateDirection(movement);
        if (movement.isDiagonal()) {
            validateDiagonal(movement, target);
            return;
        }
        if (isValidVerticalMove(movement, target)) {
            throw new IllegalArgumentException("올바르지 않은 폰의 이동");
        }
    }

    private void validateDiagonal(Movement movement, Piece target) {
        if (target.isEmpty()) {
            throw new IllegalArgumentException("폰은 상대 기물이 있을 때만 대각선 이동이 가능");
        }
        if (movement.getFileGap() != 1) {
            throw new IllegalArgumentException("폰은 대각선으로 한 칸만 움직일 수 있음");
        }
    }

    private void validateDirection(Movement movement) {
        if (Integer.signum(movement.getRankGap()) != moveDirection) {
            throw new IllegalArgumentException("폰은 뒤로 이동할 수 없습니다.");
        }
    }

    private boolean isValidVerticalMove(Movement movement, Piece target) {
        if (!movement.isVerticalMove() || !target.isEmpty()) {
            return false;
        }
        int rankGap = movement.getRankGap();
        if (rankGap == moveDirection) {
            return true;
        }
        return Math.abs(rankGap) == START_JUMP_GAP && movement.isSourceRankMatch(startRank);
    }
}
