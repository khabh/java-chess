package chess.service;

import chess.dao.BoardDAO;
import chess.dao.MoveDAO;
import chess.dao.WinnerDAO;
import chess.dto.*;
import chess.model.board.Board;
import chess.model.board.InitialBoardGenerator;
import chess.model.piece.Color;
import chess.model.position.Movement;
import chess.model.position.Position;

import java.util.List;
import java.util.Optional;

public class BoardService {
    private final BoardDAO boardDAO;
    private final MoveDAO moveDAO;
    private final WinnerDAO winnerDAO;

    public BoardService(BoardDAO boardDAO, MoveDAO moveDAO, WinnerDAO winnerDAO) {
        this.boardDAO = boardDAO;
        this.moveDAO = moveDAO;
        this.winnerDAO = winnerDAO;
    }

    public String getWinnerOfBoard(String boardName) {
        BoardDTO boardDTO = boardDAO.findByName(boardName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방 이름입니다."));
        WinnerDTO winnerDTO = winnerDAO.findByBoard(boardDTO.id())
                .orElseThrow(() -> new IllegalArgumentException("아직 게임 결과가 생성되지 않았습니다."));
        return winnerDTO.color();
    }

    public Board getOrCreateBoard(String boardName) {
        Optional<Board> existingBoard = getExistingBoard(boardName);
        return existingBoard.orElseGet(() -> createBoard(boardName));
    }

    private Optional<Board> getExistingBoard(String boardName) {
        Optional<BoardDTO> boardDTO = boardDAO.findByName(boardName);
        if (boardDTO.isEmpty()) {
            return Optional.empty();
        }
        validateBoardNotEnded(boardDTO.get());
        return Optional.of(getBoard(boardName));
    }

    private void validateBoardNotEnded(BoardDTO boardDTO) {
        int boardId = boardDTO.id();
        Optional<WinnerDTO> winnerDTO = winnerDAO.findByBoard(boardId);
        if (winnerDTO.isPresent()) {
            throw new IllegalArgumentException("이미 게임이 종료된 방입니다.");
        }
    }

    public Board getBoard(String boardName) {
        BoardDTO boardDTO = boardDAO.findByName(boardName)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 방 이름입니다."));
        List<MoveDTO> moveDTOs = moveDAO.findByBoard(boardDTO.id());
        return convertToBoard(boardDTO, moveDTOs);
    }

    private Board convertToBoard(BoardDTO boardDTO, List<MoveDTO> moveDTOs) {
        int boardId = boardDTO.id();
        Board board = new InitialBoardGenerator().create(boardId);
        for (MoveDTO moveDTO : moveDTOs) {
            Position source = Position.of(moveDTO.sourceFile(), moveDTO.sourceRank());
            Position target = Position.of(moveDTO.targetFile(), moveDTO.targetRank());
            board.move(new Movement(source, target));
        }
        return board;
    }

    public Board createBoard(String boardName) {
        BoardDTOForSave boardDTO = new BoardDTOForSave(boardName);
        int boardId = boardDAO.save(boardDTO);
        return new InitialBoardGenerator().create(boardId);
    }

    public void moveBoard(Board board, Movement movement) {
        board.move(movement);
        addMovement(board.getId(), movement);
    }

    private void addMovement(int boardId, Movement movement) {
        Position source = movement.getSource();
        Position target = movement.getTarget();
        MoveDTOForSave moveDTO = new MoveDTOForSave(boardId, source, target);
        moveDAO.save(moveDTO);
    }

    public void addBoardWinner(Board board, Color winnerColor) {
        WinnerDTOForSave winnerDTO = new WinnerDTOForSave(board.getId(), winnerColor.name());
        winnerDAO.save(winnerDTO);
    }
}
