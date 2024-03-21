package chess.model.board;

import chess.model.piece.Empty;
import chess.model.piece.Movement;
import chess.model.piece.Piece;
import chess.model.piece.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Board {
    public static final int MAX_LENGTH = 8;
    public static final int MIN_LENGTH = 1;
    private static final List<Position> ALL_POSITIONS = Position.values();

    private final Map<Position, Piece> squares;

    public Board(Map<Position, Piece> squares) {
        this.squares = new HashMap<>(squares);
        ALL_POSITIONS.forEach(position -> this.squares.putIfAbsent(position, Empty.getInstance()));
    }

    public List<String> getSignatures() {
        return squares.values().stream()
                .map(Piece::getSignature)
                .toList();
    }

    public List<List<String>> getLines() {
        List<List<String>> lines = new ArrayList<>();
        for (int rank = MAX_LENGTH; rank >= MIN_LENGTH; rank--) {
            lines.add(getLine(rank));
        }
        return lines;
    }

    private List<String> getLine(int lineIndex) {
        return IntStream.rangeClosed(MIN_LENGTH, MAX_LENGTH)
                .mapToObj(file -> squares.get(Position.from(file, lineIndex)).getSignature())
                .toList();
    }

    public void move(Position source, Position destination) {
        Movement movement = new Movement(source, destination);
        Piece sourcePiece = squares.get(source);
        Piece targetPiece = squares.get(destination);
        if (sourcePiece.isSameColorWith(targetPiece)) {
            throw new IllegalArgumentException("같은 색깔인 기물은 먹을 수 없음");
        }
        if (!sourcePiece.isValid(movement)) {
            throw new IllegalArgumentException("올바르지 않은 움직임");
        }
        if (sourcePiece.isType(Type.PAWN)) {
            validatePawn(movement, targetPiece);
        }
    }

    private void validatePawn(Movement movement, Piece targetPiece) {
        if (targetPiece.isEmpty() && movement.isVerticalMove() || movement.isDiagonal()) {
            return;
        }
        throw new IllegalArgumentException("올바르지 않은 움직임");
    }
}
