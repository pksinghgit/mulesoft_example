<!--

    MuleSoft Examples
    Copyright 2014 MuleSoft, Inc.

    This product includes software developed at
    MuleSoft, Inc. (http://www.mulesoft.com/).

-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ord="http://orders.se.mulesoft.com/"
	version="2.0" exclude-result-prefixes="xs">
	<xsl:output indent="yes" />
	<xsl:template match="/">
		<soapenv:Envelope >
			<soapenv:Body>
				<ord:processOrder>
					<xsl:copy-of select="order" />
				</ord:processOrder>
			</soapenv:Body>
		</soapenv:Envelope>
	</xsl:template>
</xsl:stylesheet>