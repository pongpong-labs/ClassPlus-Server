package pnu.classplus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import pnu.classplus.vo.Article;
import pnu.classplus.service.TestService;

@Api(tags="TestController Tag")
@Controller
public class TestController {
	@Autowired
	TestService articleService;
	@ApiOperation(value="테스트 페이지")
	@ApiImplicitParams({
			@ApiImplicitParam(name="param1", value="패러미터1", dataType="int", paramType="query"),
			@ApiImplicitParam(name="param2", value="패러미터2", dataType="String", paramType="query")
	})
	@RequestMapping("/article/list")
	@ResponseBody
	public List showList(Model aModel) {
		List<Article> list = articleService.getList();

		//aModel.addAttribute("list", list);

		return list;
	}
}