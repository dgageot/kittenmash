package net.gageot.kittenmash;

import org.simpleframework.http.*;

import java.io.*;

import static com.google.common.io.Files.*;

public class KittenController {
	public void render(Response resp, String kittenId) throws IOException {
		copy(new File("kitten", kittenId + ".jpg"), resp.getOutputStream());
	}
}
