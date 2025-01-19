package at.fhtw.mtcg.repository;

import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.entity.DeckEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeckRepository {

    private final UnitOfWork unitOfWork;

    public DeckRepository() {
        this.unitOfWork = new UnitOfWork();
    }

    public DeckEntity getDeckByUsername(String username) {
        PreparedStatement preparedStatement;
        ResultSet result;
        try {
            preparedStatement = unitOfWork.prepareStatement("SELECT * FROM deck WHERE username = ?");
            preparedStatement.setString(1, username);
            result = preparedStatement.executeQuery();

            if (result.next()) {
                DeckEntity deckEntity = new DeckEntity();
                deckEntity.setCard1(result.getString("card1"));
                deckEntity.setCard2(result.getString("card2"));
                deckEntity.setCard3(result.getString("card3"));
                deckEntity.setCard4(result.getString("card4"));
                deckEntity.setUsername(result.getString("username"));
                return deckEntity;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void createDeck(String card1, String card2, String card3, String card4, String username) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = unitOfWork.prepareStatement("INSERT INTO deck (card1, card2, card3, card4, username) VALUES (?,?,?,?,?)");
            preparedStatement.setString(1, card1);
            preparedStatement.setString(2, card2);
            preparedStatement.setString(3, card3);
            preparedStatement.setString(4, card4);
            preparedStatement.setString(5, username);
            preparedStatement.executeUpdate();
            unitOfWork.commitTransaction();
        } catch (SQLException e) {
            unitOfWork.rollbackTransaction();

            throw new RuntimeException(e);
        }
    }

    public void updateDeck(String card1, String card2, String card3, String card4, String username) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = unitOfWork.prepareStatement("UPDATE deck SET card1 = ?, card2 = ?, card3 = ?, card4 = ? WHERE username = ?");
            preparedStatement.setString(1, card1);
            preparedStatement.setString(2, card2);
            preparedStatement.setString(3, card3);
            preparedStatement.setString(4, card4);
            preparedStatement.setString(5, username);
            preparedStatement.executeUpdate();
            unitOfWork.commitTransaction();
        } catch (SQLException e) {
            unitOfWork.rollbackTransaction();

            throw new RuntimeException(e);
        }
    }
}
