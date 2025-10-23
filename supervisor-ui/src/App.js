import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import Pending from './components/PendingRequests';
import Knowledge from './components/Knowledge';
import CreateRequest from './components/CreateRequest';
import AudioRoom from './components/AudioRoom';

function App() {
  return (
    <Router>
      {/* Navbar */}
      <nav className="navbar navbar-expand-lg navbar-dark bg-primary fixed-top">
        <div className="container">
          <Link className="navbar-brand" to="/">AI Supervisor</Link>
          <div className="collapse navbar-collapse">
            <ul className="navbar-nav me-auto">
              <li className="nav-item"><Link className="nav-link" to="/pending">Pending</Link></li>
              <li className="nav-item"><Link className="nav-link" to="/knowledge">Knowledge</Link></li>
              <li className="nav-item"><Link className="nav-link" to="/audio">Audio Room</Link></li>
            </ul>
          </div>
        </div>
      </nav>

      {/* Main Content */}
      <div className="container" style={{ marginTop: '80px' }}>
        <Routes>
          <Route path="/" element={<CreateRequest />} />
          <Route path="/pending" element={<Pending />} />
          <Route path="/knowledge" element={<Knowledge />} />
          <Route path="/audio" element={<AudioRoom />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
