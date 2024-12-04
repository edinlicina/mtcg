package at.fhtw.mtcg.entity;

public class PackageEntity {


    private String id;
    private CardEntity card1;
    private CardEntity card2;
    private CardEntity card3;
    private CardEntity card4;
    private CardEntity card5;

    public PackageEntity(CardEntity card1, CardEntity card2, CardEntity card3, CardEntity card4, CardEntity card5) {
        this.card1 = card1;
        this.card2 = card2;
        this.card3 = card3;
        this.card4 = card4;
        this.card5 = card5;
    }
}
