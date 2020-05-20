package com.zxs.common.aspect;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.zxs.common.annotation.SysLog;
import com.zxs.common.base.Base;
import com.zxs.common.utils.HttpContextUtils;
import com.zxs.common.utils.IPUtils;

/**
 * 系统日志，切面处理类
 */
@Aspect // 1.表明这是一个切面类
@Component
public class SysLogAspect extends Base {

    // 2. PointCut表示这是一个切点，@annotation表示这个切点切到一个注解上，后面带该注解的全类名
    // 切面最主要的就是切点，所有的故事都围绕切点发生
    // logPointCut()代表切点名称
    @Pointcut("@annotation(com.zxs.common.annotation.SysLog)")
    public void logPointCut() {

    }

    // 3. 环绕通知
    @Around("logPointCut()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = System.currentTimeMillis();
        // 获取IP地址
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        String ip = IPUtils.getIpAddr(request);
        // 获取类名名称 和 方法名称
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = className + "." +joinPoint.getSignature().getName() + "()";
        // 获取入参
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] param = joinPoint.getArgs();
        StringBuilder sb = new StringBuilder();
        for(Object o : param){
            sb.append(o + "; ");
        }
        String params = StringUtils.isNotEmpty(sb.toString()) ? sb.toString() : "空";
        LOGGER.info("IP为{}的主机，执行{}方法开始，发送的参数为：{}", ip, methodName, params);
        // 执行方法
        joinPoint.proceed();

        // 判断是否保存入库
        SysLog syslog = method.getAnnotation(SysLog.class);
        if (syslog.storage()) {
        }
        long time = System.currentTimeMillis() - beginTime;
        LOGGER.info("执行 {} 结束，耗时毫秒 {}", methodName, time);
    }

}