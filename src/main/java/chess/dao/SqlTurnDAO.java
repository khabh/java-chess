package chess.dao;

import chess.db.ConnectionManager;
import chess.dto.TurnDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class SqlTurnDAO implements TurnDAO {
    private static final String TABLE_NAME = "turns";
    private static final String INSERT_QUERY = "INSERT INTO " + TABLE_NAME + " VALUES (?)";
    private static final String DELETE_QUERY = "DELETE FROM " + TABLE_NAME;
    private static final String SELECT_QUERY = "SELECT * FROM " + TABLE_NAME;

    private final ConnectionManager connectionManager;

    public SqlTurnDAO(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void save(TurnDTO turnDTO) {
        try (Connection connection = getConnection()) {
            PreparedStatement insertStatement = connection.prepareStatement(INSERT_QUERY);
            insertStatement.setString(1, turnDTO.currentColor());
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("turn 저장 실패", e);
        }
    }

    @Override
    public void deleteALl() {
        try (Connection connection = getConnection()) {
            PreparedStatement deleteStatement = connection.prepareStatement(DELETE_QUERY);
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("turn 전체 삭제 실패", e);
        }
    }

    @Override
    public Optional<TurnDTO> findOne() {
        try (Connection connection = getConnection()) {
            PreparedStatement selectStatement = connection.prepareStatement(SELECT_QUERY);
            ResultSet selectedTurn = selectStatement.executeQuery();
            return convertToTurn(selectedTurn);
        } catch (SQLException e) {
            throw new DatabaseException("turn 조회 실패", e);
        }
    }

    private Optional<TurnDTO> convertToTurn(ResultSet selectedTurn) throws SQLException {
        if (selectedTurn.next()) {
            String currentColor = selectedTurn.getString("current_color");
            TurnDTO turnDTO = new TurnDTO(currentColor);
            return Optional.of(turnDTO);
        }
        return Optional.empty();
    }

    private Connection getConnection() {
        return connectionManager.getConnection();
    }
}
