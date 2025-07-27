import React, { useEffect, useState } from 'react';
import {
    getAllMatches,
    createMatch,
    deleteMatch,
    updateMatch,
} from './MatchService';

function App() {
    const [matches, setMatches] = useState([]);
    const [newMatch, setNewMatch] = useState({
        team1: '',
        team2: '',
        matchType: '',
        ticketPrice: '',
        availableSeats: '',
    });

    useEffect(() => {
        loadMatches();
    }, []);

    const loadMatches = () => {
        getAllMatches()
            .then(res => setMatches(res.data))
            .catch(err => console.error('Error loading matches', err));
    };

    const handleAdd = () => {
        // transformăm string-urile în numere acolo unde e nevoie
        const matchToAdd = {
            ...newMatch,
            ticketPrice: parseInt(newMatch.ticketPrice),
            availableSeats: parseInt(newMatch.availableSeats),
        };

        createMatch(matchToAdd)
            .then(() => {
                setNewMatch({
                    team1: '',
                    team2: '',
                    matchType: '',
                    ticketPrice: '',
                    availableSeats: '',
                });
                loadMatches();
            })
            .catch(err => console.error('Error creating match', err));
    };

    const handleDelete = (id) => {
        deleteMatch(id)
            .then(() => loadMatches())
            .catch(err => console.error('Error deleting match', err));
    };

    const handleUpdate = (id) => {
        const updatedSeats = prompt('Introduceți noul număr de locuri disponibile:');
        if (updatedSeats !== null) {
            const match = matches.find(m => m.id === id);
            const updatedMatch = { ...match, availableSeats: parseInt(updatedSeats) };
            updateMatch(id, updatedMatch)
                .then(() => loadMatches())
                .catch(err => console.error('Error updating match', err));
        }
    };

    return (
        <div style={{ padding: 20 }}>
            <h2>Meciuri disponibile</h2>
            <ul>
                {matches.map(match => (
                    <li key={match.id}>
                        {match.team1} vs {match.team2} - {match.matchType}, preț: {match.ticketPrice} lei, locuri: {match.availableSeats}
                        <button onClick={() => handleDelete(match.id)}>🗑️</button>
                        <button onClick={() => handleUpdate(match.id)}>✏️</button>
                    </li>
                ))}
            </ul>

            <h3>Adaugă un meci nou</h3>
            <input
                placeholder="Team 1"
                value={newMatch.team1}
                onChange={(e) => setNewMatch({ ...newMatch, team1: e.target.value })}
            />
            <input
                placeholder="Team 2"
                value={newMatch.team2}
                onChange={(e) => setNewMatch({ ...newMatch, team2: e.target.value })}
            />
            <input
                placeholder="Tip meci (ex: Friendly)"
                value={newMatch.matchType}
                onChange={(e) => setNewMatch({ ...newMatch, matchType: e.target.value })}
            />
            <input
                placeholder="Preț bilet"
                type="number"
                value={newMatch.ticketPrice}
                onChange={(e) => setNewMatch({ ...newMatch, ticketPrice: e.target.value })}
            />
            <input
                placeholder="Locuri disponibile"
                type="number"
                value={newMatch.availableSeats}
                onChange={(e) => setNewMatch({ ...newMatch, availableSeats: e.target.value })}
            />
            <button onClick={handleAdd}>Adaugă</button>
        </div>
    );
}

export default App;
