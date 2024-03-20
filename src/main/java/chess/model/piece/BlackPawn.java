package chess.model.piece;

import chess.model.board.Position;

public class Pawn extends Piece {
    private static final int BLACK_DIRECTION = 1;
    private static final int WHITE_DIRECTION = -1;
    private static final Pawn BLACK_PAWN = new Pawn(Color.BLACK, BLACK_DIRECTION);
    private static final Pawn WHITE_PAWN = new Pawn(Color.WHITE, WHITE_DIRECTION);

    private final int rankMoveDirection;

    private Pawn(Color color, int rankMoveDirection) {
        super(color, Type.PAWN);
        this.rankMoveDirection = rankMoveDirection;
    }

    public static Pawn getBlackPiece() {
        return BLACK_PAWN;
    }

    public static Pawn getWhitePiece() {
        return WHITE_PAWN;
    }

    @Override
    boolean canMove(Movement movement, Piece target) {
        int rankGap = movement.getRankGap();
        if (Integer.signum(rankGap) != rankMoveDirection) {
            throw new IllegalArgumentException("폰의 후퇴");
        }
        int fileGap = Math.abs(movement.getFileGap());
        if (Math.abs(rankGap) == 2 && fileGap == 0 && )
        if (fileGap == 0 && Math.abs(rankGap) == 1 || fileGap == 1 && ) {
            return true;
        }
        if (fileGap == 1 && )
        return false;
    }
}
