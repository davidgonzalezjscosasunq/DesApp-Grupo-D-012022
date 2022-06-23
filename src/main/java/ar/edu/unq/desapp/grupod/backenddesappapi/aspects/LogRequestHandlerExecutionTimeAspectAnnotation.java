package ar.edu.unq.desapp.grupod.backenddesappapi.aspects;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.Timestamp;
import java.time.Instant;

@Aspect
@Slf4j
@Component
public class LogRequestHandlerExecutionTimeAspectAnnotation {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controller() { /* Comment added to avoid SonarCloud marking this empty method as a code smell */ }

    @Pointcut("execution(* *.*(..))")
    protected void allMethods() { /* Comment added to avoid SonarCloud marking this empty method as a code smell */ }

    @Around("controller() && allMethods()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        var timestamp = Timestamp.from(Instant.now());

        var startTime = System.currentTimeMillis();
        var objectToReturn = joinPoint.proceed();
        var executionTime = System.currentTimeMillis() - startTime;

        var httpRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var httpMethod = httpRequest.getMethod();
        var requestURI = httpRequest.getRequestURI();

        var signature = joinPoint.getSignature();
        var arguments = new ObjectMapper().writeValueAsString(joinPoint.getArgs());

        logInfo(timestamp, executionTime, httpMethod, requestURI, signature, arguments);

        return objectToReturn;
    }

    private void logInfo(Timestamp timeStamp, Long executionTime, String httpMethod, String requestURI, Signature signature, String arguments) {
        log.info(timeStamp + " - " + httpMethod + " " + requestURI + " | Method: "  + signature + " | Arguments: " + arguments + " | executed in " + executionTime + " ms");
    }

}
