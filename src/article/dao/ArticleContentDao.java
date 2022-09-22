package article.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import article.model.ArticleContent;
import jdbc.JdbcUtil;
//데이터 삽입에 성공하면 articlecontent 객체를 리턴하고, 실패 시 null을 리턴한다. 
public class ArticleContentDao {

	public ArticleContent insert(Connection conn, ArticleContent content) 
	throws SQLException {
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(
					"insert into article_content " + 
					"(article_no, content) values (?,?)");
			pstmt.setLong(1, content.getNumber());
			pstmt.setString(2, content.getContent());
			int insertedCount = pstmt.executeUpdate();
			if (insertedCount > 0) {
				return content;
			} else {
				return null;
			}
		} finally {
			JdbcUtil.closeStmt(pstmt);
		}
	}
	//게시글 id를 통해 article_content를 읽어오는거 
	public ArticleContent selectById(Connection conn, int no) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(
					"select * from article_content where article_no = ?");
			pstmt.setInt(1, no);
			rs = pstmt.executeQuery();
			ArticleContent content = null;
			if (rs.next()) {
				content = new ArticleContent(
						rs.getInt("article_no"), rs.getString("content"));
			}
			return content;
		} finally {
			JdbcUtil.closeRS(rs);
			JdbcUtil.closeStmt(pstmt);
		}
	}
	//파라미터로 전달받은 게시글 번호를 사용해서 글의 내용을 수정한다.
	public int update(Connection conn, int no, String content) throws SQLException {
		try (PreparedStatement pstmt = 
				conn.prepareStatement(
						"update article_content set content = ? "+
						"where article_no = ?")) {
			pstmt.setString(1, content);
			pstmt.setInt(2, no);
			return pstmt.executeUpdate();
		}
	}
	
	public int deletecontent(Connection conn, int no, String title) throws SQLException {
		try (PreparedStatement pstmt = conn.prepareStatement(
				"DELETE FROM article_content WHERE article_no = ?")){
			pstmt.setInt(1, no);
			return pstmt.executeUpdate();
		}
	}
}