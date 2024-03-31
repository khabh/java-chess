package chess.dao;

import chess.dto.PieceDTO;

import java.util.List;

public interface PieceDAO{
    void saveAll(List<PieceDTO> pieceDTOs);

    void deleteAll();

    List<PieceDTO> findAll();
}
