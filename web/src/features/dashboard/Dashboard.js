import { useEffect, useState, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import { getMe, getDashboard, logoutUser } from "../../shared/api";
import Navbar from "../../shared/components/Navbar";
import "./Dashboard.css";

export default function Dashboard() {
  const navigate = useNavigate();

  const [me, setMe]           = useState(null);
  const [stats, setStats]     = useState(null);
  const [loading, setLoading] = useState(true);

  const loadDashboard = useCallback(async () => {
    setLoading(true);
    try {
      const [meData, statsData] = await Promise.all([getMe(), getDashboard()]);
      setMe(meData);
      setStats(statsData);
    } catch (err) {
      logoutUser();
      navigate("/login");
    } finally {
      setLoading(false);
    }
  }, [navigate]);

  function handleLogout() {
    logoutUser();
    navigate("/login");
  }

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) { navigate("/login"); return; }
    loadDashboard();
  }, [navigate, loadDashboard]);

  if (loading) return <div className="dashboard-loading">Loading...</div>;

  return (
    <div className="dashboard-page">

      <Navbar />

      <main className="dashboard-main">

        <section className="dashboard-left-card">
          <div className="profile-top">
            <div className="profile-photo">PHOTO</div>
            <div className="profile-info">
              <h3>{me?.fullName || "User Name"}</h3>
              <p>{me?.email || "email@example.com"}</p>
              <span className="status-badge active">Active</span>
            </div>
          </div>

          <div className="profile-shortcuts">
            <button onClick={() => navigate("/items")}>→ Browse Items</button>
            <button onClick={() => navigate("/requests")}>→ My Requests</button>
            <button onClick={() => navigate("/profile")}>→ Account Settings</button>
          </div>

          <button className="logout-btn" onClick={handleLogout}>LOGOUT</button>
        </section>

        <section className="dashboard-stats-grid">
          <div className="stat-card">
            <p>Active Borrows</p>
            <h2>{stats?.activeBorrows ?? "—"}</h2>
          </div>
          <div className="stat-card">
            <p>Pending Requests</p>
            <h2>{stats?.pendingRequests ?? "—"}</h2>
          </div>
          <div className="stat-card">
            <p>Returned Items</p>
            <h2>{stats?.returnedItems ?? "—"}</h2>
          </div>
        </section>

      </main>

      <section className="recent-requests-card">
        <h3>Recent Borrows Requests</h3>

        {!stats?.recentRequests?.length ? (
          <p className="no-requests">No recent requests yet.</p>
        ) : (
          stats.recentRequests.map((req, i) => (
            <div className="request-row" key={i}>
              <div className="request-left">
                <div className="request-photo">Photo</div>
                <span>{req.itemName}</span>
              </div>
              <div className="request-right">
                <span>{req.requestDate}</span>
                <span className={`status-badge ${req.status.toLowerCase()}`}>
                  {req.status}
                </span>
              </div>
            </div>
          ))
        )}
      </section>

    </div>
  );
}