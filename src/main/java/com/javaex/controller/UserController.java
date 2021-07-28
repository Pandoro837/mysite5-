package com.javaex.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.javaex.service.UserService;
import com.javaex.vo.UserVo;

@Controller
@RequestMapping(value="/user", method = {RequestMethod.GET, RequestMethod.POST})
public class UserController {

	private String line = "---------------------------------------";

	@Autowired
	private UserService userService;
	
	//로그인 화면 이동
	@RequestMapping(value="/loginForm", method = {RequestMethod.GET, RequestMethod.POST} )
	public String loginForm() {
		
		System.out.println(line + "[UserController - loginForm" + line);
		
		return "/user/loginForm";
	}
	
	//로그인
	@RequestMapping(value="/login", method = {RequestMethod.GET, RequestMethod.POST} )
	public String login(@ModelAttribute UserVo userVo, HttpSession session) {
		
		System.out.println(line + "[UserController - login" + line);
		
		UserVo authUser = userService.getUser(userVo);
		
		//로그인에 성공하면(authUser != null)
		if(authUser != null) {
			System.out.println("[로그인 성공]");
			session.setAttribute("authUser", authUser);
			return "redirect:/main";
		} else {
			System.out.println("[로그인 실패]");
			return "redirect:/user/loginForm?result=fail";
		}
	}
	
	//로그아웃
	@RequestMapping(value="/logout", method = {RequestMethod.GET ,RequestMethod.POST})
	public String logout(HttpSession session) {
		
		session.removeAttribute("authUser");
		session.invalidate();
		
		return "redirect:/main";
	}
	
	//회원가입 화면
	@RequestMapping(value="/joinForm", method = {RequestMethod.GET, RequestMethod.POST})
	public String joinForm() {
		
		System.out.println(line+"joinForm"+line);
		
		return "/user/joinForm";
	}
	
	//회원가입
	@RequestMapping(value="/join", method = {RequestMethod.GET, RequestMethod.POST})
	public String join(@ModelAttribute UserVo userJoin) {
		
		System.out.println(line+"join"+line);
		
		System.out.println(userJoin.toString());
		
		userService.join(userJoin);
		
		return "/user/joinOk";
	}
	
	//회원 정보 수정
	@RequestMapping(value="/modifyForm", method = {RequestMethod.GET, RequestMethod.POST})
	public String modifyForm(HttpSession session, Model model) {
		
		int userNo = ((UserVo)session.getAttribute("authUser")).getNo();
		
		UserVo userModify = userService.getModify(userNo);
		
		model.addAttribute("userModify", userModify);
		
		return "/user/modifyForm";
	}
	
	//정보 수정
	@RequestMapping(value="/modify", method = {RequestMethod.GET, RequestMethod.POST})
	public String modify(HttpSession session ,@ModelAttribute UserVo userModify) {
		
		int no = ((UserVo)session.getAttribute("authUser")).getNo();

		userModify.setNo(no); 
		
		userService.modify(userModify);
		
		((UserVo)session.getAttribute("authUser")).setName(userModify.getName());
		
		return "redirect:/main";
		
	}
	
}
