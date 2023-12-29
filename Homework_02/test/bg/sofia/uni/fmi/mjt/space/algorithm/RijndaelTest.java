package bg.sofia.uni.fmi.mjt.space.algorithm;

import bg.sofia.uni.fmi.mjt.space.exception.CipherException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.security.auth.kerberos.EncryptionKey;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class RijndaelTest {

    @Test
    void testEncrypt() {
        byte[] buff = new byte[100];
        Rijndael rijndael = new Rijndael(new EncryptionKey(new byte[10], 1));
        try {
            rijndael.encrypt(new ByteArrayInputStream(buff), new ByteArrayOutputStream());
        } catch (CipherException e) {
            Assertions.fail();
        }
    }

    @Test
    void testDecrypt() {
        byte[] buff = new byte[100];
        Rijndael rijndael = new Rijndael(new EncryptionKey(new byte[10], 1));
        try {
            rijndael.decrypt(new ByteArrayInputStream(buff), new ByteArrayOutputStream());
        } catch (CipherException e) {
            Assertions.fail();
        }
    }
}
