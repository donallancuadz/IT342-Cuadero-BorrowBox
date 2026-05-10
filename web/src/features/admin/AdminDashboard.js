import { useEffect, useState } from "react";
import { getAdminStats } from "../../shared/api";
import AdminNav from "./AdminNav";
import "./Admin.css";

export default function AdminDashboard() {
  const [stats, setStats] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    getAdminStats()
      .then(setStats)
      .catch((err) => setError(err.message));
  }, []);

  const cards = [
    ["Total Users", stats?.totalUsers ?? "-"],
    ["Admins", stats?.admins ?? "-"],
    ["Total Items", stats?.totalItems ?? "-"],
    ["Available Items", stats?.availableItems ?? "-"],
    ["Total Requests", stats?.totalRequests ?? "-"],
    ["Pending", stats?.pendingRequests ?? "-"],
    ["Approved", stats?.approvedRequests ?? "-"],
    ["Rejected", stats?.rejectedRequests ?? "-"],
  ];

  return (
    <main className="admin-page">
      <div className="admin-wrap">
        <AdminNav />
        <header className="admin-header">
          <div>
            <h1>Admin Dashboard</h1>
            <p>Overview of BorrowBox users, inventory, and borrow requests.</p>
          </div>
        </header>

        {error && <div className="admin-alert">{error}</div>}

        <section className="admin-grid">
          {cards.map(([label, value]) => (
            <div className="admin-stat" key={label}>
              <span>{label}</span>
              <strong>{value}</strong>
            </div>
          ))}
        </section>
      </div>
    </main>
  );
}
