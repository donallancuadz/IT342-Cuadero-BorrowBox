import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import Register from "./features/auth/Register";
import Login from "./features/auth/Login";
import Dashboard from "./features/dashboard/Dashboard";
import Items from "./features/items/Items";
import Requests from "./features/requests/Requests";
import Profile from "./features/profile/Profile";
import AdminDashboard from "./features/admin/AdminDashboard";
import AdminItems from "./features/admin/AdminItems";
import AdminLogin from "./features/admin/AdminLogin";
import AdminRequests from "./features/admin/AdminRequests";
import AdminUsers from "./features/admin/AdminUsers";

function ProtectedRoute({ children }) {
  const token = localStorage.getItem("token");
  if (!token) return <Navigate to="/login" replace />;
  return children;
}

function AdminRoute({ children }) {
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");
  if (!token) return <Navigate to="/admin/login" replace />;
  if (role !== "ADMIN") return <Navigate to="/admin/login" replace />;
  return children;
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />

        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
        <Route path="/admin/login" element={<AdminLogin />} />

        <Route path="/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
        <Route path="/items" element={<ProtectedRoute><Items /></ProtectedRoute>} />
        <Route path="/requests" element={<ProtectedRoute><Requests /></ProtectedRoute>} />
        <Route path="/profile" element={<ProtectedRoute><Profile /></ProtectedRoute>} />
        <Route path="/admin" element={<AdminRoute><AdminDashboard /></AdminRoute>} />
        <Route path="/admin/items" element={<AdminRoute><AdminItems /></AdminRoute>} />
        <Route path="/admin/requests" element={<AdminRoute><AdminRequests /></AdminRoute>} />
        <Route path="/admin/users" element={<AdminRoute><AdminUsers /></AdminRoute>} />

        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </BrowserRouter>
  );
}