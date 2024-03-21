package chess.model.board;

import chess.model.piece.*;

import java.util.HashMap;
import java.util.Map;

public class InitialBoardGenerator implements BoardGenerator {
    private static final Map<Position, Piece> squares = new HashMap<>();

    static {
        squares.put(Position.from(1, 8), Rook.from(Color.BLACK));
        squares.put(Position.from(2, 8), Knight.from(Color.BLACK));
        squares.put(Position.from(3, 8), Bishop.from(Color.BLACK));
        squares.put(Position.from(4, 8), Queen.from(Color.BLACK));
        squares.put(Position.from(5, 8), King.from(Color.BLACK));
        squares.put(Position.from(6, 8), Bishop.from(Color.BLACK));
        squares.put(Position.from(7, 8), Knight.from(Color.BLACK));
        squares.put(Position.from(8, 8), Rook.from(Color.BLACK));
        for (int x = 1; x <= 8; x++) {
            squares.put(Position.from(x, 7), Pawn.from(Color.BLACK));
        }
        for (int x = 1; x <= 8; x++) {
            squares.put(Position.from(x, 2), Pawn.from(Color.WHITE));
        }
        squares.put(Position.from(1, 1), Rook.from(Color.WHITE));
        squares.put(Position.from(2, 1), Knight.from(Color.WHITE));
        squares.put(Position.from(3, 1), Bishop.from(Color.WHITE));
        squares.put(Position.from(4, 1), Queen.from(Color.WHITE));
        squares.put(Position.from(5, 1), King.from(Color.WHITE));
        squares.put(Position.from(6, 1), Bishop.from(Color.WHITE));
        squares.put(Position.from(7, 1), Knight.from(Color.WHITE));
        squares.put(Position.from(8, 1), Rook.from(Color.WHITE));
    }


    @Override
    public Board create() {
        return new Board(squares);
    }
}
