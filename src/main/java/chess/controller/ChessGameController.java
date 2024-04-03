package chess.controller;

import chess.dto.BoardDTOForView;
import chess.dto.ScoreDTO;
import chess.model.board.Board;
import chess.model.piece.Color;
import chess.model.position.Movement;
import chess.model.position.Position;
import chess.model.score.ScoreGenerator;
import chess.service.BoardService;
import chess.view.Command;
import chess.view.InputView;
import chess.view.OutputView;

import java.util.function.Supplier;

public class ChessGameController {
    private final OutputView outputView;
    private final InputView inputView;
    private final BoardService boardService;

    public ChessGameController(OutputView outputView, InputView inputView, BoardService boardService) {
        this.outputView = outputView;
        this.inputView = inputView;
        this.boardService = boardService;
    }

    public void run() {
        outputView.printGameIntro();
        Command command = retryOnException(this::getValidInitCommand);
        if (command == Command.SEARCH) {
            retryOnException(this::searchGameResult);
            return;
        }
        start();
    }

    private Command getValidInitCommand() {
        Command command = getValidCommand();
        if (command == Command.START || command == Command.SEARCH) {
            return command;
        }
        throw new IllegalArgumentException("start 혹은 search를 입력해 주세요.");
    }

    private void searchGameResult() {
        String boardName = inputView.askSearchedBoardName();
        String winnerColor = boardService.getWinnerOfBoard(boardName);
        outputView.printWinner(winnerColor);
    }

    private void start() {
        Board board = retryOnException(this::getOrCreateBoard);
        GameStatus gameStatus = new GameStatus();
        showBoard(board);
        while (gameStatus.isRunning()) {
            retryOnException(() -> playTurn(gameStatus, board));
        }
    }

    private Board getOrCreateBoard() {
        String boardName = inputView.askBoardName();
        return boardService.getOrCreateBoard(boardName);
    }

    private void showBoard(Board board) {
        BoardDTOForView boardDTO = BoardDTOForView.from(board);
        outputView.printBoard(boardDTO);
    }

    private void playTurn(GameStatus gameStatus, Board board) {
        Command command = inputView.askCommand();
        validateCommandNotStart(command);
        if (command == Command.MOVE) {
            moveAndShowResult(gameStatus, board);
            return;
        }
        if (command == Command.END) {
            gameStatus.stop();
            return;
        }
        showResult(board);
    }

    private void validateCommandNotStart(Command command) {
        if (command == Command.START) {
            throw new IllegalArgumentException("게임이 이미 시작되었습니다.");
        }
    }

    private void moveAndShowResult(GameStatus gameStatus, Board board) {
        move(board);
        showBoard(board);
        Color winnerColor = board.getWinnerColor();
        if (winnerColor == Color.NONE) {
            return;
        }
        boardService.addBoardWinner(board, winnerColor);
        gameStatus.stop();
        outputView.printWinner(winnerColor.name());
    }

    private void move(Board board) {
        Position source = inputView.askPosition();
        Position destination = inputView.askPosition();
        Movement movement = new Movement(source, destination);
        boardService.moveBoard(board, movement);
    }

    private void showResult(Board board) {
        ScoreGenerator scoreGenerator = new ScoreGenerator(board);
        ScoreDTO scoreDTO = scoreGenerator.calculateScore();
        outputView.printScore(scoreDTO);
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
