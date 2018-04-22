%dw 1.0
%output application/java
---
{
    "orderReceivedStatus": true,
    "shippingOrder": {
        "billingAddress": {
            "city": "San Francisco",
            "countryCode": "USA",
            "line1": "77 Geary St",
            "line2": "Level 4",
            "name": "Mulesoft",
            "postalCode": "94108",
            "stateOrProvinceCode": "CA"
        },
        "order": {
            "orderItemList": [
                {
                    "merchantSKU": "1234",
                    "quantity": 500
                },
                {
                    "merchantSKU": "6789",
                    "quantity": 1500
                },
                {
                    "merchantSKU": "9998",
                    "quantity": 5000
                }
            ]
        },
        "shippingAddress": {
            "city": "San Francisco",
            "countryCode": "USA",
            "line1": "77 Geary St",
            "line2": "Level 4",
            "name": "Mulesoft",
            "postalCode": "94108",
            "stateOrProvinceCode": "CA"
        },
        "shippingId": "1234"
    }
}