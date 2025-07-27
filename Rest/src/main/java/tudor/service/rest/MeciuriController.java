package tudor.service.rest;

import model.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import persistence.MatchRepo;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/matches/meciuri")
public class MeciuriController {

    @Autowired
    private MatchRepo matchRepo;

    // READ all
    @GetMapping
    public ResponseEntity<List<Match>> getAll() {
        System.out.println(">>> GET all matches");
        List<Match> matches = (List<Match>) matchRepo.findAll();
        return new ResponseEntity<>(matches, HttpStatus.OK);
    }

    // READ by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        System.out.println(">>> GET match by ID: " + id);
        Match match = matchRepo.findBy2(id);
        if (match == null) {
            return new ResponseEntity<>("Match not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(match, HttpStatus.OK);
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Match> createMatch(@RequestBody Match match) {
        System.out.println(">>> POST create match: " + match);
        Match saved = matchRepo.save(match);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMatch(@PathVariable int id, @RequestBody Match match) {
        System.out.println(">>> PUT update match id=" + id + " with: " + match);
        match.setId(id);
        Match updated = matchRepo.update(match);
        if (updated == null) {
            return new ResponseEntity<>("Match not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMatch(@PathVariable int id) {
        System.out.println(">>> DELETE match id=" + id);
        Match deleted = matchRepo.delete(id);
        if (deleted == null) {
            return new ResponseEntity<>("Match not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(deleted, HttpStatus.OK);
    }
}
