package persistence;

import model.Match;



public interface MatchRepo extends Repo<Integer, Match> {
    Match findBy2(int id);
}
