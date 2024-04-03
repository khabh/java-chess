package chess.dao;

import chess.db.ConnectionManager;
import chess.dto.BoardDTO;
import chess.dto.BoardDTOForSave;

import java.sql.*;
import java.util.Optional;

public class BoardDAO {
    private final ConnectionManager connectionManager;

    public BoardDAO(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public int save(BoardDTOForSave boardDTO) {
        try (Connection connection = getConnection()) {
            String query = "INSERT INTO boards (name) VALUES (?) ";
            PreparedStatement insertStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1, boardDTO.name());
            insertStatement.executeUpdate();
            ResultSet insertResult = insertStatement.getGeneratedKeys();
            if (insertResult.next()) {
                return insertResult.getInt(1);
            }
            throw new IllegalArgumentException("board 저장 실패");
        } catch (SQLException e) {
            throw new DatabaseException("board 저장 실패", e);
        }
    }

    public Optional<BoardDTO> findByName(String name) {
        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM boards WHERE name = ?";
            PreparedStatement selectStatement = connection.prepareStatement(query);
            selectStatement.setString(1, name);
            ResultSet selectedBoard = selectStatement.executeQuery();
            return convertToBoard(selectedBoard);
        } catch (SQLException e) {
            throw new DatabaseException("board 저장 실패", e);
        }
    }

    private Optional<BoardDTO> convertToBoard(ResultSet selectedBoard) throws SQLException {
        if (selectedBoard.next()) {
            int id = selectedBoard.getInt("id");
            return Optional.of(new BoardDTO(id));
        }
        return Optional.empty();
    }

    private Connection getConnection() {
        return connectionManager.getConnection();
    }
}
