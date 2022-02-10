package ch.fincons.library.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class AuthEntryPoint extends BasicAuthenticationEntryPoint {
    private static String REALM = "REAME";

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException
    {
        String ErrMsg = "Userid e/o Password non corrette!";

        log.warn("Errore Sicurezza: " + authException.getMessage());

        // Authentication failed, send error response.
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");

        PrintWriter writer = response.getWriter();
        writer.println(ErrMsg);
    }

    @Override
    public void afterPropertiesSet()
    {
        setRealmName(REALM);
        super.afterPropertiesSet();
    }
}
