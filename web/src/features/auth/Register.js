import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { registerUser } from "../../shared/api";
import "./Auth.css";

export default function Register() {
  const navigate = useNavigate();

  const [fullName, setFullName] = useState("");
  const [email, setEmail] = useState("");
  const [studentId, setStudentId] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [err, setErr] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    setErr("");

    if (password !== confirmPassword) {
      setErr("Passwords do not match");
      return;
    }

    setLoading(true);
    try {
      await registerUser({ fullName, email, password, confirmPassword, studentId });
      navigate("/login", { replace: true, state: { registered: true } });
    } catch (e2) {
      setErr(e2.message || "Registration failed");
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className="auth-page">
      <section className="auth-card register">
        <div className="auth-brand">
          <div className="auth-logo-box">B</div>
          <h1>BorrowBox</h1>
          <p>Create your account</p>
        </div>

        {err && <div className="auth-alert error">{err}</div>}

        <form className="auth-form" onSubmit={handleSubmit}>
          <label>
            Full name
            <input
              type="text"
              placeholder="Juan Dela Cruz"
              value={fullName}
              onChange={(e) => setFullName(e.target.value)}
              required
            />
          </label>

          <label>
            Email address
            <input
              type="email"
              placeholder="you@example.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </label>

          <label>
            Student ID
            <input
              type="text"
              placeholder="12-3456-789"
              pattern="[0-9]{2}-[0-9]{4}-[0-9]{3}"
              value={studentId}
              onChange={(e) => setStudentId(e.target.value)}
              required
            />
          </label>

          <label>
            Password
            <input
              type="password"
              placeholder="At least 6 characters"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              minLength="6"
              required
            />
          </label>

          <label>
            Confirm password
            <input
              type="password"
              placeholder="Re-enter your password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              minLength="6"
              required
            />
          </label>

          <button className="auth-submit" type="submit" disabled={loading}>
            {loading ? "Creating account..." : "Create Account"}
          </button>
        </form>

        <p className="auth-switch">
          Already have an account?{" "}
          <button type="button" onClick={() => navigate("/login")}>
            Log in here
          </button>
        </p>
      </section>
    </main>
  );
}
