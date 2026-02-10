export default function OrderCard({ order, isAdmin, onAssign }) {
  return (
    <div className="order-card">
      <h4>Order #{order.id}</h4>

      <div className="order-status">Status: {order.status}</div>

      <div className="order-amount">₹ {order.amount}</div>

      {isAdmin && (
        <button
          style={{ marginTop: "10px" }}
          onClick={() => onAssign(order.id)}
        >
          Assign Courier
        </button>
      )}
    </div>
  );
}
