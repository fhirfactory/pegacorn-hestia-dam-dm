package net.fhirfactory.pegacorn.hestia.media.im.media.cipher;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ca.uhn.fhir.rest.api.MethodOutcome;

class FileEncrypterDecrypterTest {

	@Test
	void testSave() {
		FileEncrypterDecrypter fed = new FileEncrypterDecrypter();
		try {
			SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();
			String filename = "filename.enc";
			String content = "content123";

			MethodOutcome outcome = fed.encryptAndSave(secretKey, filename, content);
			Assertions.assertTrue(outcome.getCreated());
			
			String returnedContent = fed.loadAndDecrypt(secretKey, filename);
			Assertions.assertEquals(content, returnedContent);

		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Assertions.fail(e1);
		}

	}
	
	

}
