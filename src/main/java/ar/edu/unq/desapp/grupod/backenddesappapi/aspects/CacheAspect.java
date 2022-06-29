package ar.edu.unq.desapp.grupod.backenddesappapi.aspects;

import ar.edu.unq.desapp.grupod.backenddesappapi.service.CacheService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CacheAspect {

    @Autowired
    CacheService cacheService;

    @Pointcut("execution(* ar.edu.unq.desapp.grupod.backenddesappapi.service.RateService.getActiveCoinRates(..))")
    public void getActiveCoinRates() { /* Comment added to avoid SonarCloud marking this empty method as a code smell */ }

    @Around("getActiveCoinRates()")
    public Object handle(ProceedingJoinPoint joinPoint) throws Throwable {
        long validityDurationInMinutes = 10L;

        return cacheService.handle(joinPoint.getSignature().toString(), () -> joinPoint.proceed(), validityDurationInMinutes * 60);
    }

}