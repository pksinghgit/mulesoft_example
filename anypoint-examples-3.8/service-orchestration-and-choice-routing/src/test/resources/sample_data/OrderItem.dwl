%dw 1.0
%output application/java
---
{
    "manufacturer": "Samsung",
    "name": "s-1",
    "productId": "AX02",
    "purchaseReceipt": null,
    "quantity": 1
} as :object {
	class : "com.mulesoft.se.orders.OrderItem"
}