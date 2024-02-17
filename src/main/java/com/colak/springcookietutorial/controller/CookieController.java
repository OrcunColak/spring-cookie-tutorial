package com.colak.springcookietutorial.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/cookie")
public class CookieController {

    private static final String COOKIE_NAME = "user-id";
    private static final String COOKIE_VALUE = "cookieValue";

    // http://localhost:8080/api/v1/cookie/create
    @GetMapping(path = "create")
    public void createCookie(HttpServletResponse response) {
        // If no expiration time is specified for a cookie, it lasts as long as the session is not expired.
        // Such cookies are called session cookies.
        // Session cookies remain active until the user closes their browser or clears their cookies.
        // Set cookie expiry for 30 minutes
        long cookieExpiry = 1800;

        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, COOKIE_VALUE)
                // HttpOnly cookies are used to prevent cross-site scripting (XSS) attacks and are not accessible via JavaScript's Document.cookie API.
                // When the HttpOnly flag is set for a cookie, it tells the browser that this particular cookie should only be accessed by the server.
                .httpOnly(true)
                // A secure cookie is the one which is only sent to the server over an encrypted HTTPS connection.
                // Secure cookies cannot be transmitted to the server over unencrypted HTTP connections.
                .secure(false)
                // Global cookie accessible everywhere
                .path("/")
                .maxAge(cookieExpiry)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    // http://localhost:8080/api/v1/cookie/read
    @GetMapping("read")
    public String readCookie(@CookieValue(name = COOKIE_NAME, defaultValue = "default-user-id") String userId) {
        return "Cookie value is " + userId;
    }

    // http://localhost:8080/api/v1/cookie/delete
    @DeleteMapping(path = "delete")
    public void deleteCookie(HttpServletResponse response) {
        // https://dzone.com/articles/how-to-use-cookies-in-spring-boot
        // To delete a cookie, set the Max-Age directive to 0 and unset its value.
        // You must also pass the same other cookie properties you used to set it. Don't set the Max-Age directive value to -1.
        // Otherwise, it will be treated as a session cookie by the browser.
        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, COOKIE_VALUE)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    }

}
