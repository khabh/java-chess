package chess.model.piece;

public abstract class Piece {
//    BLACK_BISHOP(Color.BLACK, Type.BISHOP),
//    BLACK_ROOK(Color.BLACK, Type.ROOK),
//    BLACK_QUEEN(Color.BLACK, Type.QUEEN),
//    BLACK_KNIGHT(Color.BLACK, Type.KNIGHT),
//    BLACK_PAWN(Color.BLACK, Type.PAWN),
//    BLACK_KING(Color.BLACK, Type.KING),
//    WHITE_BISHOP(Color.WHITE, Type.BISHOP),
//    WHITE_ROOK(Color.WHITE, Type.ROOK),
//    WHITE_QUEEN(Color.WHITE, Type.QUEEN),
//    WHITE_KNIGHT(Color.WHITE, Type.KNIGHT),
//    WHITE_PAWN(Color.WHITE, Type.PAWN),
//    WHITE_KING(Color.WHITE, Type.KING),
//    EMPTY(Color.NONE, Type.NONE);

    private final Color color;
    private final Type type;

    Piece(Color color, Type type) {
        this.color = color;
        this.type = type;
    }

    public abstract boolean isValid(Movement movement);

    public String getSignature() {
        Signature signature = type.getSignature();
        if (Color.BLACK == color) {
            return signature.getBlack();
        }
        return signature.getWhite();
    }

    public boolean isType(Type type) {
        return this.type == type;
    }

    public boolean isEmpty() {
        return isType(Type.NONE);
    }

    public boolean isSameColorWith(Piece piece) {
        return color == piece.color;
    }
}
