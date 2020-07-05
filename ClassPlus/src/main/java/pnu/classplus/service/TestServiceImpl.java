package pnu.classplus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pnu.classplus.dao.ArticleDao;
import pnu.classplus.vo.Article;

@Service
public class TestServiceImpl implements TestService {
	
	@Autowired
	ArticleDao articleDao;
	
	public List<Article> getList() {	
		return articleDao.getList();
	}
}
