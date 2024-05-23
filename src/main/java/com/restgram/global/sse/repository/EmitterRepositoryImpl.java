package com.restgram.global.sse.repository;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@NoArgsConstructor
public class EmitterRepositoryImpl implements EmitterRepository {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    // map 에 emitter 추가
    @Override
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId, sseEmitter);
        return sseEmitter;
    }

    @Override
    public void saveEventCache(String eventCacheId, Object event) {
        eventCache.put(eventCacheId, event);
    }

    @Override
    public Map<String, SseEmitter> findAllEmitterStartWithByUserId(Long userId) {
        return  emitters.entrySet().stream() //여러개의 Emitter가 존재할 수 있기때문에 stream 사용
                .filter(entry -> entry.getKey().startsWith(userId+"_"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<String, SseEmitter> findAllEmitterStartWithByUserIdInList(List userId) {
        return null;
    }

    @Override
    public Map<String, Object> findAllEventCacheStartWithByUserId(Long userId) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userId+"_"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void deleteById(String emitterId) {
        emitters.remove(emitterId);
    }

    @Override
    public void deleteAllEmitterStartWithId(Long userId) {
        emitters.forEach((key, emitter) -> {
            if (key.startsWith(userId+"_")) emitters.remove(key);
        });
    }

    @Override
    public void deleteAllEventCacheStartWithId(Long userId) {
        eventCache.forEach((key, emitter) -> {
            if (key.startsWith(userId+"_")) eventCache.remove(key);
        });
    }


}
