package com.nazjara.controller;

import com.nazjara.model.Token;
import com.nazjara.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
@RequiredArgsConstructor
public class UserDetailsController {

//    private final OAuth2AuthorizedClientService authorizedClientService;
    private final WebClient webClient;

    @Value("${api-gateway.base-url}")
    private String baseUrl;

    @GetMapping("/user-details")
    public String getUserDetails(Model model, @AuthenticationPrincipal OidcUser principal) {
//       An example of how to extract token from the Authentication object if there's a need to include it
//       into the request manually (like when using RestTemplate)

//       var auth = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
//       var authClient = authorizedClientService
//               .loadAuthorizedClient(auth.getAuthorizedClientRegistrationId(), auth.getName());
//       var token = authClient.getAccessToken().getTokenValue();

        var user = webClient.get().uri(String.format("%s/user/{id}", baseUrl), principal.getIdToken().getClaimAsString("sub"))
                .retrieve()
                .bodyToMono(User.class)
                .block();

        var token = webClient.get().uri(String.format("%s/token", baseUrl))
                .retrieve()
                .bodyToMono(Token.class)
                .block();

        model.addAttribute("token", token);
        model.addAttribute("user", user);

        return "userDetails";
    }
}
