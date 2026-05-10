import { useEffect, useState } from "react";
import {
  createAdminItem,
  deleteAdminItem,
  getAdminItems,
  updateAdminItem,
} from "../../shared/api";
import AdminNav from "./AdminNav";
import "./Admin.css";

const emptyForm = {
  name: "",
  category: "",
  description: "",
  available: true,
  imageUrl: "",
};

export default function AdminItems() {
  const [items, setItems] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [editingId, setEditingId] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    loadItems();
  }, []);

  async function loadItems() {
    try {
      setItems(await getAdminItems());
    } catch (err) {
      setError(err.message);
    }
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    try {
      if (editingId) {
        await updateAdminItem(editingId, form);
      } else {
        await createAdminItem(form);
      }
      setForm(emptyForm);
      setEditingId(null);
      loadItems();
    } catch (err) {
      setError(err.message);
    }
  }

  function startEdit(item) {
    setEditingId(item.id);
    setForm({
      name: item.name || "",
      category: item.category || "",
      description: item.description || "",
      available: Boolean(item.available),
      imageUrl: item.imageUrl || "",
    });
  }

  async function removeItem(id) {
    if (!window.confirm("Delete this item?")) return;
    try {
      await deleteAdminItem(id);
      loadItems();
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
            <h1>Manage Items</h1>
            <p>Add, edit, and delete inventory items.</p>
          </div>
        </header>

        {error && <div className="admin-alert">{error}</div>}

        <form className="admin-form" onSubmit={handleSubmit}>
          <label>
            Name
            <input value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} required />
          </label>
          <label>
            Category
            <input value={form.category} onChange={(e) => setForm({ ...form, category: e.target.value })} required />
          </label>
          <label className="wide">
            Description
            <textarea value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} required />
          </label>
          <label>
            Image URL
            <input value={form.imageUrl} onChange={(e) => setForm({ ...form, imageUrl: e.target.value })} />
          </label>
          <label>
            Availability
            <select
              value={form.available ? "true" : "false"}
              onChange={(e) => setForm({ ...form, available: e.target.value === "true" })}
            >
              <option value="true">Available</option>
              <option value="false">Unavailable</option>
            </select>
          </label>
          <div className="admin-actions wide">
            <button className="admin-primary" type="submit">
              {editingId ? "Save Item" : "Add Item"}
            </button>
            {editingId && (
              <button
                className="admin-secondary"
                type="button"
                onClick={() => {
                  setEditingId(null);
                  setForm(emptyForm);
                }}
              >
                Cancel
              </button>
            )}
          </div>
        </form>

        <section className="admin-panel">
          <table className="admin-table">
            <thead>
              <tr>
                <th>Name</th>
                <th>Category</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {items.map((item) => (
                <tr key={item.id}>
                  <td>{item.name}</td>
                  <td>{item.category}</td>
                  <td>{item.available ? "Available" : "Unavailable"}</td>
                  <td>
                    <div className="admin-actions">
                      <button className="admin-secondary" type="button" onClick={() => startEdit(item)}>Edit</button>
                      <button className="admin-danger" type="button" onClick={() => removeItem(item.id)}>Delete</button>
                    </div>
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
