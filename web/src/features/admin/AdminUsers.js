import { useEffect, useState } from "react";
import { getAdminUsers, updateAdminUserRole } from "../../shared/api";
import AdminNav from "./AdminNav";
import "./Admin.css";

export default function AdminUsers() {
  const [users, setUsers] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    loadUsers();
  }, []);

  async function loadUsers() {
    try {
      setUsers(await getAdminUsers());
    } catch (err) {
      setError(err.message);
    }
  }

  async function changeRole(id, role) {
    try {
      await updateAdminUserRole(id, role);
      loadUsers();
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
            <h1>Manage Users</h1>
            <p>View users and assign account roles.</p>
          </div>
        </header>

        {error && <div className="admin-alert">{error}</div>}

        <section className="admin-panel">
          <table className="admin-table">
            <thead>
              <tr>
                <th>Name</th>
                <th>Email</th>
                <th>Student ID</th>
                <th>Role</th>
              </tr>
            </thead>
            <tbody>
              {users.map((user) => (
                <tr key={user.id}>
                  <td>{user.fullName}</td>
                  <td>{user.email}</td>
                  <td>{user.studentId}</td>
                  <td>
                    <select value={user.role} onChange={(e) => changeRole(user.id, e.target.value)}>
                      <option value="USER">USER</option>
                      <option value="ADMIN">ADMIN</option>
                    </select>
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
