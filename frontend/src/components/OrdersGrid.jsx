// components/OrdersGrid.jsx
export default function OrdersGrid({ orders }) {
  return (
    <table border="1" width="100%">
      <thead>
        <tr>
          <th>ID</th>
          <th>Status</th>
          <th>Amount</th>
        </tr>
      </thead>
      <tbody>
        {orders.map(o => (
          <tr key={o.id}>
            <td>{o.id}</td>
            <td>{o.status}</td>
            <td>₹{o.amount}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
