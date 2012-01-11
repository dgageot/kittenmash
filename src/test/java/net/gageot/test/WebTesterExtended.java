package net.gageot.test;

import com.google.common.base.*;
import net.sourceforge.jwebunit.junit.*;

import java.net.*;

import static org.apache.commons.lang.StringUtils.*;

public class WebTesterExtended extends WebTester {
	public void assertDownloadedFileEquals(String file) {
		try {
			assertDownloadedFileEquals(new URL("file:" + file));
		} catch (MalformedURLException e) {
			throw Throwables.propagate(e);
		}
	}

	@Override
	public void setBaseUrl(String url) {
		super.setBaseUrl(removeEnd(url, "/") + "/");
	}
}
