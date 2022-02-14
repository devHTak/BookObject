package com.review.refactor.chap10.bird.after;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BirdService {

    public Map<Bird, String> plumages(List<Bird> birds) {
        Map<Bird, String> result = new HashMap<>();
        birds.stream().map(bird -> BirdFactory.createBird(bird))
                .forEach(bird -> result.put(bird, bird.pulmage()));

        return result;
    }

    public Map<Bird, Integer> speeds(List<Bird> birds) {
        Map<Bird, Integer> result = new HashMap<>();
        birds.stream().map(bird -> BirdFactory.createBird(bird))
                .forEach(bird -> result.put(bird, bird.airSpeedVelocity()));

        return result;
    }
}
