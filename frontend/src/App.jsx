import { useState } from "react";


export default function App() {
  const [role, setRole] = useState("EMPLOYEE");

  return (
    <div className="container">
      <div className="header">
        <span>🚚</span>
        Transportation Management System
      </div>

      <Header role={role} setRole={setRole} />
      <OrdersPage role={role} />
    </div>
  );
}

