import { useState } from "react";
import Navbar from "../../components/navbar";
import "./Items.css";

const items = [
  {
    id: 1,
    name: "Portable Projector",
    category: "Electronics",
    description: "A portable projector suitable for presentations, meetings, classes, and events.",
    available: true,
  },
  {
    id: 2,
    name: "Camping tent",
    category: "Outdoor",
    description: "A durable camping tent for field activities, outdoor programs, and group use.",
    available: true,
  },
  {
    id: 3,
    name: "Power Drill",
    category: "Tools",
    description: "A heavy-duty power drill used for repair, installation, and maintenance tasks.",
    available: false,
  },
  {
    id: 4,
    name: "DSLR Camera",
    category: "Photography",
    description: "A DSLR camera for documentation, media coverage, and photography purposes.",
    available: true,
  },
  {
    id: 5,
    name: "Bluetooth Speaker",
    category: "Audio",
    description: "A portable Bluetooth speaker useful for events, presentations, and meetings.",
    available: true,
  },
  {
    id: 6,
    name: "Ladder (6ft)",
    category: "Tools",
    description: "A 6-foot ladder for maintenance, setup, and installation work.",
    available: false,
  },
  {
    id: 7,
    name: "Extension Cord",
    category: "Electrical",
    description: "An extension cord for powering multiple devices safely in indoor or outdoor use.",
    available: true,
  },
  {
    id: 8,
    name: "Air Purifier",
    category: "Home Appliance",
    description: "An air purifier designed to improve indoor air quality in shared spaces.",
    available: true,
  },
];

export default function Items() {
  const [search, setSearch] = useState("");
  const [selectedItem, setSelectedItem] = useState(null);
  const [successMsg, setSuccessMsg] = useState("");

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

  function handleBorrowRequest() {
    setSuccessMsg(`Request submitted for "${selectedItem.name}"`);
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
            <div className="item-photo">Item photo</div>

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
            <div className="item-details-photo">Item photo</div>

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