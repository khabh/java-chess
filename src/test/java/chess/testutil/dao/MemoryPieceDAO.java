package chess.testutil.dao;

import chess.dao.PieceDAO;
import chess.dto.PieceDTO;

import java.util.ArrayList;
import java.util.List;

public class MemoryPieceDAO implements PieceDAO {
    private List<PieceDTO> pieces = new ArrayList<>();

    @Override
    public void saveAll(List<PieceDTO> pieceDTOs) {
        pieces.addAll(pieceDTOs);
    }

    @Override
    public void deleteAll() {
        pieces = new ArrayList<>();
    }

    @Override
    public List<PieceDTO> findAll() {
        return new ArrayList<>(pieces);
    }
}
