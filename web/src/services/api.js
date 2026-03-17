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
export async function registerUser({ fullName, email, password }) {
  const res = await fetch(`${API_BASE}/api/auth/register`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ fullName, email, password }),
  });

  const text = await res.text();
  const data = parseJsonOrText(text);

  if (!res.ok) {
    throw new Error(typeof data === "string" ? data : "Register failed");
  }

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

  if (!res.ok) {
    throw new Error(typeof data === "string" ? data : "Login failed");
  }

  // ✅ Save JWT
  localStorage.setItem("token", data.token);

  return data;
}

export function logoutUser() {
  localStorage.removeItem("token");
}

// ----------------------
// Protected API
// ----------------------
export async function getMe() {
  const res = await fetch(`${API_BASE}/api/user/me`, {
    method: "GET",
    headers: {
      ...getAuthHeader(),
    },
  });

  const text = await res.text();
  const data = parseJsonOrText(text);

  if (!res.ok) {
    throw new Error(typeof data === "string" ? data : "Unauthorized");
  }

  return data;
}

export async function getItems() {
  const res = await fetch("http://localhost:8080/api/items");

  if (!res.ok) {
    throw new Error("Failed to fetch items");
  }

  return res.json();
}