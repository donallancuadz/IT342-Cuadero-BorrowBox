import Navbar from "../../components/navbar";
import "./Requests.css";

const requests = [
  {
    id: 1,
    item: "Portable Projector",
    date: "Mar 5",
    status: "Active",
  },
  {
    id: 2,
    item: "Camping Tent",
    date: "Mar 4",
    status: "Pending",
  },
  {
    id: 3,
    item: "Power Drill",
    date: "Feb 28",
    status: "Returned",
  },
];

export default function Requests() {
  return (
    <div className="requests-page">
      <Navbar />

      <div className="requests-container">
        <h2>My Requests</h2>

        <div className="requests-table">
          <div className="table-header">
            <span>Item</span>
            <span>Date</span>
            <span>Status</span>
          </div>

          {requests.map((req) => (
            <div key={req.id} className="table-row">
              <span>{req.item}</span>
              <span>{req.date}</span>
              <span className={`status ${req.status.toLowerCase()}`}>
                {req.status}
              </span>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}