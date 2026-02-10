import { useMutation } from "@apollo/client";
import { CREATE_ORDER } from "../graphql/mutation";
import { useState } from "react";

export default function CreateOrderModal({ onClose, refetch }) {
  const [pickupAddressId, setPickup] = useState("");
  const [deliveryAddressId, setDelivery] = useState("");
  const [productId, setProductId] = useState("");
  const [quantity, setQuantity] = useState(1);

  const [createOrder, { loading }] = useMutation(CREATE_ORDER, {
    onCompleted: () => {
      refetch();
      onClose();
    }
  });

  return (
    <div className="modal">
      <h3>Create Order</h3>

      <input
        placeholder="Pickup Address ID"
        value={pickupAddressId}
        onChange={(e) => setPickup(e.target.value)}
      />

      <input
        placeholder="Delivery Address ID"
        value={deliveryAddressId}
        onChange={(e) => setDelivery(e.target.value)}
      />

      <input
        placeholder="Product ID"
        value={productId}
        onChange={(e) => setProductId(e.target.value)}
      />

      <input
        type="number"
        placeholder="Quantity"
        value={quantity}
        onChange={(e) => setQuantity(Number(e.target.value))}
      />

      <div style={{ marginTop: "12px" }}>
        <button
          onClick={() =>
            createOrder({
              variables: {
                input: {
                  pickupAddressId,
                  deliveryAddressId,
                  items: [
                    {
                      productId,
                      quantity
                    }
                  ]
                }
              }
            })
          }
          disabled={loading}
        >
          Create
        </button>

        <button onClick={onClose} style={{ marginLeft: "8px" }}>
          Cancel
        </button>
      </div>
    </div>
  );
}
