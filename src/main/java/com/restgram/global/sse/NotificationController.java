package com.restgram.global.sse;

import com.restgram.global.sse.service.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notification")
@Slf4j
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationServiceImpl notificationService;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(Authentication authentication, @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId){
        Long userId = Long.parseLong(authentication.getName());
        return notificationService.subscribe(userId, lastEventId);
    }

}
