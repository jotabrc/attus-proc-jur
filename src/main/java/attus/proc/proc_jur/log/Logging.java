package attus.proc.proc_jur.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class Logging {

    @Before("execution(* attus.proc.proc_jur.service.*.*(..))")
    public void log(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getName();
        log.info("Executing {}", className);
    }
}
