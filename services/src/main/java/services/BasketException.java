package services;

public class BasketException extends Exception{

    public BasketException(){

    }
    public BasketException(String message){
        super(message);
    }
    public BasketException(String message, Throwable cause){
        super(message, cause);
    }
}
