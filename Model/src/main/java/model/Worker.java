package model;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

import java.lang.annotation.Annotation;

@Entity
@Table(name = "Workers")
public class Worker extends EntityBase<Integer>{
    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    public Worker() {
        super();
        this.username = null;
        this.password = null;
    }

    public Worker(Integer id, String username, String password) {
        super(id);
        this.username = username;
        this.password = password;
    }

    public Worker(String username, String password) {
        this.username = username;
        this.password = password;
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
        return "Worker{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}

//
//public class Worker extends Entity<Integer>{
//    String username;
//    String password;
//
//    public Worker(String username, String password) {
//        this.username = username;
//        this.password = password;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    @Override
//    public String toString() {
//        return "Worker{" +
//                "username='" + username + '\'' +
//                ", password='" + password + '\'' +
//                '}';
//    }
//}
