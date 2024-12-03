package at.fhtw.mtcg.repository;

import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.entity.UserEntity;

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
                return userEntity;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void createUser(String username, String password) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = unitOfWork.prepareStatement("INSERT INTO user_data (username, password) VALUES (?,?)");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();
            unitOfWork.commitTransaction();
        } catch (SQLException e) {
            unitOfWork.rollbackTransaction();
            throw new RuntimeException(e);
        }
    }
}
