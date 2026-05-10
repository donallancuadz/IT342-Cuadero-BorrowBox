import { useNavigate } from "react-router-dom";
import "./Navbar.css";

export default function Navbar() {
  const navigate = useNavigate();

  return (
    <header className="dashboard-navbar">
      <div className="dashboard-logo" onClick={() => navigate("/dashboard")}>
        <div className="dashboard-logo-box"></div>
        <span>BorrowBox</span>
      </div>

      <nav className="dashboard-nav-links">
        <button onClick={() => navigate("/items")}>Items</button>
        <button onClick={() => navigate("/requests")}>Request</button>
        <button onClick={() => navigate("/profile")}>Profile</button>
      </nav>
    </header>
  );
}
