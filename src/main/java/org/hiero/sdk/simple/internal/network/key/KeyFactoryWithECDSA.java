package org.hiero.sdk.simple.internal.network.key;

import static org.hiero.sdk.simple.internal.network.key.KeyAlgorithmUtils.ECDSA_SECP256K1_DOMAIN;

import java.security.SecureRandom;
import java.util.Arrays;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.hiero.sdk.simple.network.keys.PrivateKey;
import org.hiero.sdk.simple.network.keys.PublicKey;

public class KeyFactoryWithECDSA {

    private static final ThreadLocal<SecureRandom> secureRandom = new ThreadLocal<SecureRandom>() {
        @Override
        protected SecureRandom initialValue() {
            return new SecureRandom();
        }
    };

    private KeyFactoryWithECDSA() {
    }

    public static PublicKey createPublicKey(byte[] publicKey) {
        // Validate the key if it's not all zero public key, see HIP-540
        if (Arrays.equals(publicKey, new byte[33])) {
            return new PublicKeyWithECDSA(publicKey);
        }
        if (publicKey.length == 33 || publicKey.length == 65) {
            return new PublicKeyWithECDSA(
                    // compress and validate the key
                    KeyAlgorithmUtils.ECDSA_SECP256K1_CURVE.getCurve().decodePoint(publicKey).getEncoded(true));
        }

        // Assume a DER-encoded public key descriptor
        return fromSubjectKeyInfoInternal(SubjectPublicKeyInfo.getInstance(publicKey));
    }

    public static PrivateKey createPrivateKey() {
        var generator = new ECKeyPairGenerator();
        var keygenParams = new ECKeyGenerationParameters(ECDSA_SECP256K1_DOMAIN, secureRandom.get());
        generator.init(keygenParams);
        var keypair = generator.generateKeyPair();
        var privParams = (ECPrivateKeyParameters) keypair.getPrivate();
        return new PrivateKeyWithECDSA(privParams.getD(), null);
    }

    private static PublicKey fromSubjectKeyInfoInternal(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        return createPublicKey(subjectPublicKeyInfo.getPublicKeyData().getBytes());
    }
}
