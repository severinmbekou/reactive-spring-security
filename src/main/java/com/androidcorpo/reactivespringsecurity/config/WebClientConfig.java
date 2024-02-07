package com.androidcorpo.reactivespringsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author Severin Mbekou <mbekou99@gmail.com>
 */
@Configuration
public class WebClientConfig {
  @Bean
  public WebClient webClient(final ClientRegistrationRepository clientRegistrations) {
    InMemoryReactiveClientRegistrationRepository registrationRepository =
        new InMemoryReactiveClientRegistrationRepository(clientRegistrations.findByRegistrationId("<registration name>"));
    InMemoryReactiveOAuth2AuthorizedClientService authorizedClientService = new InMemoryReactiveOAuth2AuthorizedClientService(registrationRepository);

    ReactiveOAuth2AuthorizedClientProvider oAuth2AuthorizedClientProvider = ReactiveOAuth2AuthorizedClientProviderBuilder.builder().password().refreshToken().build();
    AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(registrationRepository, authorizedClientService);

    authorizedClientManager.setAuthorizedClientProvider(oAuth2AuthorizedClientProvider);
    authorizedClientManager.setContextAttributesMapper(oAuth2AuthorizeRequest -> (Mono.just(Map.of(
        OAuth2AuthorizationContext.USERNAME_ATTRIBUTE_NAME, "<usename>", OAuth2AuthorizationContext.PASSWORD_ATTRIBUTE_NAME, "<passwor>"
    ))));
    ServerOAuth2AuthorizedClientExchangeFilterFunction clientExchangeFilterFunction = new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
    clientExchangeFilterFunction.setDefaultClientRegistrationId("<registration name>");
    return WebClient.builder().filter(clientExchangeFilterFunction).baseUrl("<change with your resource server url>").build();
  }
}
