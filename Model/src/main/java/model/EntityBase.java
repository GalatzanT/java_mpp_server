package model;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class EntityBase<T>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected T id;

    public EntityBase() {
        this.id = null;
    }

    public EntityBase(T id) {
        this.id = id;
    }

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof EntityBase<?> other)) return false;
        return id != null ? id.equals(other.id) : other.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}


//public class EntityBase<ID> {
//    private ID id;
//
//    public ID getId() {
//        return id;
//    }
//
//    public void setId(ID id) {
//        this.id = id;
//    }
//}
