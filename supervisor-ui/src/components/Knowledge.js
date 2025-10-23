import React, { useEffect, useState } from 'react';
import { Api } from '../ApiService';

export default function Knowledge() {
  const [items, setItems] = useState([]);

  const fetch = async () => {
    try {
      const res = await Api.getKnowledge();
      setItems(res.data || []);
    } catch (err) {
      console.error(err);
      alert('Failed to load knowledge base');
    }
  };

  useEffect(() => { fetch(); }, []);

  return (
    <div className="card">
      <div className="card-body">
        <h5 className="card-title">Learned Answers (Knowledge Base)</h5>
        {items.length === 0 && <div className="text-muted">No learned knowledge yet.</div>}
        {items.map(k => (
          <div key={k.id} className="mb-2">
            <div><strong>Q:</strong> {k.question}</div>
            <div><strong>A:</strong> {k.answer}</div>
            <hr />
          </div>
        ))}
      </div>
    </div>
  );
}
