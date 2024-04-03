package chess.dao;

import chess.db.ConnectionManager;
import chess.dto.MoveDTO;
import chess.dto.MoveDTOForSave;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MoveDAO {
    private final ConnectionManager connectionManager;

    public MoveDAO(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void save(MoveDTOForSave moveDTO) {
        try (Connection connection = getConnection()) {
            String query = "INSERT INTO moves (board_id, source_file, source_rank, target_file, target_rank) VALUES (?, ?, ?, ?, ?) ";
            PreparedStatement insertStatement = connection.prepareStatement(query);
            insertStatement.setInt(1, moveDTO.boardId());
            insertStatement.setInt(2, moveDTO.source().getFile());
            insertStatement.setInt(3, moveDTO.source().getRank());
            insertStatement.setInt(4, moveDTO.target().getFile());
            insertStatement.setInt(5, moveDTO.target().getRank());
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("move 저장 실패", e);
        }
    }

    public List<MoveDTO> findByBoard(int boardId) {
        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM moves WHERE board_id = ?";
            PreparedStatement selectStatement = connection.prepareStatement(query);
            selectStatement.setInt(1, boardId);
            ResultSet selectedMoves = selectStatement.executeQuery();
            return convertToMoves(selectedMoves);
        } catch (SQLException e) {
            throw new DatabaseException("board 저장 실패", e);
        }
    }

    private List<MoveDTO> convertToMoves(ResultSet selectedMoves) throws SQLException {
        List<MoveDTO> moveDTOs = new ArrayList<>();
        if (selectedMoves.next()) {
            int sourceFile = selectedMoves.getInt("source_file");
            int sourceRank = selectedMoves.getInt("source_rank");
            int targetFile = selectedMoves.getInt("target_file");
            int targetRank = selectedMoves.getInt("target_rank");
            MoveDTO moveDTO = new MoveDTO(sourceFile, sourceRank, targetFile, targetRank);
            moveDTOs.add(moveDTO);
        }
        return moveDTOs;
    }

    private Connection getConnection() {
        return connectionManager.getConnection();
    }
}
