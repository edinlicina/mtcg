package at.fhtw.mtcg.entity;

public class CardEntity {

    private String name;

    private String username;

    private float damage;

    private String id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getDamage() {

        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    @Override
    public String toString() {
        return String.format("{ \"name\": \"%s\", \"damage\": \"%s\", \"id\": \"%s\" }", name, damage, id);
    }
}
