package chess.dao;

import chess.dto.TurnDTO;

import java.util.Optional;

public interface TurnDAO {
    void save(TurnDTO turnDTO);

    void deleteALl();

    Optional<TurnDTO> findOne();
}
