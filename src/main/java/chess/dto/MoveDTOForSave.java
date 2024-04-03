package chess.dto;

import chess.model.position.Position;

public record MoveDTOForSave(int boardId, Position source, Position target) {
}
