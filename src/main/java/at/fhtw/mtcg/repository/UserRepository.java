package at.fhtw.mtcg.repository;

import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.entity.UserEntity;
import at.fhtw.mtcg.exceptions.NotUniqueException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
    private final UnitOfWork unitOfWork;

    public UserRepository() {
        this.unitOfWork = new UnitOfWork();
    }

    public UserEntity getUserByUsername(String username) {
        PreparedStatement preparedStatement;
        ResultSet result;
        try {
            preparedStatement = unitOfWork.prepareStatement("SELECT * FROM user_data WHERE username = ?");
            preparedStatement.setString(1, username);
            result = preparedStatement.executeQuery();

            if (result.next()) {
                UserEntity userEntity = new UserEntity();
                userEntity.setUsername(result.getString("username"));
                userEntity.setPassword(result.getString("password"));
                userEntity.setCoins(result.getInt("coins"));

                return userEntity;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void createUser(String username, String password) throws NotUniqueException {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = unitOfWork.prepareStatement("INSERT INTO user_data (username, password, coins) VALUES (?,?, 20)");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();
            unitOfWork.commitTransaction();
        } catch (SQLException e) {
            unitOfWork.rollbackTransaction();
            // 23505 is the Postgres error code for violating the unique constraint
            if ("23505".equals(e.getSQLState())) {
                throw new NotUniqueException();
            }
            throw new RuntimeException(e);
        }
    }

    public void updateUserCoins(int coins, String username) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = unitOfWork.prepareStatement("UPDATE user_data SET coins = ? WHERE username = ?");

            preparedStatement.setInt(1, coins);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
            unitOfWork.commitTransaction();
        } catch (SQLException e) {
            unitOfWork.rollbackTransaction();
            throw new RuntimeException(e);
        }
    }
}

