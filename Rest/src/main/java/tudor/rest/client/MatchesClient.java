package tudor.rest.client;



import model.Match;
import tudor.service.rest.ServiceException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;


public class MatchesClient {
    public static final String URL = "http://localhost:8080/matches/meciuri";

    private RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) { // server down, resource exception
            throw new ServiceException(e);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public Match[] getAll() {
        return execute(() -> restTemplate.getForObject(URL, Match[].class));
    }

    public Match getById(int id) {
        return execute(() -> restTemplate.getForObject(String.format("%s/%s", URL, id), Match.class));
    }

    public Match create(Match Match) {
        return execute(() -> restTemplate.postForObject(URL, Match, Match.class));
    }

    public void update(Match Match) {
        execute(() -> {
            restTemplate.put(String.format("%s/%s", URL, Match.getId()), Match);
            return null;
        });
    }

    public void delete(int id) {
        execute(() -> {
            restTemplate.delete(String.format("%s/%s", URL, id));
            return null;
        });
    }

}

