package member.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;
import member.model.Member;
import member.model.MemberDao;

public class JoinService {//다오에서 만든 하나하나 기능을 이용해 응용로직을 구성하고, 트랜잭션도 걸수있다 (connection이 여기에 있으니깐 dao가 아니라)

	private MemberDao memberDao = new MemberDao();
	
	public void join(JoinRequest joinReq) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			
			Member member = memberDao.selectById(conn, joinReq.getId());
			if (member != null) {//가입하려는 id가 존재한다면, 
				JdbcUtil.rollback(conn);//트랜잭션을 롤백한다. 
				throw new DuplicateldException();//예외를 발생시킨다. 
			}
			
			memberDao.insert(conn, 
					new Member(
							joinReq.getId(), 
							joinReq.getName(), 
							joinReq.getPassword(), 
							new Date())
					);
			conn.commit();//트랜잭션 커밋
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.closeConn(conn);
		}
	}
}