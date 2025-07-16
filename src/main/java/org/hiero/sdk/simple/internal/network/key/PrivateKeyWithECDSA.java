package org.hiero.sdk.simple.internal.network.key;

import static org.hiero.sdk.simple.internal.network.key.KeyAlgorithmUtils.ECDSA_SECP256K1_DOMAIN;

import java.math.BigInteger;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;
import org.bouncycastle.util.Arrays;
import org.hiero.sdk.simple.network.keys.PrivateKey;
import org.hiero.sdk.simple.network.keys.PublicKey;
import org.jspecify.annotations.Nullable;

public record PrivateKeyWithECDSA(BigInteger keyData, @Nullable KeyParameter chainCode) implements PrivateKey {

    @Override
    public PublicKey createPublicKey() {
        var q = ECDSA_SECP256K1_DOMAIN.getG().multiply(keyData);
        var publicParams = new ECPublicKeyParameters(q, ECDSA_SECP256K1_DOMAIN);
        return KeyFactoryWithECDSA.createPublicKey(publicParams.getQ().getEncoded(true));
    }

    @Override
    public byte[] sign(byte[] message) {
        var hash = KeyAlgorithmUtils.calcKeccak256(message);
        var signer = new ECDSASigner(new HMacDSAKCalculator(new SHA256Digest()));
        signer.init(true, new ECPrivateKeyParameters(keyData, ECDSA_SECP256K1_DOMAIN));
        BigInteger[] bigSig = signer.generateSignature(hash);
        byte[] sigBytes = Arrays.copyOf(KeyAlgorithmUtils.bigIntTo32Bytes(bigSig[0]), 64);
        System.arraycopy(KeyAlgorithmUtils.bigIntTo32Bytes(bigSig[1]), 0, sigBytes, 32, 32);
        return sigBytes;
    }

}
