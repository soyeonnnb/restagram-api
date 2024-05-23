package com.restgram.global.sse.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

public interface EmitterRepository {
    SseEmitter save(String emitterId, SseEmitter sseEmitter); // Emitter 저장
    void saveEventCache(String eventCacheId, Object event); //이벤트 저장

    Map<String, SseEmitter> findAllEmitterStartWithByUserId(Long userId); //해당 회원과  관련된 모든 Emitter를 찾는다

    Map<String, SseEmitter> findAllEmitterStartWithByUserIdInList(List userId); //List 에서 해당 회원과  관련된 모든 Emitter를 찾는다(미 개발)

    Map<String, Object> findAllEventCacheStartWithByUserId(Long userId); //해당 회원과관련된 모든 이벤트를 찾는다

    void deleteById(String emitterId); //Emitter를 지운다

    void deleteAllEmitterStartWithId(Long userId); //해당 회원과 관련된 모든 Emitter를 지운다

    void deleteAllEventCacheStartWithId(Long userId); //해당 회원과 관련된 모든 이벤트를 지운다
}
