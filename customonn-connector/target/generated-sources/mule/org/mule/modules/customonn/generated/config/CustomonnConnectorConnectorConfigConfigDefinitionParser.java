
package org.mule.modules.customonn.generated.config;

import javax.annotation.Generated;
import org.mule.config.MuleManifest;
import org.mule.modules.customonn.config.ConnectorConfig;
import org.mule.modules.customonn.generated.adapters.CustomonnConnectorConnectorConfigBasicAdapter;
import org.mule.security.oauth.config.AbstractDevkitBasedDefinitionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;
import org.springframework.beans.factory.parsing.Location;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

@SuppressWarnings("all")
@Generated(value = "Mule DevKit Version 3.9.0", date = "2017-11-20T09:42:04+05:30", comments = "Build UNNAMED.2793.f49b6c7")
public class CustomonnConnectorConnectorConfigConfigDefinitionParser
    extends AbstractDevkitBasedDefinitionParser
{

    private static Logger logger = LoggerFactory.getLogger(CustomonnConnectorConnectorConfigConfigDefinitionParser.class);

    public String moduleName() {
        return "Customonn";
    }

    public BeanDefinition parse(Element element, ParserContext parserContext) {
        parseConfigName(element);
        BeanDefinitionBuilder builder = getBeanDefinitionBuilder(parserContext);
        builder.setScope(BeanDefinition.SCOPE_SINGLETON);
        setInitMethodIfNeeded(builder, CustomonnConnectorConnectorConfigBasicAdapter.class);
        setDestroyMethodIfNeeded(builder, CustomonnConnectorConnectorConfigBasicAdapter.class);
        BeanDefinitionBuilder basicConfigBuilder = BeanDefinitionBuilder.rootBeanDefinition(ConnectorConfig.class.getName());
        parseProperty(basicConfigBuilder, element, "greeting", "greeting");
        parseProperty(basicConfigBuilder, element, "reply", "reply");
        builder.addPropertyValue("config", basicConfigBuilder.getBeanDefinition());
        BeanDefinition definition = builder.getBeanDefinition();
        setNoRecurseOnDefinition(definition);
        return definition;
    }

    private BeanDefinitionBuilder getBeanDefinitionBuilder(ParserContext parserContext) {
        try {
            return BeanDefinitionBuilder.rootBeanDefinition(CustomonnConnectorConnectorConfigBasicAdapter.class.getName());
        } catch (NoClassDefFoundError noClassDefFoundError) {
            String muleVersion = "";
            try {
                muleVersion = MuleManifest.getProductVersion();
            } catch (Exception _x) {
                logger.error("Problem while reading mule version");
            }
            logger.error(("Cannot launch the mule app, the configuration [config] within the connector [customonn] is not supported in mule "+ muleVersion));
            throw new BeanDefinitionParsingException(new Problem(("Cannot launch the mule app, the configuration [config] within the connector [customonn] is not supported in mule "+ muleVersion), new Location(parserContext.getReaderContext().getResource()), null, noClassDefFoundError));
        }
    }

}
