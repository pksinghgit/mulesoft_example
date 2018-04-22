CREATE DATABASE ETL;

CREATE TABLE ETL.`ORDERS` (
  `OrderID` int(11) NOT NULL,
  `RetailerName` varchar(45) DEFAULT NULL,
  `SKUs` varchar(45) DEFAULT NULL,
  `OrderDate` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE ETL.`SKU` (
  `SKU` int(11) NOT NULL,
  `Product` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`SKU`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE ETL.`ORDER_STATUS` (
  `OrderID` int(11) NOT NULL,
  `Status` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`OrderID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE ETL.`STATUS_REPORT` (
  `OrderID` int(11) NOT NULL,
  `Products` varchar(90) DEFAULT NULL,
  `RetailerName` varchar(45) DEFAULT NULL,
  `Status` varchar(45) DEFAULT NULL,
  `OrderDate` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT INTO `ETL`.`ORDERS`
(`OrderID`,
`RetailerName`,
`SKUs`,
`OrderDate`)
VALUES
(123,
'Zapper Inc',
'1,2',
'2016-02-04');


INSERT INTO `ETL`.`SKU`
(`SKU`,
`Product`)
VALUES
(1,
'Samsung Galaxy S6');

INSERT INTO `ETL`.`SKU`
(`SKU`,
`Product`)
VALUES
(2,
'Unlimited Talk Time Plan');

INSERT INTO `ETL`.`ORDER_STATUS`
(`OrderID`,
`Status`)
VALUES
(123,
'Shipped');






