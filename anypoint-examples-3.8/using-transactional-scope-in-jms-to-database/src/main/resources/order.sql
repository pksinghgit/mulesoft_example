CREATE TABLE orders (
  id int(11) NOT NULL AUTO_INCREMENT,
  item_id int(11) NOT NULL,
  item_units int(11) NOT NULL,
  customer_id int(11) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8;


