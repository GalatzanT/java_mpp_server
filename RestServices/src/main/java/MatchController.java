

import java.util.List;

public interface MatchController {
    List<Meci> getAllMatches();
    Meci getMatchById(Long id);
    Meci createMatch(Meci match);
    Meci updateMatch(Long id, Meci match);
    void deleteMatch(Long id);
}