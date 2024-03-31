package chess;

import chess.controller.ChessGameController;
import chess.dao.PieceDAO;
import chess.dao.SqlPieceDAO;
import chess.dao.SqlTurnDAO;
import chess.dao.TurnDAO;
import chess.db.ConnectionManager;
import chess.service.BoardService;
import chess.view.InputView;
import chess.view.OutputView;

public class Application {
    public static void main(String[] args) {
        ConnectionManager connectionManager = new ConnectionManager("chess");
        PieceDAO pieceDAO = new SqlPieceDAO(connectionManager);
        TurnDAO turnDAO = new SqlTurnDAO(connectionManager);

        ChessGameController chessGameController = new ChessGameController(new OutputView(), new InputView(),
                new BoardService(pieceDAO, turnDAO));
        chessGameController.run();
    }
}
