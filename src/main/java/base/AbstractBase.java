package base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.PropertyUtil;

public abstract class AbstractBase {

	private static final Logger logger = LoggerFactory.getLogger(AbstractBase.class);
	
	protected String getProperty(String key) {
		String value = PropertyUtil.getProperty(key);
		logger.debug("Retrieved property: {} = {}", key, value);
		return value;
	}
}
