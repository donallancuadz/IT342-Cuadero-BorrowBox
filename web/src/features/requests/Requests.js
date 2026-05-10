import { useEffect, useState } from "react";
import Navbar from "../../shared/components/Navbar";
import { getMyRequests } from "../../shared/api";
import "./Requests.css";

export default function Requests() {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadRequests() {
      try {
        const data = await getMyRequests();
        setRequests(data);
      } catch (err) {
        console.error("Failed to load requests:", err);
      } finally {
        setLoading(false);
      }
    }

    loadRequests();
  }, []);

  if (loading) {
    return (
      <div className="requests-page">
        <Navbar />
        <div className="requests-container">
          <h2>My Requests</h2>
          <p>Loading requests...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="requests-page">
      <Navbar />

      <div className="requests-container">
        <h2>My Requests</h2>

        <div className="requests-table">
          <div className="table-header">
            <span>Item</span>
            <span>Date</span>
            <span>Status</span>
          </div>

          {requests.length === 0 ? (
            <div className="table-row">
              <span>No requests yet</span>
              <span>-</span>
              <span>-</span>
            </div>
          ) : (
            requests.map((req) => (
              <div key={req.id} className="table-row">
                <span>{req.itemName}</span>
                <span>
                  {new Date(req.requestDate).toLocaleDateString()}
                </span>
                <span className={`status ${req.status.toLowerCase()}`}>
                  {req.status}
                </span>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
}