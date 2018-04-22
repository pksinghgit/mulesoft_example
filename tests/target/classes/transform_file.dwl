%dw 1.0
%output application/json
%function words(name) name splitBy " "
%function test(name) name replace "M" with "k"
---
contacts: payload.users.*user map using (parts =  words($.name)){
	firstName: parts[0],
	(secondName: parts[1]) when (sizeOf parts) > 2,
	lastName: parts[-1],
	email: ((lower $.name) replace " " with ".") ++ "@acme.com",
	address: $.street,
	details: using (parts=words("pavan kumar singh")){
		firstName: parts[0]  ,
		(middelName: parts[1]) when (sizeOf parts) > 2,
		(lastName: parts[-1]) when (sizeOf parts) > 1
	}
}