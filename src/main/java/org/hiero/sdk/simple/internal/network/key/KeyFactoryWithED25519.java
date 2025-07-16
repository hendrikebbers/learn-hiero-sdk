package org.hiero.sdk.simple.internal.network.key;

import java.security.SecureRandom;
import java.util.Arrays;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.math.ec.rfc8032.Ed25519;
import org.hiero.sdk.simple.network.keys.PrivateKey;
import org.hiero.sdk.simple.network.keys.PublicKey;

public class KeyFactoryWithED25519 {

    private static final ThreadLocal<SecureRandom> secureRandom = new ThreadLocal<SecureRandom>() {
        @Override
        protected SecureRandom initialValue() {
            return new SecureRandom();
        }
    };

    private KeyFactoryWithED25519() {
    }

    public static PublicKey createPublicKey(byte[] publicKey) {
        if (publicKey.length == Ed25519.PUBLIC_KEY_SIZE) {
            // Validate the key if it's not all zero public key, see HIP-540
            if (!Arrays.equals(publicKey, new byte[32])) {
                // Will throw if the key is invalid
                new Ed25519PublicKeyParameters(publicKey, 0);
            }
            // If this is a 32 byte string, assume an Ed25519 public key
            return new PublicKeyWithED25519(publicKey);
        }

        // Assume a DER-encoded public key descriptor
        return fromSubjectKeyInfoInternal(SubjectPublicKeyInfo.getInstance(publicKey));
    }

    public static PrivateKey createPrivateKey() {
        // extra 32 bytes for chain code
        byte[] data = new byte[Ed25519.SECRET_KEY_SIZE + 32];
        secureRandom.get().nextBytes(data);
        var keyData = Arrays.copyOfRange(data, 0, 32);
        var chainCode = new KeyParameter(data, 32, 32);
        return new PrivateKeyWithED25519(keyData, chainCode);
    }

    private static PublicKey fromSubjectKeyInfoInternal(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        return new PublicKeyWithED25519(subjectPublicKeyInfo.getPublicKeyData().getBytes());
    }
}
