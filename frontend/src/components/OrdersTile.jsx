// components/OrdersTile.jsx
export default function OrdersTile({ orders }) {
  return (
    <div style={{ display: "flex", gap: 10 }}>
      {orders.map(o => (
        <div
          key={o.id}
          style={{
            border: "1px solid #ccc",
            padding: 10,
            width: 150,
          }}
        >
          <p><b>ID:</b> {o.id}</p>
          <p><b>Status:</b> {o.status}</p>
          <p><b>Amount:</b> ₹{o.amount}</p>
        </div>
      ))}
    </div>
  );
}
