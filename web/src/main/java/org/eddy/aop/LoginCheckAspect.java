package org.eddy.aop;

import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.eddy.annotation.LoginCheck;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Optional;

@Aspect
@Component
public class LoginCheckAspect {

    private static final String FORBIDDEN = "redirect:/login/toLogin.html";
    public static final String LOGIN_ATTR_KEY = "LOGIN_ATTR_KEY";

    @Pointcut("(execution(* org.eddy.controller..*(..)) && (@annotation(org.springframework.web.bind.annotation.RequestMapping))) ")
    public void loginCheckPointcut(){}

    @Around("loginCheckPointcut()")
    public Object check(ProceedingJoinPoint point) throws Throwable {
        if (needCheck(point) && !loginCheck()) {
            return checkFailAndReturn(point);
        }
        return point.proceed();
    }

    private boolean needCheck(ProceedingJoinPoint point) {
        if (! MethodSignature.class.isAssignableFrom(point.getSignature().getClass())) {
            return false;
        }
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();

        if (method.isAnnotationPresent(LoginCheck.class)) {
            return true;
        }

        Class glass = method.getDeclaringClass();
        if (! glass.isAnnotationPresent(LoginCheck.class)) {
            return false;
        }

        LoginCheck loginCheck = (LoginCheck) glass.getAnnotation(LoginCheck.class);
        String[] methods = loginCheck.exclusions();

        return ! ArrayUtils.contains(methods, method.getName());
    }

    private Object checkFailAndReturn(ProceedingJoinPoint point) throws Throwable {
        if (! MethodSignature.class.isAssignableFrom(point.getSignature().getClass())) {
            return point.proceed();
        }
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Class r = methodSignature.getReturnType();
        if (r == String.class) {
            return FORBIDDEN;
        } else {
            return point.proceed();
        }
    }

    /**
     *  登陆校验
     * @return true:用户已登陆
     */
    private boolean loginCheck() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        return Optional.ofNullable(request.getAttribute(LOGIN_ATTR_KEY)).map(s -> true).orElseGet(() -> false);
    }
}
