package at.fhtw.mtcg.repository;

import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.entity.CardEntity;
import at.fhtw.mtcg.exceptions.DuplicatedCardIdException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public void updateUsername(String username, String cardId) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = unitOfWork.prepareStatement("UPDATE card SET username = ? WHERE id = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, cardId);
            preparedStatement.executeUpdate();
            unitOfWork.commitTransaction();
        } catch (SQLException e) {
            unitOfWork.rollbackTransaction();
            throw new RuntimeException(e);
        }
    }

    public List<CardEntity> getCardsForUser(String username) {
        List<CardEntity> cardEntityList = new ArrayList<>();
        PreparedStatement preparedStatement;
        ResultSet result;
        try {
            preparedStatement = unitOfWork.prepareStatement("SELECT * FROM card WHERE username = ?");
            preparedStatement.setString(1, username);
            result = preparedStatement.executeQuery();

            while (result.next()) {
                CardEntity cardEntity = new CardEntity();
                cardEntity.setName(result.getString("name"));
                cardEntity.setDamage(result.getFloat("damage"));
                cardEntity.setId(result.getString("id"));
                cardEntityList.add(cardEntity);
            }
            return cardEntityList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public CardEntity getCardById(String id) {
        PreparedStatement preparedStatement;
        ResultSet result;
        try {
            preparedStatement = unitOfWork.prepareStatement("SELECT * FROM card WHERE id = ?");
            preparedStatement.setString(1, id);
            result = preparedStatement.executeQuery();

            if (result.next()) {
                CardEntity cardEntity = new CardEntity();
                cardEntity.setName(result.getString("name"));
                cardEntity.setDamage(result.getFloat("damage"));
                cardEntity.setId(result.getString("id"));
                return cardEntity;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
