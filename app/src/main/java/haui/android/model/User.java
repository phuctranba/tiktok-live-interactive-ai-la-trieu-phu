package haui.android.model;

import java.io.Serializable;

public class User implements Serializable {

    private Integer id;
    private String username;
    private Integer score;

    public User(Integer id, String username, Integer score) {
        this.id = id;
        this.username = username;
        this.score = score;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Integer getScore() {
        return score;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return id + "-" + username + "-" + score;
    }
}
