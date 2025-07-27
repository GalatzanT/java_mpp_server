package persistence;


import model.Client;


public interface ClientRepo extends Repo<Integer, Client> {

    Client findBy(String username, String address);
}
