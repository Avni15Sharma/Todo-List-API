package com.TaskTracker.Todo.List.RateLimiting;

import com.TaskTracker.Todo.List.Service.PricingPlanService;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {
    private final PricingPlanService pricingPlanService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String apiKey = request.getHeader("X-Api-Key");
        if(apiKey == null || apiKey.isEmpty()){
//            response.sendError(HttpStatus.BAD_REQUEST.value(),"Missing Header: X-Api-Key");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("application/json");
            response.getWriter().write("""
            {
                "status": 400,
                "error": "Bad Request: API Key is missing"
            }
            """);
            return false;
        }
        Bucket bucket = pricingPlanService.resolveBucket(apiKey);
        ConsumptionProbe consumptionProbe = bucket.tryConsumeAndReturnRemaining(1);
        if(consumptionProbe.isConsumed()){
            response.addHeader("X-Rate-Limit-Remaining",Long.toString(consumptionProbe.getRemainingTokens()));
            return true;
        }else{
            response.addHeader("X-Rate-Limit-Retry-After-Seconds",Long.toString(consumptionProbe.getNanosToWaitForRefill()/1000000000));
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(),"You have exhausted your API request quota");
            return false;
        }
    }
}
