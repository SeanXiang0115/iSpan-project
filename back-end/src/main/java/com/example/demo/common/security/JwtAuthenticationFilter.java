package com.example.demo.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import jakarta.servlet.http.Cookie;
import org.springframework.web.util.WebUtils;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                String email = tokenProvider.getEmailFromToken(jwt);

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        // 為了相容行動裝置或其他系統，依然允許從 Authorization 標頭讀取
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        String path = request.getRequestURI();

        // 優先檢查 URL 判斷
        if (path.startsWith("/api/admins")) {
            Cookie adminCookie = WebUtils.getCookie(request, "adminAccessToken");
            if (adminCookie != null && StringUtils.hasText(adminCookie.getValue())) {
                return adminCookie.getValue();
            }
        }

        // 以免同一個 API (如 /api/users) 給兩邊共用導致判斷錯誤，
        // 嘗試看 Referer 來自哪裡 (若來自設定後台，就優先使用 adminCookie)
        String referer = request.getHeader("Referer");
        if (referer != null && referer.contains("/admin")) {
            Cookie adminCookie = WebUtils.getCookie(request, "adminAccessToken");
            if (adminCookie != null && StringUtils.hasText(adminCookie.getValue())) {
                return adminCookie.getValue();
            }
        }

        // 預設尋找使用者的 accessToken
        Cookie userCookie = WebUtils.getCookie(request, "accessToken");
        if (userCookie != null && StringUtils.hasText(userCookie.getValue())) {
            return userCookie.getValue();
        }

        // 如果連 userCookie 都沒有，最後再 fallback 去找 adminCookie (雙重保險)
        Cookie adminCookie = WebUtils.getCookie(request, "adminAccessToken");
        if (adminCookie != null && StringUtils.hasText(adminCookie.getValue())) {
            return adminCookie.getValue();
        }

        return null;
    }
}
