package at.fhtw.mtcg.repository;

import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.exceptions.DuplicatedCardIdException;
import at.fhtw.mtcg.exceptions.NotUniqueException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CardRepository {

    private final UnitOfWork unitOfWork;

    public CardRepository() {
        unitOfWork = new UnitOfWork();
    }

    public void createCard(String id, String name, float damage) throws DuplicatedCardIdException {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = unitOfWork.prepareStatement("INSERT INTO card (id, name, damage) VALUES (?,?,?)");
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setFloat(3, damage);
            preparedStatement.executeUpdate();
            unitOfWork.commitTransaction();
        } catch (SQLException e) {
            unitOfWork.rollbackTransaction();
            // 23505 is the Postgres error code for violating the unique constraint
            if ("23505".equals(e.getSQLState())) {
                throw new DuplicatedCardIdException();
            }
            throw new RuntimeException(e);
        }
    }
}
