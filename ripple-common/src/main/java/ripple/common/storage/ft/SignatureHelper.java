// Copyright (c) 2024 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.storage.ft;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Zhen Tang
 */
public final class SignatureHelper {
    private SignatureHelper() {

    }

    public static byte[] calculateSignature(byte[] value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            return digest.digest(value);
        } catch (NoSuchAlgorithmException exception) {
            return null;
        }
    }

    public static String getSignatureFileName(String key) {
        return key + ".signature";
    }
}
