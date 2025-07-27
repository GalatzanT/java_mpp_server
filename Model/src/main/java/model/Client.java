package model;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;

@Entity
@Table(name = "Clients")
public class Client extends EntityBase<Integer> {

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "email", nullable = false)
    private String email;

    public Client(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Client() {

    }

    public String getAddres() {
        return email;
    }

    public void setAddres(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


//
//public class Client extends Entity<Integer>{
//    String name;
//    String addres;
//
//    public Client(String name, String addres) {
//        this.name = name;
//        this.addres = addres;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    @Override
//    public String toString() {
//        return "Client{" +
//                "name='" + name + '\'' +
//                ", addres='" + addres + '\'' +
//                '}';
//    }
//
//    public String getAddres() {
//        return addres;
//    }
//
//    public void setAddres(String addres) {
//        this.addres = addres;
//    }
//}
