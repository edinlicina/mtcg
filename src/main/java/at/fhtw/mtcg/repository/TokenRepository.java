package at.fhtw.mtcg.repository;

import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.utils.TokenUtil;

import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TokenRepository {
    private final UnitOfWork unitOfWork;

    public TokenRepository() {
        unitOfWork = new UnitOfWork();
    }

    public String getTokenByUsername(String username) {
        PreparedStatement preparedStatement;
        ResultSet result;
        try {
            preparedStatement = unitOfWork.prepareStatement("SELECT * FROM token WHERE username = ?");
            preparedStatement.setString(1, username);
            result = preparedStatement.executeQuery();

            if (result.next()) {
                return result.getString(2);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public String createToken(String username) throws NoSuchAlgorithmException {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = unitOfWork.prepareStatement("INSERT INTO token (username, token) VALUES (?,?)");
            String newToken = TokenUtil.generateToken(username);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, newToken);
            preparedStatement.executeUpdate();
            return newToken;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
