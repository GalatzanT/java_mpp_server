package networking.dto;

import java.io.Serializable;

public class ClientDTO implements Serializable {
    private Integer id;
    private String name;
    private String addres; // presupun că e intenționat scris așa; dacă e greșeală de tastare, corectează-l în "address"

    public ClientDTO(Integer id, String name, String addres) {
        this.id = id;
        this.name = name;
        this.addres = addres;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddres() {
        return addres;
    }

    public void setAddres(String addres) {
        this.addres = addres;
    }

    @Override
    public String toString() {
        return "ClientDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", addres='" + addres + '\'' +
                '}';
    }
}

