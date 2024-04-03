package chess;

import chess.controller.ChessGameController;
import chess.dao.BoardDAO;
import chess.dao.MoveDAO;
import chess.dao.WinnerDAO;
import chess.db.ConnectionManager;
import chess.service.BoardService;
import chess.view.InputView;
import chess.view.OutputView;

public class Application {
    public static void main(String[] args) {
        ConnectionManager connectionManager = new ConnectionManager("chess");
        BoardDAO boardDAO = new BoardDAO(connectionManager);
        MoveDAO moveDAO = new MoveDAO(connectionManager);
        WinnerDAO winnerDAO = new WinnerDAO(connectionManager);

        ChessGameController chessGameController = new ChessGameController(new OutputView(), new InputView(),
                new BoardService(boardDAO, moveDAO, winnerDAO));
        chessGameController.run();
    }
}
