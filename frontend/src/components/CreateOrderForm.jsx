// components/CreateOrderForm.jsx
import { useMutation } from "@apollo/client";
import { CREATE_ORDER } from "../graphql/mutation";

import { useState } from "react";

export default function CreateOrderForm({ onSuccess }) {
  const [createOrder] = useMutation(CREATE_ORDER);

  const handleCreate = async () => {
    await createOrder({
      variables: {
        input: {
          pickupAddressId: 1,
          deliveryAddressId: 2,
          items: [{ productId: 1, quantity: 1 }],
        },
      },
    });
    onSuccess();
  };

  return (
    <div>
      <h3>Create Order</h3>
      <button onClick={handleCreate}>Create Dummy Order</button>
    </div>
  );
}
