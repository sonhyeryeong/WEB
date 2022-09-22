package article.service;

import java.util.Map;

import article.model.Writer;
//게시글 쓰기에 필요한 데이터를 제공하는 클래스 
public class WriteRequest{
	private Writer writer;
	private String title;
	private String content;
	public WriteRequest(Writer writer, String title, String content) {
		super();
		this.writer = writer;
		this.title = title;
		this.content = content;
	}
	public Writer getWriter() {
		return writer;
	}
	public String getTitle() {
		return title;
	}
	public String getContent() {
		return content;
	}
	//데이터의 유효 여부를 검사한다. 잘못된 데이터가 존재하면 errors 객체에 관련 코드를 추가한다. 
	public void validate(Map<String, Boolean> errors) {
		if(title==null || title.trim().isEmpty()) {
			errors.put("title",Boolean.TRUE);
		}
	}
}



