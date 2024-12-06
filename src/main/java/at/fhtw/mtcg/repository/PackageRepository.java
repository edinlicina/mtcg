package at.fhtw.mtcg.repository;

import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.entity.PackageEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    public PackageEntity getPackage(){
        PreparedStatement preparedStatement;
        ResultSet result;
        try{
            preparedStatement = unitOfWork.prepareStatement("SELECT * FROM package LIMIT 1");
            result = preparedStatement.executeQuery();

            if(result.next()){
                PackageEntity packageEntity = new PackageEntity();
                packageEntity.setId(result.getInt("id"));
                packageEntity.setCard1(result.getString("card1"));
                packageEntity.setCard2(result.getString("card2"));
                packageEntity.setCard3(result.getString("card3"));
                packageEntity.setCard4(result.getString("card4"));
                packageEntity.setCard5(result.getString("card5"));
                return packageEntity;

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public void deletePackage(int packageId){
        PreparedStatement preparedStatement;

        try{
            preparedStatement=unitOfWork.prepareStatement("DELETE FROM package WHERE id = ?");
            preparedStatement.setInt(1, packageId);
            preparedStatement.executeUpdate();
            unitOfWork.commitTransaction();
        } catch (SQLException e) {
            unitOfWork.rollbackTransaction();
            throw new RuntimeException(e);
        }
    }
}
