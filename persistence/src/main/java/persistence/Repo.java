package persistence;

import model.EntityBase;

import java.util.List;

public interface Repo<ID, E extends EntityBase<ID>> {
    E findOne(ID id);
    List<E> findAll();
    E save(E entity);
    E delete(ID id);
    E update(E entity);
}
