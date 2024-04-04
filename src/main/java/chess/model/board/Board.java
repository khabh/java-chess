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
    private Map<Position, Piece> squares;
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
        if (isCheckmate(Color.BLACK)) {
            return Color.WHITE;
        }
        if (isCheckmate(Color.WHITE)) {
            return Color.BLACK;
        }
        return Color.NONE;
    }

    private boolean isCheckmate(Color color) {
        Piece king = King.from(color);
        Color oppositeColor = color.getOpposite();
        List<Position> oppositePositions = squares.keySet()
                .stream()
                .filter(position -> getByPosition(position).hasColor(oppositeColor))
                .toList();
        return squares.values()
                .stream()
                .noneMatch(piece -> piece.equals(king));
    }

    private boolean isChecked(Color color) {
        Position kingPosition = getKingPosition(color);
        List<Position> oppositePositions = getPositionsOf(color.getOpposite());
        return oppositePositions.stream()
                .anyMatch(oppositePosition -> canMove(oppositePosition, kingPosition));
    }

    private boolean canMove(Position source, Position target) {
        Movement movement = new Movement(source, target);
        Piece sourcePiece = getByPosition(source);
        Color color = sourcePiece.getColor();
        try {
            validateMovement(movement, color);
            MovementAnalysis movementAnalysis = sourcePiece.analyze(movement, history);
            validateSquareColors(movementAnalysis);
            return true;
        } catch (Exception e) {
            System.out.println(source.getFile() + " " + source.getRank());
            return false;
        }
    }

    private Position getKingPosition(Color color) {
        Piece king = King.from(color);
        return squares.keySet()
                .stream()
                .filter(position -> getByPosition(position).equals(king))
                .findFirst()
                .orElseThrow();
    }

    private List<Position> getPositionsOf(Color color) {
        return squares.keySet()
                .stream()
                .filter(position -> getByPosition(position).hasColor(color))
                .toList();
    }

    public void move(Movement movement) {
        Color currentColor = history.getCurrentColor();
        validateMovement(movement, currentColor);
        Piece sourcePiece = getSourcePiece(movement);
        MovementAnalysis movementAnalysis = sourcePiece.analyze(movement, history);
        validateSquareColors(movementAnalysis);
        boolean isEscapeNeeded = isChecked(currentColor);
        Map<Position, Piece> prevSquares = new HashMap<>(squares);
        changeSquares(movementAnalysis);
        history.addChange(new Change(movement, sourcePiece));
        if (isEscapeNeeded && isChecked(currentColor)) {
            history.removeLast();
            squares = prevSquares;
            throw new IllegalArgumentException("체크를 벗어나는 수를 두세오");
        }
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
        Map<Position, Piece> prev = new HashMap<>(squares);
        Map<Position, Piece> changes = movementAnalysis.getChanges();
        System.out.println(changes);
        for (Position position : changes.keySet()) {
            if (changes.get(position).equals(EMPTY)) {
                squares.remove(position);
                continue;
            }
            squares.put(position, prev.getOrDefault(position, EMPTY));
        }
    }

    private void validateMovement(Movement movement, Color sourceColor) {
        Piece sourcePiece = getSourcePiece(movement);
        Piece targetPiece = getTargetOf(movement);
        if (!sourcePiece.hasColor(sourceColor)) {
            throw new IllegalArgumentException("현재 턴에 맞는 색상을 움직여 주세요");
        }
        if (targetPiece.hasColor(sourceColor)) {
            throw new IllegalArgumentException("동일한 색상이 있는 기물로는 움직일 수 없습니다.");
        }
        if (!sourcePiece.canMove(movement)) {
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
