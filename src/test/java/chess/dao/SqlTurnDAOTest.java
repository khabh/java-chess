package chess.dao;

import chess.dto.TurnDTO;
import chess.model.piece.Color;
import chess.testutil.db.TestConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SqlTurnDAOTest {
    private final SqlTurnDAO sqlTurnDAO = new SqlTurnDAO(TestConnectionManager.getInstance());

    @BeforeEach
    void initTurnTable() {
        try (Connection connection = TestConnectionManager.getConnection()) {
            PreparedStatement truncateStatement = connection.prepareStatement("TRUNCATE TABLE turns");
            truncateStatement.executeUpdate();
        } catch (final SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Test
    void 입력된_데이터를_추가한다() {
        String colorName = Color.WHITE.name();
        TurnDTO turnDTO = new TurnDTO(colorName);
        sqlTurnDAO.save(turnDTO);
        TurnDTO savedTurnDTO = sqlTurnDAO.findOne()
                .orElseThrow(() -> new IllegalStateException("조회된 Turn이 없습니다."));
        assertThat(savedTurnDTO).isEqualTo(turnDTO);
    }

    @Test
    void 데이터_전체를_삭제한다() {
        sqlTurnDAO.deleteALl();
        Optional<TurnDTO> turnDTO = sqlTurnDAO.findOne();
        assertThat(turnDTO.isEmpty()).isTrue();
    }
}
