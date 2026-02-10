import { useMutation } from "@apollo/client";
import { ASSIGN_COURIER } from "../graphql/mutation";
import { useState } from "react";

export default function AssignCourierModal({ orderId, onClose, refetch }) {
  const [courierId, setCourierId] = useState("");

  const [assignCourier, { loading }] = useMutation(ASSIGN_COURIER, {
    onCompleted: () => {
      refetch();
      onClose();
    }
  });

  return (
    <div className="modal">
      <h3>Assign Courier</h3>

      <input
        placeholder="Courier ID"
        value={courierId}
        onChange={(e) => setCourierId(e.target.value)}
      />

      <div style={{ marginTop: "12px" }}>
        <button
          onClick={() =>
            assignCourier({
              variables: {
                orderId,
                courierId
              }
            })
          }
          disabled={loading}
        >
          Assign
        </button>

        <button onClick={onClose} style={{ marginLeft: "8px" }}>
          Cancel
        </button>
      </div>
    </div>
  );
}
