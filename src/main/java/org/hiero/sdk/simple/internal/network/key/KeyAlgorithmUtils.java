package org.hiero.sdk.simple.internal.network.key;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.hiero.sdk.simple.network.keys.Key;

public abstract class KeyAlgorithmUtils implements Key {

    static final ASN1ObjectIdentifier ID_ED25519 = new ASN1ObjectIdentifier("1.3.101.112");
    static final ASN1ObjectIdentifier ID_ECDSA_SECP256K1 = new ASN1ObjectIdentifier("1.3.132.0.10");
    static final ASN1ObjectIdentifier ID_EC_PUBLIC_KEY = new ASN1ObjectIdentifier("1.2.840.10045.2.1");

    static final X9ECParameters ECDSA_SECP256K1_CURVE = SECNamedCurves.getByName("secp256k1");
    static final ECDomainParameters ECDSA_SECP256K1_DOMAIN = new ECDomainParameters(
            ECDSA_SECP256K1_CURVE.getCurve(),
            ECDSA_SECP256K1_CURVE.getG(),
            ECDSA_SECP256K1_CURVE.getN(),
            ECDSA_SECP256K1_CURVE.getH());

    static byte[] calcKeccak256(byte[] message) {
        var digest = new Keccak.Digest256();
        digest.update(message);
        return digest.digest();
    }

    static byte[] bigIntTo32Bytes(BigInteger n) {
        byte[] bytes = n.toByteArray();
        byte[] bytes32 = new byte[32];
        System.arraycopy(
                bytes,
                Math.max(0, bytes.length - 32),
                bytes32,
                Math.max(0, 32 - bytes.length),
                Math.min(32, bytes.length));
        return bytes32;
    }
}
