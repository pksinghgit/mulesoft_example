/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

boolean orderReceivedStatus='true'
def shippingOrder = new org.ordermgmt.ShippingOrder(
	shippingId = '2324234',
    billingAddress = new org.ordermgmt.Address(name='Company1_Name', line1 ='34 Smith St', line2 ='Apt.2', city='Fresno', stateOrProvinceCode='CA', countryCode='USA', postalCode='94537'),
    shippingAddress = new org.ordermgmt.Address(name='Company2_Name', line1 ='88 Smith St', line2 ='Apt.3', city='Chicago', stateOrProvinceCode='CA', countryCode='USA', postalCode='94537'),
    order = new org.ordermgmt.Order(orderItemList=[new org.ordermgmt.OrderItem(merchantSKU:'1234', quantity:200),new org.ordermgmt.OrderItem(merchantSKU:'3334', quantity:200)]
    )
   )

return shippingOrder
   