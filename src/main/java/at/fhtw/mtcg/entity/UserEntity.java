package at.fhtw.mtcg.entity;

public class UserEntity {
    private String username;
    private String password;
    private String name;
    private String bio;
    private String image;

    private int coins ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format("{ \"name\": \"%s\", \"bio\": \"%s\", \"image\": \"%s\", \"username\": \"%s\" }", name, bio, image, username);
    }
}
