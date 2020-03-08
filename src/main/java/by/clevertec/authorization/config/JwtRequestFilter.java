package by.clevertec.authorization.config;

import lombok.extern.slf4j.Slf4j;
import by.clevertec.authorization.repository.UserRepository;
import by.clevertec.authorization.model.UserApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private final UserRepository userRepository;

    @Autowired
    public JwtRequestFilter(JwtTokenUtil jwtTokenUtil, UserRepository userRepository) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            String header = request.getHeader("Authorization");
            String username = null;
            String jwtToken = null;
            if (header != null) {
                jwtToken = header.substring(9);
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);

                UserApp userApp = userRepository.findById(username).orElseThrow(IllegalArgumentException::new);
                String tokenEncInDb = userApp.getToken();

                if (jwtToken.equals(tokenEncInDb)){
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            username, userApp.getPassword(), null);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }
        chain.doFilter(request, response);
    }
}
