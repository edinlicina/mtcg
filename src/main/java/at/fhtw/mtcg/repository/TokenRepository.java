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
        String newToken = TokenUtil.generateToken(username);
        try {
            preparedStatement = unitOfWork.prepareStatement("INSERT INTO token (username, token) VALUES (?,?)");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, newToken);
            preparedStatement.executeUpdate();
            unitOfWork.commitTransaction();
        } catch (SQLException e) {
            unitOfWork.rollbackTransaction();
            throw new RuntimeException(e);
        }
        return newToken;
    }

    public boolean isValidToken(String token){
        PreparedStatement preparedStatement;
        ResultSet result;
        try {
            preparedStatement = unitOfWork.prepareStatement("SELECT * FROM token WHERE token = ?");
            preparedStatement.setString(1, token);
            result = preparedStatement.executeQuery();

            if (result.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    public String getUsernameByToken(String token){
        PreparedStatement preparedStatement;
        ResultSet result;
        try {
            preparedStatement = unitOfWork.prepareStatement("SELECT username FROM token WHERE token = ?");
            preparedStatement.setString(1, token);
            result = preparedStatement.executeQuery();

            if (result.next()) {
                return result.getString("username");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;

    }
}
