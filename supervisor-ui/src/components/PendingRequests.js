import React, { useEffect, useState } from 'react';
import { Api } from '../ApiService';

export default function PendingRequests() {
  const [pending, setPending] = useState([]);
  const [answers, setAnswers] = useState({});
  const [loadingIds, setLoadingIds] = useState({});

  const fetchPending = async () => {
    try {
      const res = await Api.getPending();
      setPending(res.data || []);
    } catch (err) {
      console.error(err);
      alert('Failed to fetch pending requests. Is the backend running?');
    }
  };

  useEffect(() => {
    fetchPending();
    const handler = () => fetchPending();
    window.addEventListener('helpCreated', handler);
    return () => window.removeEventListener('helpCreated', handler);
  }, []);

  const submitAnswer = async (id) => {
    const answer = (answers[id] || '').trim();
    if (!answer) { alert('Please type an answer'); return; }
    setLoadingIds(prev => ({ ...prev, [id]: true }));
    try {
      await Api.respond(id, answer);
      setAnswers(prev => ({ ...prev, [id]: '' }));
      await fetchPending();
    } catch (err) {
      console.error(err);
      alert('Failed to submit answer');
    } finally {
      setLoadingIds(prev => ({ ...prev, [id]: false }));
    }
  };

  return (
    <div className="card">
      <div className="card-body">
        <h5 className="card-title">Pending Help Requests</h5>
        <div className="mb-3">
          <button className="btn btn-outline-secondary btn-sm" onClick={fetchPending}>Refresh</button>
        </div>

        {pending.length === 0 && <div className="text-muted">No pending requests</div>}

        {pending.map(req => (
          <div key={req.id} className="border rounded p-3 mb-2">
            <div className="d-flex justify-content-between">
              <div><strong>#{req.id}</strong> <small className="text-muted"> {req.createdAt ? new Date(req.createdAt).toLocaleString() : ''}</small></div>
              <div><span className="badge bg-warning text-dark">{req.status}</span></div>
            </div>

            <div className="mt-2">Q: {req.question}</div>

            <textarea className="form-control mt-2" rows="3"
              value={answers[req.id] || ''}
              onChange={(e) => setAnswers(prev => ({ ...prev, [req.id]: e.target.value }))}
              placeholder="Type your answer here..." />

            <div className="mt-2">
              <button className="btn btn-success btn-sm" onClick={() => submitAnswer(req.id)} disabled={loadingIds[req.id]}>
                {loadingIds[req.id] ? 'Submitting...' : 'Submit Answer'}
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
