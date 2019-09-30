/**
 * 
 */
package com.carbonrider.gcp.web;

import java.nio.ByteBuffer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Storage;

/**
 * @author Carbonrider
 * 
 *         https://cloud.google.com/appengine/docs/standard/java/googlecloudstorageclient/read-write-to-cloud-storage
 * 
 *         https://github.com/googleapis/google-cloud-java/blob/master/google-cloud-examples/src/main/java/com/google/cloud/examples/storage/StorageExample.java
 * 
 *         https://github.com/spring-cloud/spring-cloud-gcp/blob/master/spring-cloud-gcp-samples/spring-cloud-gcp-integration-storage-sample/src/main/java/com/example/GcsSpringIntegrationApplication.java
 *
 */
@RestController
public class FileReaderController {

	@Autowired
	private Storage storage;

	@RequestMapping(path = { "/hello" }, method = { RequestMethod.GET })
	public Message readFromFile() throws Exception {

		StringBuilder sb = new StringBuilder();

		try (ReadChannel channel = storage.reader("carbonrider", "config.txt")) {
			ByteBuffer bytes = ByteBuffer.allocate(64 * 1024);
			while (channel.read(bytes) > 0) {
				bytes.flip();
				String data = new String(bytes.array(), 0, bytes.limit());
				sb.append(data);
				bytes.clear();
			}
		}

		Message message = new Message();
		message.setContents(sb.toString());

		return message;
	}
}

class Message {
	private String contents;

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

}