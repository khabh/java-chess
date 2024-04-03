package chess.model.board;

import chess.model.piece.*;
import chess.model.position.Movement;
import chess.model.position.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;

public class Board {
    public static final int MIN_LENGTH = 1;
    public static final int MAX_LENGTH = 8;
    private static final Piece EMPTY = Empty.getInstance();

    private final int id;
    private final Map<Position, Piece> squares;
    private final History history;

    public Board(Map<Position, Piece> squares, Color currentColor) {
        this(0, squares);
    }

    public Board(int id, Map<Position, Piece> squares) {
        this.id = id;
        this.squares = new HashMap<>(squares);
        this.history = new History();
    }

    public Piece getPiece(int file, int rank) {
        Position position = Position.of(file, rank);
        return getByPosition(position);
    }

    public List<Piece> getRank(int rank) {
        return IntStream.rangeClosed(MIN_LENGTH, MAX_LENGTH)
                .mapToObj(file -> Position.of(file, rank))
                .map(this::getByPosition)
                .toList();
    }

    public Map<Piece, Integer> getPieceCount(Color color) {
        return squares.values()
                .stream()
                .filter(piece -> piece.hasColor(color))
                .collect(groupingBy(Function.identity(),
                        collectingAndThen(counting(), Long::intValue)));
    }

    public int countPawnOfFile(Color color, int file) {
        Piece pawn = Pawn.from(color);
        return (int) getFile(file).stream()
                .filter(filePiece -> filePiece.equals(pawn))
                .count();
    }

    private List<Piece> getFile(int file) {
        return IntStream.rangeClosed(MIN_LENGTH, MAX_LENGTH)
                .mapToObj(rank -> Position.of(file, rank))
                .map(this::getByPosition)
                .toList();
    }

    public Color getWinnerColor() {
        Color currentColor = history.getCurrentColor();
        if (isKingCaptured(currentColor)) {
            return currentColor.getOpposite();
        }
        return Color.NONE;
    }

    private boolean isKingCaptured(Color color) {
        Piece king = King.from(color);
        return squares.values()
                .stream()
                .noneMatch(piece -> piece.equals(king));
    }

    public void move(Movement movement) {
        validateMovement(movement);
        Piece sourcePiece = getSourcePiece(movement);
        MovementAnalysis movementAnalysis = sourcePiece.analyze(movement, history);
        validateSquareColors(movementAnalysis);
        changeSquares(movementAnalysis);
        history.addChange(new Change(movement, sourcePiece));
    }

    private void validateSquareColors(MovementAnalysis movementAnalysis) {
        Map<Position, Color> validColors = movementAnalysis.getValidColors();
        for (Position position : validColors.keySet()) {
            Piece piece = getByPosition(position);
            if (!piece.hasColor(validColors.get(position))) {
                throw new IllegalArgumentException("올바르지 않은 움직임입니다.");
            }
        }
    }

    private void changeSquares(MovementAnalysis movementAnalysis) {
        Map<Position, Piece> changes = movementAnalysis.getChanges();
        for (Position position : changes.keySet()) {
            if (changes.get(position).equals(EMPTY)) {
                squares.remove(position);
                continue;
            }
            squares.put(position, changes.get(position));
        }
    }

    private void validateMovement(Movement movement) {
        Piece sourcePiece = getSourcePiece(movement);
        Piece targetPiece = getTargetOf(movement);
        if (!sourcePiece.canMove(movement, targetPiece)) {
            throw new IllegalArgumentException("올바르지 않은 움직임입니다.");
        }
    }

    private Piece getSourcePiece(Movement movement) {
        Position source = movement.getSource();
        return getByPosition(source);
    }

    private Piece getTargetOf(Movement movement) {
        Position destination = movement.getTarget();
        return getByPosition(destination);
    }

    private Piece getByPosition(Position position) {
        return squares.getOrDefault(position, EMPTY);
    }

    public int getId() {
        return id;
    }
}
