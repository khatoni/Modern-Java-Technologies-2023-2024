package bg.sofia.uni.fmi.mjt.space.algorithm;

import bg.sofia.uni.fmi.mjt.space.exception.CipherException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;

public class Rijndael implements SymmetricBlockCipher {

    private SecretKey secretKey;
    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final int KILOBYTE = 1024;

    public Rijndael(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public void encrypt(InputStream inputStream, OutputStream outputStream) throws CipherException {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new CipherException("Problem generating Cipher");
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        } catch (InvalidKeyException | InvalidParameterException | UnsupportedOperationException e) {
            throw new CipherException("Problem during cipher.init");
        }
        try (OutputStream encryptedOutputStream = new CipherOutputStream(outputStream,
            cipher)) {

            byte[] buffer = new byte[KILOBYTE];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                encryptedOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new CipherException("Problem with auto-closeable");
        }

    }

    @Override
    public void decrypt(InputStream inputStream, OutputStream outputStream) throws CipherException {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new CipherException("Problem generating Cipher", e);
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
        } catch (InvalidKeyException | InvalidParameterException | UnsupportedOperationException e) {
            throw new CipherException("Problem during cipher.init");
        }

        try (OutputStream decryptedOutputStream = new CipherOutputStream(outputStream,
            cipher)) {

            byte[] buffer = new byte[KILOBYTE];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                decryptedOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new CipherException("Problem with auto-closeable");
        }
    }
}
