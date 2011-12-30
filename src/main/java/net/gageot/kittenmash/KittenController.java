package net.gageot.kittenmash;

import org.simpleframework.http.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class KittenController {
	public void render(Response resp, List<String> path) throws IOException {
		String kittenId = path.get(1);
		Files.copy(Paths.get("kitten", kittenId + ".jpg"), resp.getOutputStream());
	}
}
