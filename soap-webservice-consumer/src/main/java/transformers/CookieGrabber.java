package transformers;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;
import org.mule.transformer.AbstractMessageTransformer;
import org.mule.transport.http.CookieHelper;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Cookie;

public class CookieGrabber extends AbstractMessageTransformer {

	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
		Object _CookieHeader = message.getInboundProperty("Cookie");
		List<Cookie> _CookieList = null;
		Map<String, String> _CookieMap = new HashMap<String, String>();

		try {
			// Grab the cookies from the header and put them into a List
			_CookieList = (List<Cookie>) Arrays.asList(CookieHelper.parseCookiesAsAServer(_CookieHeader.toString(),
					new URI("" + message.getInboundProperty("host"))));
			// And put them in a convenient List which can be accessed from the
			// flow
			message.setProperty("incomingCookieList", _CookieList, PropertyScope.SESSION);

			// Let's also put them in a nice Map, since incoming cookies will
			// usually only contain a name and a value, so let's get easy access
			// to them by their name.
			for (Cookie _Cookie : _CookieList) {
				_CookieMap.put(_Cookie.getName(), _Cookie.getValue());
			}
			message.setProperty("incomingCookieMap", _CookieMap, PropertyScope.SESSION);

		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return message;
	}
}
