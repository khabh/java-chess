package chess.dao;

import chess.db.ConnectionManager;
import chess.dto.TurnDTO;
import chess.model.piece.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TurnDAOTest {
    private static final ConnectionManager CONNECTION_MANAGER = new ConnectionManager("chess_test");

    private final TurnDAO turnDAO = new TurnDAO(CONNECTION_MANAGER);

    @BeforeEach
    void initTurnTable() {
        try (Connection connection = CONNECTION_MANAGER.getConnection()) {
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
        turnDAO.save(turnDTO);
        TurnDTO savedTurnDTO = turnDAO.findOne()
                .orElseThrow(() -> new IllegalStateException("조회된 Turn이 없습니다."));
        assertThat(savedTurnDTO).isEqualTo(turnDTO);
    }

    @Test
    void 데이터_전체를_삭제한다() {
        turnDAO.deleteALl();
        Optional<TurnDTO> turnDTO = turnDAO.findOne();
        assertThat(turnDTO.isEmpty()).isTrue();
    }
}
