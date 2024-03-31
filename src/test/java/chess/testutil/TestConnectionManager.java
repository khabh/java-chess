package chess.testutil;

import chess.db.ConnectionManager;

import java.sql.Connection;

public class TestConnectionManager {
    private static final ConnectionManager TEST_CONNECTION_MANAGER = new ConnectionManager("chess_test");

    public static ConnectionManager getInstance() {
        return TEST_CONNECTION_MANAGER;
    }

    public static Connection getConnection() {
        return TEST_CONNECTION_MANAGER.getConnection();
    }
}
