import { useLocation, useNavigate } from "react-router-dom";
import { logoutUser } from "../../services/api";

export default function AdminNav() {
  const navigate = useNavigate();
  const location = useLocation();

  const tabs = [
    ["/admin", "Overview"],
    ["/admin/items", "Items"],
    ["/admin/requests", "Requests"],
    ["/admin/users", "Users"],
  ];

  function handleLogout() {
    logoutUser();
    navigate("/admin/login");
  }

  return (
    <header className="admin-topbar">
      <div className="admin-brand" onClick={() => navigate("/admin")}>
        <div className="admin-brand-mark">B</div>
        <div>
          <strong>BorrowBox Admin</strong>
          <span>Operations Portal</span>
        </div>
      </div>

      <div className="admin-tabs">
        {tabs.map(([path, label]) => (
          <button
            key={path}
            type="button"
            className={location.pathname === path ? "active" : ""}
            onClick={() => navigate(path)}
          >
            {label}
          </button>
        ))}
      </div>

      <button className="admin-logout" type="button" onClick={handleLogout}>
        Logout
      </button>
    </header>
  );
}
