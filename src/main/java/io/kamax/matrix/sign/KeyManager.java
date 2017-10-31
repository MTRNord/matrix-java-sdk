/*
 * matrix-java-sdk - Matrix Client SDK for Java
 * Copyright (C) 2017 Maxime Dor
 *
 * https://max.kamax.io/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package io.kamax.matrix.sign;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.KeyPairGenerator;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class KeyManager {

    private String storagePath;

    private EdDSAParameterSpec keySpecs;
    private List<KeyPair> keys;

    public KeyManager(String storagePath) {
        this.storagePath = storagePath;

        try {
            keySpecs = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.CURVE_ED25519_SHA512);
            keys = new ArrayList<>();

            Path privKey = Paths.get(storagePath);
            if (Files.isDirectory(privKey)) {
                throw new RuntimeException("Invalid path for private key: " + privKey.toString());
            }

            if (!Files.exists(privKey)) {
                KeyPair pair = (new KeyPairGenerator()).generateKeyPair();
                String keyEncoded = Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded());
                FileUtils.writeStringToFile(privKey.toFile(), keyEncoded, StandardCharsets.ISO_8859_1);
                keys.add(pair);
            } else {
                if (!Files.isReadable(privKey)) {
                    throw new RuntimeException("Signing keys file is not readable: " + storagePath);
                }

                byte[] seed = Base64.getDecoder()
                        .decode(FileUtils.readFileToString(privKey.toFile(), StandardCharsets.ISO_8859_1));
                EdDSAPrivateKeySpec privKeySpec = new EdDSAPrivateKeySpec(seed, keySpecs);
                EdDSAPublicKeySpec pubKeySpec = new EdDSAPublicKeySpec(privKeySpec.getA(), keySpecs);
                keys.add(new KeyPair(new EdDSAPublicKey(pubKeySpec), new EdDSAPrivateKey(privKeySpec)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getCurrentIndex() {
        return 0;
    }

    public KeyPair getKeys(int index) {
        return keys.get(index);
    }

    public PrivateKey getPrivateKey(int index) {
        return getKeys(index).getPrivate();
    }

    public EdDSAPublicKey getPublicKey(int index) {
        return (EdDSAPublicKey) getKeys(index).getPublic();
    }

    public EdDSAParameterSpec getSpecs() {
        return keySpecs;
    }

    public String getPublicKeyBase64(int index) {
        return Base64.getEncoder().encodeToString(getPublicKey(index).getAbyte());
    }

}
