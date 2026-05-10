const KEY = "borrowbox_auth";

export function saveAuth(email, password) {
  localStorage.setItem(KEY, JSON.stringify({ email, password }));
}

export function loadAuth() {
  const raw = localStorage.getItem(KEY);
  if (!raw) return null;
  try {
    return JSON.parse(raw);
  } catch {
    return null;
  }
}

export function clearAuth() {
  localStorage.removeItem(KEY);
}