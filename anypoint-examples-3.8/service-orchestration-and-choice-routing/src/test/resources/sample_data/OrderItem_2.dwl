%dw 1.0
%output application/java
---
{
	manufacturer: "Samsung",
	name: "Galaxy",
	productId: "i1900",
	purchaseReceipt: null,
	quantity: 1
} as :object {
	class : "com.mulesoft.se.orders.OrderItem"
}