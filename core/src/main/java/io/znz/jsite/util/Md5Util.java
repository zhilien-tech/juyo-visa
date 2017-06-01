package io.znz.jsite.util;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import org.apache.commons.io.Charsets;

/**
 * Created by Chaly on 16/8/29.
 */
public class Md5Util {
    public static String md5(String str) {
        Hasher hasher = Hashing.md5().newHasher();
        hasher.putString(str, Charsets.UTF_8);
        String hash = hasher.hash().toString();
        return hash;
    }

    public static String md5(int str) {
        Hasher hasher = Hashing.md5().newHasher();
        hasher.putInt(str);
        String hash = hasher.hash().toString();
        return hash;
    }
}
