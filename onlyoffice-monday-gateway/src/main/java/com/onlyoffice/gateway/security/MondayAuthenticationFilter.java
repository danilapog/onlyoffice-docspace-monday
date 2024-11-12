package com.onlyoffice.gateway.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class MondayAuthenticationFilter extends OncePerRequestFilter {
  private static final String SESSION_TOKEN_PARAM = "sessionToken";
  private static final String HX_CURRENT_URL = "HX-Current-URL";
  private final ObjectMapper mapper = new ObjectMapper();

  private final JwtDecoder decoder;
  private final AuthenticationManager authenticationManager;

  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    var token = request.getParameter(SESSION_TOKEN_PARAM);
    var tokenHeader = request.getHeader(HX_CURRENT_URL);
    if (tokenHeader == null || tokenHeader.isBlank())
      tokenHeader = request.getHeader(HttpHeaders.REFERER);

    if (token == null || token.isBlank()) {
      var params =
          UriComponentsBuilder.fromUri(URI.create(tokenHeader))
              .build()
              .getQueryParams()
              .get("sessionToken");
      if (params != null && !params.isEmpty()) token = params.getFirst();
    }

    if (token == null || token.isBlank()) {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      return;
    }

    try {
      var decodedToken = decoder.decode(token);
      if (decodedToken.getClaim("dat") instanceof Map<?, ?> data) {
        var principal = mapper.convertValue(data, MondayAuthenticationPrincipal.class);
        var authenticated =
            authenticationManager.authenticate(new MondayAuthentication(principal, token));
        SecurityContextHolder.getContext().setAuthentication(authenticated);
        chain.doFilter(request, response);
        return;
      }

      throw new JwtException("Could not decode Monday session token");
    } catch (JwtException e) {
      response.setStatus(HttpStatus.FORBIDDEN.value());
    }
  }

  protected boolean shouldNotFilter(HttpServletRequest request) {
    var path = request.getRequestURI();
    return path.startsWith("/actuator")
        || path.contains("main.css")
        || path.contains("webSocket.js");
  }
}
