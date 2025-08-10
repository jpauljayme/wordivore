package dev.jp.emancipate_the_self.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MySimpleUrlAuthenticationSuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession httpSession  = request.getSession();

        if(httpSession == null){
            return;
        }

        httpSession.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    private void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()){
            log.debug("Response already committed. Cannot redirect to {}.", targetUrl);
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    private String determineTargetUrl(Authentication authentication) {

        Map<String, String> roleToTargetUrl = new HashMap<>();
        roleToTargetUrl.put("ROLE_USER", "/user");
        roleToTargetUrl.put("ROLE_ADMIN", "/admin");

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        log.info(authorities.toString());
        for (final GrantedAuthority grantedAuthority : authorities){
            String authorityName= grantedAuthority.getAuthority();
            if(roleToTargetUrl.containsKey(authorityName)){
                return roleToTargetUrl.get(authorityName);
            }
        }

        throw new IllegalStateException();
    }
}
