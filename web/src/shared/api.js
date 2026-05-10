const API_BASE = "http://localhost:8080";

// ----------------------
// Helpers
// ----------------------
function parseJsonOrText(text) {
  try {
    return JSON.parse(text);
  } catch {
    return text;
  }
}

function getAuthHeader() {
  const token = localStorage.getItem("token");
  if (!token) return {};
  return { Authorization: `Bearer ${token}` };
}

// ----------------------
// Auth API
// ----------------------
export async function registerUser({ fullName, email, password, confirmPassword, studentId }) {
  const res = await fetch(`${API_BASE}/api/auth/register`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ fullName, email, password, confirmPassword, studentId }),
  });
  const text = await res.text();
  const data = parseJsonOrText(text);
  if (!res.ok) throw new Error(typeof data === "string" ? data : "Register failed");
  return data;
}

export async function loginUser({ email, password }) {
  const res = await fetch(`${API_BASE}/api/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password }),
  });
  const text = await res.text();
  const data = parseJsonOrText(text);
  if (!res.ok) throw new Error(typeof data === "string" ? data : "Login failed");
  localStorage.setItem("token", data.token);
  if (data.role) localStorage.setItem("role", data.role);
  return data;
}

export function logoutUser() {
  localStorage.removeItem("token");
  localStorage.removeItem("role");
}

// ----------------------
// User API
// ----------------------
export async function getMe() {
  const res = await fetch(`${API_BASE}/api/user/me`, {
    headers: { ...getAuthHeader() },
  });
  return readApiResponse(res, "Unauthorized");
}

export async function getDashboard() {
  const res = await fetch(`${API_BASE}/api/user/dashboard`, {
    headers: { ...getAuthHeader() },
  });
  return readApiResponse(res, "Failed to load dashboard");
}

export async function updateProfile({ fullName, currentPassword, newPassword }) {
  const res = await fetch(`${API_BASE}/api/user/me`, {
    method: "PUT",
    headers: { "Content-Type": "application/json", ...getAuthHeader() },
    body: JSON.stringify({ fullName, currentPassword, newPassword }),
  });
  return readApiResponse(res, "Update failed");
}

// ----------------------
// Items & Requests
// ----------------------
export async function getItems() {
  const res = await fetch(`${API_BASE}/api/items`);
  if (!res.ok) throw new Error("Failed to fetch items");
  return res.json();
}

export async function createBorrowRequest(itemId) {
  const res = await fetch(`${API_BASE}/api/requests`, {
    method: "POST",
    headers: { "Content-Type": "application/json", ...getAuthHeader() },
    body: JSON.stringify({ itemId }),
  });
  return readApiResponse(res, "Failed to create borrow request");
}

export async function getMyRequests() {
  const res = await fetch(`${API_BASE}/api/requests/my`, {
    headers: { ...getAuthHeader() },
  });
  return readApiResponse(res, "Failed to fetch requests");
}

// ----------------------
// Admin API
// ----------------------
export async function getAdminStats() {
  const res = await fetch(`${API_BASE}/api/admin/stats`, {
    headers: { ...getAuthHeader() },
  });
  return readApiResponse(res, "Failed to fetch admin stats");
}

export async function getAdminItems() {
  const res = await fetch(`${API_BASE}/api/admin/items`, {
    headers: { ...getAuthHeader() },
  });
  return readApiResponse(res, "Failed to fetch admin items");
}

export async function createAdminItem(item) {
  const res = await fetch(`${API_BASE}/api/admin/items`, {
    method: "POST",
    headers: { "Content-Type": "application/json", ...getAuthHeader() },
    body: JSON.stringify(item),
  });
  return readApiResponse(res, "Failed to create item");
}

export async function updateAdminItem(id, item) {
  const res = await fetch(`${API_BASE}/api/admin/items/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json", ...getAuthHeader() },
    body: JSON.stringify(item),
  });
  return readApiResponse(res, "Failed to update item");
}

export async function deleteAdminItem(id) {
  const res = await fetch(`${API_BASE}/api/admin/items/${id}`, {
    method: "DELETE",
    headers: { ...getAuthHeader() },
  });
  if (res.status === 204) return null;
  return readApiResponse(res, "Failed to delete item");
}

export async function getAdminRequests() {
  const res = await fetch(`${API_BASE}/api/admin/requests`, {
    headers: { ...getAuthHeader() },
  });
  return readApiResponse(res, "Failed to fetch requests");
}

export async function decideAdminRequest(id, action) {
  const res = await fetch(`${API_BASE}/api/admin/requests/${id}/${action}`, {
    method: "PUT",
    headers: { ...getAuthHeader() },
  });
  return readApiResponse(res, `Failed to ${action} request`);
}

export async function getAdminUsers() {
  const res = await fetch(`${API_BASE}/api/admin/users`, {
    headers: { ...getAuthHeader() },
  });
  return readApiResponse(res, "Failed to fetch users");
}

export async function updateAdminUserRole(id, role) {
  const res = await fetch(`${API_BASE}/api/admin/users/${id}/role`, {
    method: "PUT",
    headers: { "Content-Type": "application/json", ...getAuthHeader() },
    body: JSON.stringify({ role }),
  });
  return readApiResponse(res, "Failed to update role");
}

// ----------------------
// Shared helper
// ----------------------
async function readApiResponse(res, fallback) {
  const text = await res.text();
  const data = text ? parseJsonOrText(text) : null;
  if (!res.ok) throw new Error(typeof data === "string" ? data : fallback);
  return data;
}