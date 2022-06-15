package net.fhirfactory.pegacorn.hestia.media.im.media.cipher;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.enterprise.context.ApplicationScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.rest.api.MethodOutcome;

@ApplicationScoped
public class FileEncrypterDecrypter {
	
	private static String CIPHER_KEY = "AES/CBC/PKCS5Padding";
    private static final Logger LOG = LoggerFactory.getLogger(FileEncrypterDecrypter.class);
	
    private Cipher cipher;
    
    public FileEncrypterDecrypter() {
    	this(CIPHER_KEY);
    }
    
    protected Logger getLogger() {
        return (LOG);
    }
    
    public FileEncrypterDecrypter(String cipherKey) {
    	try {
			cipher = Cipher.getInstance(cipherKey);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
    }

	public MethodOutcome encryptAndSave(SecretKey secretKey, String fileName, byte[] content) {
		MethodOutcome outcome = null;
		try {
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);

			byte[] iv = cipher.getIV();

			FileOutputStream fileOut = new FileOutputStream(fileName);
			CipherOutputStream cipherOut = new CipherOutputStream(fileOut, cipher);

			fileOut.write(iv);
			cipherOut.write(content);
			outcome = new MethodOutcome();
			outcome.setCreated(true);
			cipherOut.close();
		} catch (Exception ex) {
			getLogger().error(".encryptAndSave(): ", ex);
			outcome = new MethodOutcome();
			outcome.setCreated(false);
		}
		return outcome;
	}
	
	public byte[] loadAndDecrypt(SecretKey key, String fileName) {
		byte[] content;

        try (FileInputStream fileIn = new FileInputStream(fileName)) {
            byte[] fileIv = new byte[16];
            fileIn.read(fileIv);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(fileIv));

            try (
                    CipherInputStream cipherIn = new CipherInputStream(fileIn, cipher)
                ) {
            	content = cipherIn.readAllBytes();
            }

            return content;

        } catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        return null;
	}
	
}
