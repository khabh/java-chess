package chess.controller;

import chess.dto.BoardDTO;
import chess.dto.PositionDTO;
import chess.model.board.Board;
import chess.model.board.InitialBoardGenerator;
import chess.model.position.Movement;
import chess.view.Command;
import chess.view.InputView;
import chess.view.OutputView;

import java.util.function.Supplier;

public class ChessGameController {

    private final OutputView outputView;
    private final InputView inputView;

    public ChessGameController(OutputView outputView, InputView inputView) {
        this.outputView = outputView;
        this.inputView = inputView;
    }

    public void run() {
        outputView.printGameIntro();
        while (getValidCommand() != Command.START) {
            outputView.printException("게임을 시작하려면 start를 입력하세요.");
        }
        start();
    }

    private void start() {
        Board board = new InitialBoardGenerator().create();
        GameStatus gameStatus = new GameStatus();
        showBoard(board);
        while (gameStatus.isRunning()) {
            retryOnException(() -> playTurn(gameStatus, board));
        }
    }

    private void showBoard(Board board) {
        BoardDTO boardDTO = BoardDTO.from(board);
        outputView.printBoard(boardDTO);
    }

    private void playTurn(GameStatus gameStatus, Board board) {
        Command command = inputView.askCommand();
        if (command == Command.START) {
            throw new IllegalArgumentException("게임이 이미 시작되었습니다.");
        }
        if (command == Command.END) {
            gameStatus.stop();
            return;
        }
        move(board);
        showBoard(board);
    }

    private void move(Board board) {
        PositionDTO sourcePositionDTO = inputView.askPosition();
        PositionDTO targetPositionDTO = inputView.askPosition();
        Movement movement = new Movement(sourcePositionDTO.toEntity(), targetPositionDTO.toEntity());
        board.move(movement);
    }

    private Command getValidCommand() {
        return retryOnException(inputView::askCommand);
    }

    private void retryOnException(Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException e) {
            outputView.printException(e.getMessage());
            retryOnException(action);
        }
    }

    private <T> T retryOnException(Supplier<T> retryOperation) {
        try {
            return retryOperation.get();
        } catch (IllegalArgumentException e) {
            outputView.printException(e.getMessage());
            return retryOnException(retryOperation);
        }
    }
}
