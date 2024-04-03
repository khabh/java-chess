package chess.dao;

import chess.db.ConnectionManager;
import chess.dto.WinnerDTO;
import chess.dto.WinnerDTOForSave;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class WinnerDAO {
    private final ConnectionManager connectionManager;

    public WinnerDAO(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void save(WinnerDTOForSave winnerDTO) {
        try (Connection connection = getConnection()) {
            String query = "INSERT INTO winners (board_id, color) VALUES (?, ?) ";
            PreparedStatement insertStatement = connection.prepareStatement(query);
            insertStatement.setInt(1, winnerDTO.boardId());
            insertStatement.setString(2, winnerDTO.color());
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("winner 저장 실패", e);
        }
    }

    public Optional<WinnerDTO> findByBoard(int boardId) {
        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM winners WHERE board_id = ?";
            PreparedStatement selectStatement = connection.prepareStatement(query);
            selectStatement.setInt(1, boardId);
            ResultSet selectedWinner = selectStatement.executeQuery();
            return convertToWinner(selectedWinner);
        } catch (SQLException e) {
            throw new DatabaseException("board 저장 실패", e);
        }
    }

    private Optional<WinnerDTO> convertToWinner(ResultSet selectedWinner) throws SQLException {
        if (selectedWinner.next()) {
            String color = selectedWinner.getString("color");
            return Optional.of(new WinnerDTO(color));
        }
        return Optional.empty();
    }

    private Connection getConnection() {
        return connectionManager.getConnection();
    }
}
