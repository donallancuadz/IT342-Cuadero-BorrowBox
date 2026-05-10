import { useEffect, useState } from "react";
import { decideAdminRequest, getAdminRequests } from "../../shared/api";
import AdminNav from "./AdminNav";
import "./Admin.css";

export default function AdminRequests() {
  const [requests, setRequests] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    loadRequests();
  }, []);

  async function loadRequests() {
    try {
      setRequests(await getAdminRequests());
    } catch (err) {
      setError(err.message);
    }
  }

  async function decide(id, action) {
    try {
      await decideAdminRequest(id, action);
      loadRequests();
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <main className="admin-page">
      <div className="admin-wrap">
        <AdminNav />
        <header className="admin-header">
          <div>
            <h1>Manage Requests</h1>
            <p>Approve or reject borrow requests.</p>
          </div>
        </header>

        {error && <div className="admin-alert">{error}</div>}

        <section className="admin-panel">
          <table className="admin-table">
            <thead>
              <tr>
                <th>Item</th>
                <th>Borrower</th>
                <th>Date</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {requests.map((request) => (
                <tr key={request.id}>
                  <td>{request.itemName}</td>
                  <td>
                    {request.borrowerName}
                    <br />
                    <small>{request.borrowerEmail}</small>
                  </td>
                  <td>{new Date(request.requestDate).toLocaleDateString()}</td>
                  <td>
                    <span className={`admin-status ${request.status.toLowerCase()}`}>
                      {request.status}
                    </span>
                  </td>
                  <td>
                    <div className="admin-actions">
                      <button
                        className="admin-secondary"
                        type="button"
                        disabled={request.status !== "PENDING"}
                        onClick={() => decide(request.id, "approve")}
                      >
                        Approve
                      </button>
                      <button
                        className="admin-danger"
                        type="button"
                        disabled={request.status !== "PENDING"}
                        onClick={() => decide(request.id, "reject")}
                      >
                        Reject
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </section>
      </div>
    </main>
  );
}
