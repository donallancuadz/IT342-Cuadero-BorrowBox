import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getMe, logoutUser } from "../../services/api";
import Navbar from "../../components/navbar";
import "./Dashboard.css";

export default function Dashboard() {
  const navigate = useNavigate();

  const [me, setMe] = useState(null);
  const [msg, setMsg] = useState("");
  const [loading, setLoading] = useState(true);

  async function loadMe() {
    setMsg("");
    setLoading(true);

    try {
      const data = await getMe();
      setMe(data);
    } catch (err) {
      logoutUser();
      navigate("/login");
    } finally {
      setLoading(false);
    }
  }

  function handleLogout() {
    logoutUser();
    navigate("/login");
  }

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) {
      navigate("/login");
      return;
    }
    loadMe();
  }, [navigate]);

  if (loading) {
    return <div className="dashboard-loading">Loading...</div>;
  }

  return (
    <div className="dashboard-page">

      <Navbar />

      {msg && <p className="dashboard-error">{msg}</p>}

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

          <button className="logout-btn" onClick={handleLogout}>
            LOGOUT
          </button>
        </section>

        <section className="dashboard-stats-grid">
          <div className="stat-card">
            <p>Active Borrows</p>
            <h2>3</h2>
          </div>

          <div className="stat-card">
            <p>Pending Requests</p>
            <h2>1</h2>
          </div>

          <div className="stat-card">
            <p>Returned Items</p>
            <h2>12</h2>
          </div>

          <div className="stat-card">
            <p>Available Items</p>
            <h2>47</h2>
          </div>
        </section>
      </main>

      <section className="recent-requests-card">
        <h3>Recent Borrows Requests</h3>

        <div className="request-row">
          <div className="request-left">
            <div className="request-photo">Photo</div>
            <span>Portable Projector</span>
          </div>
          <div className="request-right">
            <span>Mar 5</span>
            <span className="status-badge active">Active</span>
          </div>
        </div>

        <div className="request-row">
          <div className="request-left">
            <div className="request-photo">Photo</div>
            <span>Camping tent</span>
          </div>
          <div className="request-right">
            <span>Mar 4</span>
            <span className="status-badge pending">Pending</span>
          </div>
        </div>

        <div className="request-row">
          <div className="request-left">
            <div className="request-photo">Photo</div>
            <span>Power Drill</span>
          </div>
          <div className="request-right">
            <span>Feb 28</span>
            <span className="status-badge returned">Returned</span>
          </div>
        </div>
      </section>
    </div>
  );
}