package article.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.service.ArticleData;
import article.service.ArticleNotFoundException;
import article.service.ModifyArticleService;
import article.service.ModifyRequest;
import article.service.PermissionDeniedException;
import article.service.ReadArticleService;
import auth.service.User;
import mvc.command.CommandHandler;

public class ModifyArticleHandler implements CommandHandler {
	private static final String FORM_VIEW = "/WEB-INF/view/modifyForm.jsp";

	private ReadArticleService readService = new ReadArticleService();
	private ModifyArticleService modifyService = new ModifyArticleService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} else if (req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req, res);
		} else {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}
	//get방식 
	private String processForm(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		try {
			String noVal = req.getParameter("no");//get방식으로 들어왔기 때문에 url에 파라미터가 있다. 
			int no = Integer.parseInt(noVal);
			ArticleData articleData = readService.getArticle(no, false);//조회수 올리지 않고, 게시물을 읽는다.
			User authUser = (User) req.getSession().getAttribute("authUser");//로그인유저 읽기
			if (!canModify(authUser, articleData)) {//수정 권한이 없을 때는 
				res.sendError(HttpServletResponse.SC_FORBIDDEN);
				return null;
			}
			ModifyRequest modReq = new ModifyRequest(authUser.getId(), no,
					articleData.getArticle().getTitle(),
					articleData.getContent());

			req.setAttribute("modReq", modReq);//읽어온 정보를 보낸다.
			return FORM_VIEW;
		} catch (ArticleNotFoundException e) {//게시글을 찾을 수 없을 때 
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
	}

	private boolean canModify(User authUser, ArticleData articleData) {
		String writerId = articleData.getArticle().getWriter().getId();
		return authUser.getId().equals(writerId);
	}
	//post-> 수정 제출을 눌렀을 때 
	private String processSubmit(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		User authUser = (User) req.getSession().getAttribute("authUser");//현재 로그인한 유저 
		String noVal = req.getParameter("no");//게시글 번호
		int no = Integer.parseInt(noVal);
		//요청 파라미터랑 현재 사용자 정보를 이용해 게시글 수정 
		ModifyRequest modReq = new ModifyRequest(authUser.getId(), no,
				req.getParameter("title"),
				req.getParameter("content"));
		req.setAttribute("modReq", modReq);

		Map<String, Boolean> errors = new HashMap<>();
		req.setAttribute("errors", errors);
		modReq.validate(errors);//수정이 유효한지 검사한다. 
		if (!errors.isEmpty()) {//문제가 있다면 다시 수정폼으로 리턴
			return FORM_VIEW;
		}
		try {//게시글 수정에 문제가 없다면 db를 수정하고
			modifyService.modify(modReq);
			return "/WEB-INF/view/modifySuccess.jsp";//성공 페이지로 보낸다. 
		} catch (ArticleNotFoundException e) {
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		} catch (PermissionDeniedException e) {
			res.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
	}
}

