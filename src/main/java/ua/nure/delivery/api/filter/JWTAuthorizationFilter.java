package ua.nure.delivery.api.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import ua.nure.delivery.api.config.security.entity.UserPrincipal;
import ua.nure.delivery.entity.User;
import ua.nure.delivery.entity.enums.Role;
import ua.nure.delivery.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static ua.nure.delivery.util.Constants.*;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private UserService userService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager,
                                  UserService userService) {
        super(authenticationManager);
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        String header = req.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            SecurityContextHolder.getContext().setAuthentication(null);
            chain.doFilter(req, res);
            return;
        } else if (isBlocked(header)) {
            throw new AccessDeniedException("User banned!");
        }

        String refreshToken = refreshToken(header);

        if (refreshToken != null) {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(refreshToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(req, res);
        }
    }

    private boolean isBlocked(String token) {
        String user = JWT.require(Algorithm.HMAC256(SECRET.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .getSubject();
        return getUserPrincipal(user).getRole().equals(Role.BLOCKED);
    }

    private String refreshToken(String token) {
        try {
            JWT.require(Algorithm.HMAC256(SECRET.getBytes()))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""))
                    .getSubject();
            return token;
        } catch (TokenExpiredException ex) {
            UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();

            if (userPrincipal != null) {
                return TOKEN_PREFIX + JWT.create()
                        .withSubject(userPrincipal.getLogin())
                        .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                        .sign(HMAC512(SECRET.getBytes()));
            }
            return null;
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        return Objects.nonNull(token) ? parseToken(token) : null;
    }

    private UsernamePasswordAuthenticationToken parseToken(String token) {
        String user = JWT.require(Algorithm.HMAC256(SECRET.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .getSubject();

        return Objects.nonNull(user)
                ? new UsernamePasswordAuthenticationToken(getUserPrincipal(user), null, getGrantedAuthorities(user))
                : null;
    }

    private List<GrantedAuthority> getGrantedAuthorities(String user) {
        User userEntity = (User) userService.loadUserByUsername(user);
        return AuthorityUtils
                .createAuthorityList(("ROLE_" + userEntity.getRole().getAuthority()).toUpperCase());
    }

    private UserPrincipal getUserPrincipal(String login) {
        User userEntity = (User) userService.loadUserByUsername(login);
        return new UserPrincipal(userEntity);
    }
}
