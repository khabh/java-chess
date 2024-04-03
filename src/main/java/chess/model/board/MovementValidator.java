package chess.model.board;

import chess.model.piece.Color;
import chess.model.position.Position;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovementValidator {
    private final Map<Position, Color> positionsColor;

    public MovementValidator(Map<Position, Color> positionsColor) {
        this.positionsColor = positionsColor;
    }

    public MovementValidator(List<Position> positions, Color color) {
        this.positionsColor = new HashMap<>();
        for (Position position : positions) {
            positionsColor.put(position, color);
        }
    }

    public static MovementValidator createWithEmptyColor(List<Position> positions) {
        return new MovementValidator(positions, Color.NONE);
    }

    public static MovementValidator createEmpty() {
        return new MovementValidator(Collections.emptyMap());
    }

    public Map<Position, Color> getPositionsColor() {
        return Collections.unmodifiableMap(positionsColor);
    }
}
