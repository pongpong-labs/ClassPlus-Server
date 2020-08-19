package pnu.classplus.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pnu.classplus.dto.MessageDto;
import pnu.classplus.service.MessageService;

import javax.servlet.http.HttpServletRequest;

@Tag(name="메세지 관련 기능")
@RestController
@RequestMapping("/message/*")
public class MessageController {
    @Autowired
    MessageService service;

    @PostMapping("/post")
    public ResponseEntity postMessage(HttpServletRequest request, @RequestBody MessageDto messageDto) {
        return service.postMessage(request, messageDto);
    }

    @GetMapping("/get")
    public ResponseEntity getMessage(HttpServletRequest request) {
        return service.getMessage(request);
    }
}
