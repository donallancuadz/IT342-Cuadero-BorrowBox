import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { loginUser, logoutUser } from "../../services/api";
import "../Auth.css";

export default function AdminLogin() {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [msg, setMsg] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    setMsg("");
    setLoading(true);

    try {
      const data = await loginUser({ email, password });
      if (data.role !== "ADMIN") {
        logoutUser();
        setMsg("This account does not have admin access.");
        return;
      }
      navigate("/admin");
    } catch (err) {
      setMsg(err.message || "Admin login failed");
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className="auth-page admin-auth">
      <section className="auth-card">
        <div className="auth-brand">
          <div className="auth-logo-box">B</div>
          <h1>BorrowBox Admin</h1>
          <p>Sign in to the operations portal</p>
        </div>

        {msg && <div className="auth-alert error">{msg}</div>}

        <form className="auth-form" onSubmit={handleSubmit}>
          <label>
            Admin email
            <input
              type="email"
              placeholder="admin@example.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </label>

          <label>
            Password
            <input
              type="password"
              placeholder="Enter admin password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </label>

          <button className="auth-submit" type="submit" disabled={loading}>
            {loading ? "Checking access..." : "Admin Login"}
          </button>
        </form>

        <p className="auth-switch">
          Borrower account?{" "}
          <button type="button" onClick={() => navigate("/login")}>
            User login
          </button>
        </p>
      </section>
    </main>
  );
}
