package io.znz.jsite.core.aop;

import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * aop记录方法执行时间
 * Created by Chaly on 16/4/11.
 */
@Aspect
@Component
public class MethodTimeAspect {

    protected Logger log = LoggerFactory.getLogger(MethodTimeAspect.class);

    @Around("execution(* *..service..*(..)) || execution(* io.znz.jsite.base.BaseService+.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //用 commons-lang3 提供的 StopWatch 计时，Spring 也提供了一个 StopWatch
        StopWatch clock = new StopWatch();
        clock.start(); //计时开始
        Object result = pjp.proceed();
        clock.stop();  //计时结束
        Signature signature = pjp.getSignature();

        //获取类型信息
        String classImplName = pjp.getTarget().getClass().getName();
        String classAbstName = signature.getDeclaringTypeName();
        String methodName = signature.toLongString().replace(classAbstName, classImplName);

        //输出日志
        log.debug("Takes:" + clock.getTime() + " ms [" + methodName + "]");
        //返回原有结果
        return result;
    }
}
