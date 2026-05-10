import { useEffect, useState } from "react";
import { getItems, createBorrowRequest } from "../../shared/api";
import Navbar from "../../shared/components/Navbar";
import "./Items.css";

export default function Items() {
  const [search, setSearch] = useState("");
  const [selectedItem, setSelectedItem] = useState(null);
  const [successMsg, setSuccessMsg] = useState("");
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadItems() {
      try {
        const data = await getItems();
        setItems(data);
      } catch (err) {
        console.error("Failed to fetch items:", err);
      } finally {
        setLoading(false);
      }
    }

    loadItems();
  }, []);

  const filteredItems = items.filter((item) =>
    item.name.toLowerCase().includes(search.toLowerCase())
  );

  function openDetailsModal(item) {
    setSelectedItem(item);
    setSuccessMsg("");
  }

  function closeDetailsModal() {
    setSelectedItem(null);
    setSuccessMsg("");
  }

  async function handleBorrowRequest() {
  try {
    const result = await createBorrowRequest(selectedItem.id);
    setSuccessMsg(`Request submitted for "${result.itemName}"`);
  } catch (err) {
    setSuccessMsg(err.message || "Failed to submit request");
  }
}

  if (loading) {
    return (
      <div className="items-page">
        <Navbar />
        <p style={{ padding: "20px", fontSize: "18px" }}>Loading items...</p>
      </div>
    );
  }

  return (
    <div className="items-page">
      <Navbar />

      <div className="items-toolbar">
        <div className="search-box">
          <input
            type="text"
            placeholder="Search items..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>

        <button className="toolbar-btn">Filter</button>
        <button className="toolbar-btn">Sort</button>
      </div>

      <div className="items-grid">
        {filteredItems.map((item) => (
          <div
            key={item.id}
            className="item-card"
            onClick={() => openDetailsModal(item)}
          >
            <div className="item-photo">
              {item.imageUrl ? (
                <img
                  src={item.imageUrl}
                  alt={item.name}
                  style={{ width: "100%", height: "100%", objectFit: "cover" }}
                />
              ) : (
                "Item photo"
              )}
            </div>

            <div className="item-info">
              <h3>{item.name}</h3>
              <p>{item.description}</p>

              {item.available ? (
                <button
                  className="borrow-btn"
                  onClick={(e) => {
                    e.stopPropagation();
                    openDetailsModal(item);
                  }}
                >
                  Request Borrow
                </button>
              ) : (
                <button
                  className="unavailable-btn"
                  onClick={(e) => e.stopPropagation()}
                >
                  Unavailable
                </button>
              )}
            </div>
          </div>
        ))}
      </div>

      {selectedItem && (
        <div className="modal-overlay">
          <div className="item-details-modal">
            <div className="item-details-photo">
              {selectedItem.imageUrl ? (
                <img
                  src={selectedItem.imageUrl}
                  alt={selectedItem.name}
                  style={{ width: "100%", height: "100%", objectFit: "cover" }}
                />
              ) : (
                "Item photo"
              )}
            </div>

            <div className="item-details-info">
              <h2>{selectedItem.name}</h2>
              <p className="details-category">{selectedItem.category}</p>
              <p className="details-description">{selectedItem.description}</p>

              <span
                className={`details-status ${
                  selectedItem.available ? "available" : "unavailable"
                }`}
              >
                {selectedItem.available ? "Available" : "Unavailable"}
              </span>

              {successMsg && <p className="success-msg">{successMsg}</p>}

              <div className="details-actions">
                <button className="cancel-btn" onClick={closeDetailsModal}>
                  Close
                </button>

                {selectedItem.available ? (
                  <button className="confirm-btn" onClick={handleBorrowRequest}>
                    Request Borrow
                  </button>
                ) : (
                  <button className="disabled-btn">Unavailable</button>
                )}
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}