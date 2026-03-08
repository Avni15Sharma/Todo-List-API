package com.TaskTracker.Todo.List.Service;

import com.TaskTracker.Todo.List.RateLimiting.PricingPlan;
import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class PricingPlanService {
    public ConcurrentHashMap<String, Bucket> cache = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String apiKey){
        return cache.computeIfAbsent(apiKey, this::newBucket);
    }

    public Bucket newBucket(String apiKey){
        PricingPlan plan = PricingPlan.resolvePlanByApiKey(apiKey);
        return Bucket.builder()
                .addLimit(plan.getLimit())
                .build();
    }
}
