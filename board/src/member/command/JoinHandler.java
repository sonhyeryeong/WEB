package member.command;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import member.service.DuplicateldException;
import member.service.JoinRequest;
import member.service.JoinService;
import mvc.command.CommandHandler;

public class JoinHandler implements CommandHandler {

	private static final String FORM_VIEW = "/WEB-INF/view/joinForm.jsp";
	private JoinService joinService = new JoinService();
	
	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) {//요청 제어를 하고 있는 메소드 . 
		//어떤 방식으로 요청 했는지 알아낸다. 
		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} else if (req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req, res);
		} else {//구현하지 않은 방식으로  하면 405번으로 응답한다. 
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}
	//get방식으로 보내면 폼을 다시 보여준다. 
	private String processForm(HttpServletRequest req, HttpServletResponse res) {
		return FORM_VIEW; //다른 주소로 보낸다. 
	}

	private String processSubmit(HttpServletRequest req, HttpServletResponse res) {
		JoinRequest joinReq = new JoinRequest();
		joinReq.setId(req.getParameter("id"));
		joinReq.setName(req.getParameter("name"));
		joinReq.setPassword(req.getParameter("password"));
		joinReq.setConfirmPassword(req.getParameter("confirmPassword"));
		
		Map<String, Boolean> errors = new HashMap<>();
		req.setAttribute("errors", errors);
		
		joinReq.validate(errors);
		
		if (!errors.isEmpty()) {//문제가 있으면 다시 입력폼으로 리턴시킴
			return FORM_VIEW;
		}
		
		try {
			joinService.join(joinReq);
			return "/WEB-INF/view/joinSuccess.jsp";
		} catch (DuplicateldException e) {
			errors.put("duplicateId", Boolean.TRUE);
			return FORM_VIEW;
		}
	}

}