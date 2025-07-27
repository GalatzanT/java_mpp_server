package networking.dto;

import java.io.Serializable;

public class WorkerDTO implements Serializable {
    int id;
    String name;
    String pass;
    public WorkerDTO(int id, String name, String pass) {
        this.id = id;
        this.name = name;
        this.pass = pass;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPass() {
        return pass;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "WorkerDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}
