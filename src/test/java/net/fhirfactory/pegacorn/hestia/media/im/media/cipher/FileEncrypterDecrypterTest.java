package net.fhirfactory.pegacorn.hestia.media.im.media.cipher;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FileEncrypterDecrypterTest {

	@Test
	void test() {
		FileEncrypterDecrypter fed = new FileEncrypterDecrypter();
		String content = "content123";
		String encryption = fed.encrypt("123", content);
//		assertNotEquals("", encryption);
		assertNotEquals(content, encryption);
	}

}
