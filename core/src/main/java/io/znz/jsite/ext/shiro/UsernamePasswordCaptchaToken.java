package io.znz.jsite.ext.shiro;


import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 扩展添加验证码-继承用户验证类
 * @author Chaly
 */
public class UsernamePasswordCaptchaToken extends UsernamePasswordToken {

    private String captcha;

    public boolean isDirect() {
        return direct;
    }

    public void setDirect(boolean direct) {
        this.direct = direct;
    }

    //是否可以直接使用密文直接登录
    private boolean direct = false;

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public UsernamePasswordCaptchaToken(String username, String password, boolean rememberMe, String captcha,
        String host
    ) {
        super(username, password, rememberMe, host);
        this.captcha = captcha;
    }

}
