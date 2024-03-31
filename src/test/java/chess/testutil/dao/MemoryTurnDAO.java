package chess.testutil.dao;

import chess.dao.TurnDAO;
import chess.dto.TurnDTO;

import java.util.Objects;
import java.util.Optional;

public class MemoryTurnDAO implements TurnDAO {
    private TurnDTO turnDTO;

    @Override
    public void save(TurnDTO turnDTO) {
        this.turnDTO = turnDTO;
    }

    @Override
    public void deleteALl() {
        turnDTO = null;
    }

    @Override
    public Optional<TurnDTO> findOne() {
        if (Objects.isNull(turnDTO)) {
            return Optional.empty();
        }
        return Optional.of(turnDTO);
    }
}
