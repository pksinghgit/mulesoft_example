%dw 1.0
%output application/java
---
{
	id: "123",
	price: "450",
	result: "ACCEPTED"
} as :object {
	class : "com.mulesoft.se.samsung.OrderResponse"
}