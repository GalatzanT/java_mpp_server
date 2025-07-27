package networking.jsonprotocol;


import model.Client;
import model.Ticket;
import networking.dto.DTOUtils;
import model.Worker;


public class JsonProtocolUtils {


    public static Response createOkResponse() {
        Response resp = new Response();
        resp.setType(ResponseType.OK);
        return resp;
    }

    public static Response createErrorResponse(String errorMessage) {
        Response resp = new Response();
        resp.setType(ResponseType.ERROR);
        resp.setErrorMessage(errorMessage);
        return resp;
    }


    public static Request createLoginRequest(Worker user) {
        Request req = new Request();
        req.setType(RequestType.LOGIN);
        req.setWorkerDTO(DTOUtils.getDTO(user));
        return req;
    }


    public static Request createLogoutRequest(Worker user) {
        Request req = new Request();
        req.setType(RequestType.LOGOUT);
        req.setWorkerDTO(DTOUtils.getDTO(user));
        return req;
    }
    public static Request createGetMatches() {
        Request req = new Request();
        req.setType(RequestType.GET_MATCHES);
        return req;
    }

    public static Request createBuyTicketRequest(Ticket ticket){
        Request req = new Request();
        req.setType(RequestType.TICKET_ORDER);
        req.setTicketDTO(DTOUtils.getDTO(ticket));
        return req;
    }
    public static Request createFindTicketsByNameRequest(String name){
        Request req = new Request();
        req.setType(RequestType.SEARCH_NAME);
        req.setData(name);
        return req;
    }
    public static Request createFindTicketsByAddressRequest(String name){
        Request req = new Request();
        req.setType(RequestType.SEARCH_ADDRESS);
        req.setData(name);
        return req;
    }
}


