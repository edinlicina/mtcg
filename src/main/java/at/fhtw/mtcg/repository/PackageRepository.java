package at.fhtw.mtcg.repository;

import at.fhtw.mtcg.dal.UnitOfWork;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PackageRepository {

    private final UnitOfWork unitOfWork;

    public PackageRepository() {
        this.unitOfWork = new UnitOfWork();
    }

    public void createPackage(String card1,String card2,String card3,String card4,String card5) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = unitOfWork.prepareStatement("INSERT INTO package (card1, card2, card3, card4, card5) VALUES (?,?,?,?,?)");
            preparedStatement.setString(1, card1);
            preparedStatement.setString(2, card2);
            preparedStatement.setString(3, card3);
            preparedStatement.setString(4, card4);
            preparedStatement.setString(5, card5);
            preparedStatement.executeUpdate();
            unitOfWork.commitTransaction();
        } catch (SQLException e) {
            unitOfWork.rollbackTransaction();

            throw new RuntimeException(e);
        }
    }
}
