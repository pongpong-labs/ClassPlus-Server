package pnu.classplus.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    @Parameters({
        @Parameter(name="receiver_mem_idx", description="수신자 멤버 코드", in= ParameterIn.QUERY, schema=@Schema(type="integer"), required=true),
        @Parameter(name="message", description="전송할 메시지 (최대 5000바이트)", in=ParameterIn.QUERY, schema=@Schema(type="string"), required=true)
    })
    @Operation(summary="메시지 전송")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Success", content={
            @Content(mediaType= MediaType.APPLICATION_JSON_VALUE, examples=@ExampleObject(value="{'resultCode' : '0', 'resultMessage' : 'Message Send Successful'}"))
        }),
        @ApiResponse(responseCode="400", description="Error (Bad Request)", content={
            @Content(mediaType=MediaType.APPLICATION_JSON_VALUE,
                examples=@ExampleObject(
                    value="{'resultCode' : '33', 'resultMessage' : 'invalid sender (session invalid)'}\n" +
                        "{'resultCode' : '40', 'resultMessage' : 'invalid receiver'}\n" +
                        "{'resultCode' : '41', 'resultMessage' : 'sender equals to receiver'}"))
        })
    })
    @PostMapping("/post")
    public ResponseEntity postMessage(HttpServletRequest request, @RequestBody MessageDto messageDto) {
        return service.postMessage(request, messageDto);
    }

    @Operation(summary="수신 메시지")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Success", content={
            @Content(mediaType= MediaType.APPLICATION_JSON_VALUE,
                examples=@ExampleObject(value="{\n 'resultCode' : '0',\n 'resultMessage' : 'Message get Successful'\n" +
                " 'data' : [\n" +
                "  {\n" +
                "   'created' : '2020-08-19T16:30:57.86'\n" +
                "   'idx' : 1\n" +
                "   'sender' : {\n" +
                "    'idx' : 2,\n" +
                "    'uid' : 'st_depark',\n" +
                "    'role' : 'ROLE_STUDENT',\n" +
                "    'name' : '박대언'\n" +
                "    'email' : 'depark@pusan.ac.kr'\n" +
                "    'department' : {\n" +
                "     'idx' : 22,\n" +
                "     'name' : '정보컴퓨터공학부'\n" +
                "    }\n" +
                "   }\n" +
                "   'receiver' : {\n" +
                "    'idx' : 11,\n" +
                "    'uid' : 'pf_minsu',\n" +
                "    'role' : 'ROLE_PROFESSOR',\n" +
                "    'name' : '김민수'\n" +
                "    'email' : 'abcd@pusan.ac.kr'\n" +
                "    'department' : {\n" +
                "     'idx' : 22,\n" +
                "     'name' : '정보컴퓨터공학부'\n" +
                "    }\n" +
                "   }\n" +
                "   'message' : '안녕하세요. 쪽지 발송 테스트입니다.'\n" +
                "  },\n" +
                "  {\n" +
                "   'created' : '2020-08-19T16:30:57.86'\n" +
                "   'idx' : 1\n" +
                "   'sender' : {\n" +
                "    'idx' : 2,\n" +
                "    'uid' : 'st_depark',\n" +
                "    'role' : 'ROLE_STUDENT',\n" +
                "    'name' : '박대언'\n" +
                "    'email' : 'depark@pusan.ac.kr'\n" +
                "    'department' : {\n" +
                "     'idx' : 22,\n" +
                "     'name' : '정보컴퓨터공학부'\n" +
                "    }\n" +
                "   }\n" +
                "   'receiver' : {\n" +
                "    'idx' : 11,\n" +
                "    'uid' : 'pf_minsu',\n" +
                "    'role' : 'ROLE_PROFESSOR',\n" +
                "    'name' : '김민수'\n" +
                "    'email' : 'abcd@pusan.ac.kr'\n" +
                "    'department' : {\n" +
                "     'idx' : 22,\n" +
                "     'name' : '정보컴퓨터공학부'\n" +
                "    }\n" +
                "   }\n" +
                "   'message' : '안녕하세요. 쪽지 발송 테스트입니다.'\n" +
                "  },\n" +
                " ]\n" +
                "}\n"))
            }),
        @ApiResponse(responseCode="400", description="Error (Bad Request)", content={
            @Content(mediaType=MediaType.APPLICATION_JSON_VALUE,
                examples=@ExampleObject(
                    value="{'resultCode' : '33', 'resultMessage' : 'invalid sender (session invalid)'}\n" +
                        "{'resultCode' : '42', 'resultMessage' : 'No message'}"))
        })
    })
    @GetMapping("/get")
    public ResponseEntity getMessage(HttpServletRequest request) {
        return service.getMessage(request);
    }
}
