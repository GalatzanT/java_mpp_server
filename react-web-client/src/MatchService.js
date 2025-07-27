// src/services/MatchService.js
import axios from 'axios';

const API_URL = 'http://localhost:8080/matches/meciuri';

export const getAllMatches = () => axios.get(API_URL);

export const getMatchById = (id) => axios.get(`${API_URL}/${id}`);

export const createMatch = (match) => axios.post(API_URL, match);

export const updateMatch = (id, match) => axios.put(`${API_URL}/${id}`, match);

export const deleteMatch = (id) => axios.delete(`${API_URL}/${id}`);
