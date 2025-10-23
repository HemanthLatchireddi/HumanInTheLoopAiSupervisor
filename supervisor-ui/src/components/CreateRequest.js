import React, { useState } from 'react';
import { Api } from '../ApiService';

const SAMPLE_QUESTIONS = [
  "What are your business hours?",
  "Do you offer hair coloring?",
  "Where is your salon located?",
  "How much does a haircut cost?",
  "Do you accept walk-ins?"
];

export default function CreateRequest() {
  const [question, setQuestion] = useState('');
  const [selectedSample, setSelectedSample] = useState('');
  const [aiResponse, setAiResponse] = useState('');

  const create = async (q) => {
    try {
      const res = await Api.createHelpRequest(q);

      if (res.data.status === 'ANSWERED_FROM_KB') {
        setAiResponse(res.data.answer);
      } else {
        setAiResponse('Your question has been sent to supervisor.');
        window.dispatchEvent(new Event('helpCreated'));
      }

      setQuestion('');
      setSelectedSample('');
    } catch (err) {
      console.error(err);
      alert('Failed to create request: ' + (err.response?.data?.message || err.message));
    }
  };

  return (
    <div className="card mb-4">
      <div className="card-body">
        <h5 className="card-title">Create Help Request</h5>

        <div className="mb-2">
          <label className="form-label">Choose sample question</label>
          <select className="form-select" value={selectedSample} onChange={(e) => {
            setSelectedSample(e.target.value);
            setQuestion(e.target.value);
          }}>
            <option value="">-- choose sample --</option>
            {SAMPLE_QUESTIONS.map((s, i) => <option key={i} value={s}>{s}</option>)}
          </select>
        </div>

        <div className="mb-3">
          <label className="form-label">Or type a custom question</label>
          <input className="form-control" value={question} onChange={e => setQuestion(e.target.value)} placeholder="Type question..." />
        </div>

        <button className="btn btn-primary" onClick={() => {
          if (!question.trim()) { alert('Please enter a question'); return; }
          create(question.trim());
        }}>Submit</button>

        {aiResponse && (
          <div className="alert alert-success mt-3" role="alert">
            <strong>AI Response:</strong> {aiResponse}
          </div>
        )}
      </div>
    </div>
  );
}
