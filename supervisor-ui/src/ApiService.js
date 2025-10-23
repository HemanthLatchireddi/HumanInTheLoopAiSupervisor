import axios from 'axios';

export const API_BASE = 'http://localhost:8080/api';

const client = axios.create({
  baseURL: API_BASE,
  timeout: 5000,
  headers: { 'Content-Type': 'application/json' }
});

export const Api = {
  createHelpRequest: (question) => client.post('/help/request', { question }),
  getPending: () => client.get('/help/pending'),
  respond: (id, answer) => client.post(`/help/${id}/response`, { answer }),
  getKnowledge: () => client.get('/knowledge'),
};
