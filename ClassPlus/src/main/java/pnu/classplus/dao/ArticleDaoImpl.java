package pnu.classplus.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import pnu.classplus.vo.Article;

@Component
public class ArticleDaoImpl implements ArticleDao {
	public List<Article> getList() {
		Article article1 = new Article(1, "2019-08-20 12:12:12", "? œëª?1", "?‚´?š©1");
		Article article2 = new Article(2, "2019-08-20 12:12:13", "? œëª?2", "?‚´?š©2");
		Article article3 = new Article(3, "2019-08-20 12:12:14", "? œëª?3", "?‚´?š©3");
		
		List<Article> list = new ArrayList<Article>();
		
		list.add(article1);
		list.add(article2);
		list.add(article3);
		
		return list;
	}
}
