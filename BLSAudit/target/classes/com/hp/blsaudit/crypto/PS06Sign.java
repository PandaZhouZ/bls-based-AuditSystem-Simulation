package com.hp.blsaudit.crypto;

import com.hp.blsaudit.configure.Configure;
import it.unisa.dia.gas.crypto.jpbc.signature.ps06.engines.PS06Signer;
import it.unisa.dia.gas.crypto.jpbc.signature.ps06.generators.PS06ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.signature.ps06.generators.PS06SecretKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.signature.ps06.generators.PS06SetupGenerator;
import it.unisa.dia.gas.crypto.jpbc.signature.ps06.params.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.digests.SHA512Digest;
import static org.junit.Assert.fail;


public class PS06Sign {
    public static AsymmetricCipherKeyPair setUp(PS06Parameters parameters) {
        PS06SetupGenerator setup = new PS06SetupGenerator();
        setup.init(new PS06SetupGenerationParameters(null, parameters));

        return setup.generateKeyPair();
    }

    public static PS06Parameters createParameters(int nU, int nM) {
        return new PS06ParametersGenerator().init(PairingFactory.getPairingParameters(Configure.PROPROOT + "typeAparams.properties"), nU, nM).generateParameters();
    }

    public static CipherParameters extract(AsymmetricCipherKeyPair keyPair, String identity) {
        PS06SecretKeyGenerator ext = new PS06SecretKeyGenerator();
        ext.init(new PS06SecretKeyGenerationParameters(keyPair, identity));

        return ext.generateKey();
    }

    public static byte[] sign(String message, CipherParameters secretKey) {
        byte[] bytes = message.getBytes();

        PS06Signer signer = new PS06Signer(new SHA512Digest());
        signer.init(true, new PS06SignParameters((PS06SecretKeyParameters)secretKey));
        signer.update(bytes, 0, bytes.length);

        byte[] signature = null;
        try {
            signature = signer.generateSignature();
        } catch (CryptoException e) {
            fail(e.getMessage());
        }

        return signature;
    }

    public static boolean verify(CipherParameters publicKey, String message, String identity, byte[] signature) {
        byte[] bytes = message.getBytes();

        PS06Signer signer = new PS06Signer(new SHA512Digest());
        signer.init(false, new PS06VerifyParameters((PS06PublicKeyParameters)publicKey, identity));
        signer.update(bytes, 0, bytes.length);

        return signer.verifySignature(signature);
    }
}
