package pnu.classplus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags="MainController Tag")
@RestController
public class MainController {
	@ApiOperation(value="���� ������", notes="��Ʈ")
	@RequestMapping("/home/main")
	@ResponseBody
	public String showMain() {
		return "Main";
	}
}