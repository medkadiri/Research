package io.microhooks.util.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

//import lombok.extern.slf4j.Slf4j; //Understanding: Log project by Lombok where log already exist, need to clarify

@Aspect
@Component
//@Slf4j
public class Logging {

    private Log log = LogFactory.getLog(Logging.class);

    @Around("@annotation(io.microhooks.util.logging.Logged)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            log.trace("Entering: " + joinPoint.getSignature().toLongString());
            Object proceed = joinPoint.proceed();
            log.trace("Exiting: " + joinPoint.getSignature().toLongString());
            return proceed;
        } catch (Throwable throwable) {
            log.warn("An error occured at: " + joinPoint.getSignature().toLongString());
            throw throwable;
        }
    }

}