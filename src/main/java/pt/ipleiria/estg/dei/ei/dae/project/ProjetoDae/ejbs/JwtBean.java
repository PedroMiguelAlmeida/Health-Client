import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.JWSHeader.Builder;
import com.nimbusds.jose.crypto.RSASSASigner;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

@Stateless(
        name = "JwtEJB"
)
public class JwtBean {
    private static final PrivateKey privateKey;
    private static final int TOKEN_VALIDITY = 14400;
    private static final String CLAIM_ROLES = "groups";
    private static final String ISSUER = "quickstart-jwt-issuer";
    private static final String AUDIENCE = "jwt-audience";

    public JwtBean() {
    }

    public String createJwt(String subject, String[] roles) throws Exception {
        JWSSigner signer = new RSASSASigner(privateKey);
        JsonArrayBuilder rolesBuilder = Json.createArrayBuilder();
        String[] var5 = roles;
        int var6 = roles.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            String role = var5[var7];
            rolesBuilder.add(role);
        }

        JsonObjectBuilder claimsBuilder = Json.createObjectBuilder().add("sub", subject).add("iss", "quickstart-jwt-issuer").add("aud", "jwt-audience").add("groups", rolesBuilder.build()).add("exp", System.currentTimeMillis() / 1000L + 14400L);
        JWSObject jwsObject = new JWSObject((new Builder(JWSAlgorithm.RS256)).type(new JOSEObjectType("jwt")).build(), new Payload(claimsBuilder.build().toString()));
        jwsObject.sign(signer);
        return jwsObject.serialize();
    }

    static {
        FileInputStream fis = null;
        char[] password = "jwtsecret".toCharArray();
        String alias = "alias";
        PrivateKey pk = null;

        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            String configDir = System.getProperty("jboss.server.config.dir");
            String keystorePath = configDir + File.separator + "jwt.keystore";
            fis = new FileInputStream(keystorePath);
            ks.load(fis, password);
            Key key = ks.getKey(alias, password);
            if (key instanceof PrivateKey) {
                pk = (PrivateKey)key;
            }
        } catch (Exception var16) {
            var16.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException var15) {
                }
            }

        }

        privateKey = pk;
    }
}