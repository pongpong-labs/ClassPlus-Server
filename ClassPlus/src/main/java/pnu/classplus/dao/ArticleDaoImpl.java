package pnu.classplus.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import pnu.classplus.vo.Article;

@Component
public class ArticleDaoImpl implements ArticleDao {
	public List<Article> getList() {
		Article article1 = new Article(1, "2019-08-20 12:12:12", "제목제목1", "tetxt");
		Article article2 = new Article(2, "2019-08-20 12:12:12", "제목제목11", "tetxt");
		Article article3 = new Article(3, "2019-08-20 12:12:12", "제목제목11", "tetxt");


		List<Article> list = new ArrayList<Article>();

		list.add(article1);
		list.add(article2);
		list.add(article3);

		return list;
	}
}