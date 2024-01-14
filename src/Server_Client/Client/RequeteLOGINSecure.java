package Server_Client.Client;

import Crypto.MyCrypto;
import Server_Client.Requete;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

public class RequeteLOGINSecure implements Requete, Serializable
{
    private String login;
    private String password;
    private boolean isnew;
    private long temps;
    private double alea;
    private byte[] digest;
    private byte[]clesession;

    public RequeteLOGINSecure(String l, String p, boolean iznew, byte[] cleSession) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        login = l;
        password = p;
        isnew = iznew;
        clesession = cleSession;

        this.temps = new Date().getTime();
        this.alea = Math.random();


        MessageDigest md = MessageDigest.getInstance("SHA-1","BC");
        md.update(login.getBytes());
        md.update(password.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeLong(temps);
        dos.writeDouble(alea);
        md.update(baos.toByteArray());
        digest = md.digest();

    }
    public boolean VerifyPassword(String password) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {

        MessageDigest md = MessageDigest.getInstance("SHA-1","BC");
        md.update(login.getBytes());
        md.update(password.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeLong(temps);
        dos.writeDouble(alea);
        md.update(baos.toByteArray());
        byte[] digestLocal = md.digest();

        return MessageDigest.isEqual(digest,digestLocal);
    }
    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public boolean isIsnew() {
        return isnew;
    }

    public byte[] getClesession() {
        return clesession;
    }

}
