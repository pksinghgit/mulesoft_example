%dw 1.0
%output application/java
---
{
	id: "123",
	price: "420",
	result: "ACCEPTED"
} as :object {
	class : "com.mulesoft.se.samsung.OrderResponse"
}