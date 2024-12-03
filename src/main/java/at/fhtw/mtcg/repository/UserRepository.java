package at.fhtw.mtcg.repository;

import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.entity.UserEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserRepository {
    private final UnitOfWork unitOfWork;
    public UserRepository ( ){
        this.unitOfWork = new UnitOfWork();
    }

    public UserEntity getUserByUsername(String username){
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
}
