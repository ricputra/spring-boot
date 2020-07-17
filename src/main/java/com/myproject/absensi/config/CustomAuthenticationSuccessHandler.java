package com.myproject.absensi.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.myproject.absensi.service.UserService;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	private UserService userService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		String username = authentication.getName();
		
		System.out.println("masuk SINI DONG " +username);
		UserDetails theUser = userService.loadUserByUsername(username);
		
		// now place in the session
		HttpSession session = request.getSession();
		session.setAttribute("user", theUser);
		session.setMaxInactiveInterval(30);
		response.sendRedirect(request.getContextPath() + "/");
	}

}
