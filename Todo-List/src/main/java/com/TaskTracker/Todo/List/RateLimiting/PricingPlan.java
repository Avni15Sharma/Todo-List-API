package com.TaskTracker.Todo.List.RateLimiting;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;

import java.time.Duration;

public enum PricingPlan {
    FREE(2),
    BASIC(4),
    PRO(10);
    private final int capacity;
    PricingPlan(int capacity){
        this.capacity = capacity;
    }
    public Bandwidth getLimit(){
        return Bandwidth.classic(this.capacity,Refill.intervally(this.capacity,Duration.ofMinutes(1)));
    }
    public static PricingPlan resolvePlanByApiKey(String apiKey){
        return switch (apiKey) {
            case "PX001-" -> PricingPlan.PRO;
            case "BX001-" -> PricingPlan.BASIC;
            default -> PricingPlan.FREE;
        };
    }
}
