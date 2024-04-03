package chess.dao;

import java.sql.SQLException;

public class DatabaseException extends RuntimeException {
    private static final String ERROR_PREFIX = "[DATABASE_ERROR] ";

    public DatabaseException(String message, SQLException e) {
        super(ERROR_PREFIX + message, e);
    }

    public DatabaseException(String message) {
        super(ERROR_PREFIX + message);
    }
}
