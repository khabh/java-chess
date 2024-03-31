package chess.dao;

import chess.dto.PieceDTO;
import chess.model.piece.Type;
import chess.testutil.db.TestConnectionManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SqlPieceDAOTest {
    private static final List<PieceDTO> SAVED_PIECES = List.of(
            new PieceDTO(3, 2, Type.BLACK_PAWN.name()),
            new PieceDTO(4, 4, Type.WHITE_KING.name()),
            new PieceDTO(7, 8, Type.BLACK_KING.name())
    );

    private static final List<PieceDTO> TEST_SAVED_PIECES = List.of(
            new PieceDTO(7, 7, Type.WHITE_ROOK.name()),
            new PieceDTO(3, 4, Type.WHITE_KNIGHT.name())
    );

    private final SqlPieceDAO sqlPieceDAO = new SqlPieceDAO(TestConnectionManager.getInstance());

    @BeforeEach
    void initPieceTable() {
        sqlPieceDAO.saveAll(SAVED_PIECES);
    }

    @AfterEach
    void rollbackPieceTable() {
        List<PieceDTO> changedPieces = new ArrayList<>(SAVED_PIECES);
        changedPieces.addAll(TEST_SAVED_PIECES);
        try (Connection connection = TestConnectionManager.getConnection()) {
            for (PieceDTO pieceDTO : changedPieces) {
                PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM pieces WHERE piece_file = ? AND piece_rank = ?");
                deleteStatement.setInt(1, pieceDTO.file());
                deleteStatement.setInt(2, pieceDTO.rank());
                deleteStatement.executeUpdate();
            }
        } catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Test
    void 입력된_데이터_전체를_추가한다() {
        sqlPieceDAO.saveAll(TEST_SAVED_PIECES);
        assertThat(sqlPieceDAO.findAll()).containsAll(TEST_SAVED_PIECES);
    }

    @Test
    void 데이터_전체를_삭제한다() {
        sqlPieceDAO.deleteAll();
        assertThat(sqlPieceDAO.findAll().isEmpty()).isTrue();
    }

    @Test
    void 데이터_전체를_조회한다() {
        assertThat(sqlPieceDAO.findAll()).containsAll(SAVED_PIECES);
    }
}
