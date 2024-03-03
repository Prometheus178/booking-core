package org.booking.core.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.esotericsoftware.minlog.Log;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserDetailsService userDetailService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest httpServletRequest,
                                    @NonNull HttpServletResponse httpServletResponse,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = httpServletRequest.getHeader("authorization");
        if (authHeader == null) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        if (authHeader.startsWith("Bearer ")) {
            var jwtToken = authHeader.substring(7);
            DecodedJWT decodedJWT = JWT.decode(jwtToken);
            var userEmail = decodedJWT.getSubject();
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Date currentTime = new Date();
                Date tokenExpirationTime = decodedJWT.getExpiresAt();
                if (tokenExpirationTime.before(currentTime)) {
                    Log.warn("Token has expired");
                    if (isProlongationAvailable(currentTime, tokenExpirationTime)) {
                        String refreshedToken = jwtService.refreshToken(jwtToken, currentTime);
                        httpServletResponse.setHeader("authorization-fresh-token", refreshedToken);
                        Log.info("Token is refreshed");
                        authenticated(httpServletRequest, userEmail);
                    }
                } else {
                    authenticated(httpServletRequest, userEmail);
                }
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    private void authenticated(HttpServletRequest httpServletRequest, String userEmail) {
        UserDetails userDetails = this.userDetailService.loadUserByUsername(userEmail);
        UsernamePasswordAuthenticationToken authenticationToken = defineAuthToken(httpServletRequest, userDetails);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private UsernamePasswordAuthenticationToken defineAuthToken(HttpServletRequest httpServletRequest, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
        return authenticationToken;
    }

    public boolean isProlongationAvailable(Date currentDate, Date date) {
        long timeDifferenceMillis = currentDate.getTime() - date.getTime();
        return timeDifferenceMillis < (60 * 1000 * JWTService.EXPIRATION_TIME_IN_MINUTES);
    }
}
