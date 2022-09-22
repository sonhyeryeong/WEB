package auth.command;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import auth.service.LoginFailException;
import auth.service.LoginService;
import auth.service.User;
import mvc.command.CommandHandler;

public class LoginHandler implements CommandHandler {

	private static final String FORM_VIEW = "/WEB-INF/view/loginForm.jsp";
	private LoginService loginService = new LoginService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) 
	throws Exception {
		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} else if (req.getMethod().equalsIgnoreCase("POST")) {//equalsIgnoreCase: 대소문자를 구분없이 비교함
			return processSubmit(req, res);
		} else {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}

	private String processForm(HttpServletRequest req, HttpServletResponse res) {
		return FORM_VIEW;
	}

	private String processSubmit(HttpServletRequest req, HttpServletResponse res) 
	throws Exception {
		//폼에서 전송한 id,password 파라미터 값을 구한다. 
		String id = trim(req.getParameter("id"));
		String password = trim(req.getParameter("password"));

		//에러 정보를 담을 map객체를 생성한다.
		Map<String, Boolean> errors = new HashMap<>();
		req.setAttribute("errors", errors);
		//id나 password값이 없을 경우 에러를 추가한다. 
		if (id == null || id.isEmpty())
			errors.put("id", Boolean.TRUE);
		if (password == null || password.isEmpty())
			errors.put("password", Boolean.TRUE);
		//에러가 존재할 경우
		if (!errors.isEmpty()) {
			return FORM_VIEW;
		}
		//user객체를 세션의 authUser속성에 저장한다. 
		try {
			User user = loginService.login(id, password); //여기서 LoginFailException에러 발생가능
			req.getSession().setAttribute("authUser", user);//세션범위에서 user저장
			//만약 세션 유지하며 다른사람이 로그인하면 같은 key니깐 새로 로그인한 user로 덮어씀
			res.sendRedirect(req.getContextPath() + "/index.jsp");
			return null;
		} catch (LoginFailException e) {
			errors.put("idOrPwNotMatch", Boolean.TRUE);
			return FORM_VIEW;
		}
	}

	private String trim(String str) {
		return str == null ? null : str.trim();
	}
}