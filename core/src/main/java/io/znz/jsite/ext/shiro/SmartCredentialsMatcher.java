package io.znz.jsite.ext.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

/**
 * Created by Chaly on 16/8/2.
 */
public class SmartCredentialsMatcher extends HashedCredentialsMatcher {
    /**
     * Creates an instance using the specified {@link #getHashAlgorithmName() hashAlgorithmName} to hash submitted
     * credentials.
     * @param hashAlgorithmName the {@code Hash} {@link org.apache.shiro.crypto.hash.Hash#getAlgorithmName() algorithmName}
     * to use when performing hashes for credentials matching.
     * @since 1.1
     */
    public SmartCredentialsMatcher(String hashAlgorithmName) {
        super(hashAlgorithmName);
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        if (token instanceof UsernamePasswordCaptchaToken) {
            UsernamePasswordCaptchaToken upct = (UsernamePasswordCaptchaToken) token;
            if (upct.isDirect() && info.getCredentials().equals(new String(upct.getPassword()))) {
                return true;
            }
        }
        return super.doCredentialsMatch(token, info);
    }
}
