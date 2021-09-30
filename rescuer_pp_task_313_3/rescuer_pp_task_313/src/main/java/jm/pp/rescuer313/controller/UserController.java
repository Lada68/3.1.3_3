package jm.pp.rescuer313.controller;

import jm.pp.rescuer313.dto.RoleDto;
import jm.pp.rescuer313.dto.UserDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
@RequestMapping
public class UserController {
    @GetMapping("/")
    public String getIndex(HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return response.getRawStatusCode() != 401 && super.hasError(response);
            }
        });

        HttpHeaders httpHeaders = new HttpHeaders();
        Optional.ofNullable(request.getCookies()).map(Arrays::stream)
                .map(cookies -> cookies.map(cookie -> cookie.getName() + "=" + cookie.getValue())
                        .collect(Collectors.joining("; ")))
                .ifPresent(sCookie -> httpHeaders.add(HttpHeaders.COOKIE, sCookie));

        ResponseEntity<UserDto> user = restTemplate.exchange("http://localhost:8080/api/",
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                UserDto.class);

        return Optional.ofNullable(user.getBody())
                .filter(dto -> user.getStatusCodeValue() != 401)
                .map(UserDto::getRoles)
                .map(Collection::stream)
                .map(roleDtoStream -> roleDtoStream.map(RoleDto::getName)
                        .anyMatch("ROLE_ADMIN"::equals) ? "pages/admin" : "pages/user")
                .orElse("login");
    }
}
