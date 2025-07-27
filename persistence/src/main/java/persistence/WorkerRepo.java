package persistence;

import model.Worker;

public interface WorkerRepo extends Repo<Integer, Worker> {
    Worker findBy(String username, String password);
}
