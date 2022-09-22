package article.service;

import java.sql.Connection;
import java.sql.SQLException;

import article.dao.ArticleContentDao;
import article.dao.ArticleDao;
import article.model.Article;
import article.model.ArticleContent;
import jdbc.connection.ConnectionProvider;

public class ReadArticleService {

	private ArticleDao articleDao = new ArticleDao();
	private ArticleContentDao contentDao = new ArticleContentDao();
	
	public ArticleData getArticle(int articleNum, boolean increaseReadCount) {
		try (Connection conn = ConnectionProvider.getConnection()){
			Article article = articleDao.selectById(conn, articleNum);//게시글 번호로 article객체 생성 
			if (article == null) {//게시글이 존재하지 않을 때
				throw new ArticleNotFoundException();
			}
			ArticleContent content = contentDao.selectById(conn, articleNum);//게시글 번호로 article_content객체 생성 
			if (content == null) {//내용이 존재하지 않을 때 
				throw new ArticleContentNotFoundException();
			}
			if (increaseReadCount) {//읽었다면(true라면) 
				articleDao.increaseReadCount(conn, articleNum);//조회수 증가
			}
			return new ArticleData(article, content);//artice(내용,게시글 객체 전부 합친거)
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}