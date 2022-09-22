package member.service;

import java.util.Map;

public class JoinRequest {
	private String id;
	private String name;
	private String password;
	private String confirmPassword;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	//비밀번호 확인이랑 비밀번호랑 같은지 확인한다. 
	public boolean isPasswordEqualToConfirm() {
		return password != null && password.equals(confirmPassword);
	}
	//유효성 검사 ->  에러 정보 담긴 맵 체크
	public void validate(Map<String, Boolean>errors) {
		//id필드값이 올바르지 않으면 errors맵에 <"id", true>로 추가해 두는 과정 by checkEmpty메소드
		checkEmpty(errors,id,"id");
		checkEmpty(errors,name,"name");
		checkEmpty(errors,password,"password");
		checkEmpty(errors,confirmPassword,"confirmPassword");
		
		if(!errors.containsKey("confirmPassword")) {
			if(!isPasswordEqualToConfirm()) {
				errors.put("notMatch",Boolean.TRUE); //암호와 확인값의 불일치 -> notMatch 에러키 추가
			}
		}
	}
	//value값(필드값)이 없는 경우, errors맵에 <"fieldName", true>로 집어넣음
	private void checkEmpty(Map<String, Boolean> errors, String value, String fieldName) {
		if(value==null || value.isEmpty()) {
			errors.put(fieldName, Boolean.TRUE);
		}
	}
	
	
	
}
