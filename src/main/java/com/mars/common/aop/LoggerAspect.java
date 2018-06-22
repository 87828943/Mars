package com.mars.common.aop;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggerAspect {

    private Logger logger =  LoggerFactory.getLogger(this.getClass());

    //before
    @Before(value = "within(com.mars..*) && @annotation(loggerAnnotation)")
    public void beforeLogger(JoinPoint joinPoint, LoggerAnnotation loggerAnnotation){
        logger.info("--------------this ["+loggerAnnotation.desc()+"] start--------------");
        logger.info("--------------target ["+joinPoint.getTarget()+"]");
        logger.info("--------------method ["+joinPoint.getSignature().getName()+"]");
        logger.info("--------------param ["+parseParames(joinPoint.getArgs())+"]");
    }

    //return
    @AfterReturning(value = "within(com.mars..*) && @annotation(loggerAnnotation)")
    public void afterReturningLogger(JoinPoint joinPoint, LoggerAnnotation loggerAnnotation){
        logger.info("--------------this ["+loggerAnnotation.desc()+"] end--------------");
    }

    //throwing
    @AfterThrowing(pointcut = "within(com.mars..*) && @annotation(loggerAnnotation)",throwing = "e")
    public void afterThrowingLogger(JoinPoint joinPoint, LoggerAnnotation loggerAnnotation,Exception e){
        logger.info("--------------error ["+loggerAnnotation.desc()+"]",e);
    }

    private String parseParames(Object[] parames) {
        if (null == parames || parames.length <= 0 || parames.length >10240) {
            return "";
        }
        StringBuffer param = new StringBuffer("");
        for (Object obj : parames) {
            param.append(ToStringBuilder.reflectionToString(obj)).append("  ");
        }
        return param.toString();
    }

}
