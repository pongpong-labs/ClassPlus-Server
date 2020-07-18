package pnu.classplus.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags="MainController Tag")
@RestController
public class MainController {

	@ApiOperation(value="루트", notes="노트")
	@RequestMapping("/")
	@ResponseBody
	public String showRoot() {
		return "Hello World!";
	}


	@ApiOperation(value="메인 페이지", notes="노트")
	@RequestMapping("/home/main")
	@ResponseBody
	public String showMain() {
		return "Main";
	}
}