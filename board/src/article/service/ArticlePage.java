package article.service;

import java.util.List;

import article.model.Article;

public class ArticlePage {

	private int total;//전체 게시글 개수
	private int currentPage;//사용자가 요청한 페이지 번호
	private List<Article> content;//게시글 목록을 보관
	private int totalPages;//전체 페이지 개수
	private int startPage;//페이지 이동 링크 시작 
	private int endPage;//페이지 이동 링크 끝

	public ArticlePage(int total, int currentPage, int size, List<Article> content) {
		this.total = total;
		this.currentPage = currentPage;
		this.content = content;
		if (total == 0) {//게시글 갯수가 0일때 
			totalPages = 0;
			startPage = 0;
			endPage = 0;
		} else {
			totalPages = total / size;//전체 페이지 수를 계산
			if (total % size > 0) {//한 페이지 하고 남는 게시글이 있으면 페이지 하나 더 추가해야함. 
				totalPages++;
			}
			//화면 하단에 보여줄 페이지 이동 링크의 시작, 끝 -> 현재 8이면 [6 7,8 9,10] 꼴
			int modVal = currentPage % 5;
			startPage = currentPage / 5 * 5 + 1;
			if (modVal == 0) startPage -= 5;
			
			endPage = startPage + 4;
			if (endPage > totalPages) endPage = totalPages;
		}
	}

	public int getTotal() {
		return total;
	}

	public boolean hasNoArticles() {
		return total == 0;
	}

	public boolean hasArticles() {
		return total > 0;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public List<Article> getContent() {
		return content;
	}

	public int getStartPage() {
		return startPage;
	}
	
	public int getEndPage() {
		return endPage;
	}
}