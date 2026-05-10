import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../../shared/components/Navbar";
import { getMe, logoutUser, updateProfile } from "../../shared/api";
import "./Profile.css";

export default function Profile() {
  const navigate = useNavigate();
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [editMode, setEditMode] = useState(false);
  const [saving, setSaving] = useState(false);
  const [successMsg, setSuccessMsg] = useState("");
  const [errorMsg, setErrorMsg] = useState("");

  const [form, setForm] = useState({
    fullName: "",
    currentPassword: "",
    newPassword: "",
    confirmPassword: "",
  });

  useEffect(() => {
    fetchUser();
  }, []);

  async function fetchUser() {
    try {
      const data = await getMe();
      setUser(data);
      setForm((f) => ({ ...f, fullName: data.fullName }));
    } catch (err) {
      setErrorMsg("Could not load profile.");
    } finally {
      setLoading(false);
    }
  }

  async function handleSave() {
    setErrorMsg("");
    setSuccessMsg("");

    if (form.newPassword && form.newPassword !== form.confirmPassword) {
      setErrorMsg("New passwords do not match.");
      return;
    }

    setSaving(true);
    try {
      const body = { fullName: form.fullName };
      if (form.newPassword) {
        body.currentPassword = form.currentPassword;
        body.newPassword = form.newPassword;
      }

      const updated = await updateProfile(body);
      setUser(updated);
      setSuccessMsg("Profile updated successfully!");
      setEditMode(false);
      setForm((f) => ({ ...f, currentPassword: "", newPassword: "", confirmPassword: "" }));
    } catch (err) {
      setErrorMsg(err.message);
    } finally {
      setSaving(false);
    }
  }

  function handleLogout() {
    logoutUser();
    navigate("/login");
  }

  function getInitials(name) {
    if (!name) return "?";
    return name
      .split(" ")
      .map((n) => n[0])
      .join("")
      .toUpperCase()
      .slice(0, 2);
  }

  if (loading) {
    return (
      <>
        <Navbar />
        <div className="profile-loading">Loading profile...</div>
      </>
    );
  }

  return (
    <>
      <Navbar />
      <div className="profile-page">
        <div className="profile-container">

          {/* Avatar + identity */}
          <div className="profile-header-card">
            <div className="profile-avatar">{getInitials(user?.fullName)}</div>
            <div className="profile-identity">
              <h1 className="profile-name">{user?.fullName}</h1>
              <p className="profile-email">{user?.email}</p>
              <span className="profile-status-badge">Active</span>
            </div>
            <button
              className="profile-edit-btn"
              onClick={() => { setEditMode(!editMode); setSuccessMsg(""); setErrorMsg(""); }}
            >
              {editMode ? "Cancel" : "Edit Profile"}
            </button>
          </div>

          {/* Feedback messages */}
          {successMsg && <div className="profile-alert success">{successMsg}</div>}
          {errorMsg && <div className="profile-alert error">{errorMsg}</div>}

          {/* Edit form */}
          {editMode && (
            <div className="profile-card">
              <h2 className="profile-section-title">Edit Profile</h2>

              <div className="profile-field">
                <label>Full Name</label>
                <input
                  type="text"
                  value={form.fullName}
                  onChange={(e) => setForm({ ...form, fullName: e.target.value })}
                  placeholder="Your full name"
                />
              </div>

              <div className="profile-divider" />
              <p className="profile-section-sub">Change password (leave blank to keep current)</p>

              <div className="profile-field">
                <label>Current Password</label>
                <input
                  type="password"
                  value={form.currentPassword}
                  onChange={(e) => setForm({ ...form, currentPassword: e.target.value })}
                  placeholder="Enter current password"
                />
              </div>
              <div className="profile-field">
                <label>New Password</label>
                <input
                  type="password"
                  value={form.newPassword}
                  onChange={(e) => setForm({ ...form, newPassword: e.target.value })}
                  placeholder="Enter new password"
                />
              </div>
              <div className="profile-field">
                <label>Confirm New Password</label>
                <input
                  type="password"
                  value={form.confirmPassword}
                  onChange={(e) => setForm({ ...form, confirmPassword: e.target.value })}
                  placeholder="Confirm new password"
                />
              </div>

              <button className="profile-save-btn" onClick={handleSave} disabled={saving}>
                {saving ? "Saving..." : "Save Changes"}
              </button>
            </div>
          )}

          {/* Account info (read-only) */}
          {!editMode && (
            <div className="profile-card">
              <h2 className="profile-section-title">Account Information</h2>
              <div className="profile-info-row">
                <span className="profile-info-label">Full Name</span>
                <span className="profile-info-value">{user?.fullName}</span>
              </div>
              <div className="profile-info-row">
                <span className="profile-info-label">Email Address</span>
                <span className="profile-info-value">{user?.email}</span>
              </div>
              <div className="profile-info-row">
                <span className="profile-info-label">Account Status</span>
                <span className="profile-status-badge">Active</span>
              </div>
            </div>
          )}

          {/* Danger zone */}
          <div className="profile-card profile-danger-card">
            <h2 className="profile-section-title danger">Danger Zone</h2>
            <p className="profile-danger-desc">
              Logging out will end your current session.
            </p>
            <button className="profile-logout-btn" onClick={handleLogout}>
              Logout
            </button>
          </div>

        </div>
      </div>
    </>
  );
}
