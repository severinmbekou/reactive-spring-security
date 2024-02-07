package com.androidcorpo.reactivespringsecurity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Severin Mbekou <mbekou99@gmail.com>
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class DemoRestController {
  private final WebClient webClient;

  @GetMapping
  public String test() {

    return webClient.get().uri("demo").retrieve()
        .bodyToMono(String.class).doOnSuccess(log::info).doOnError(s -> log.error("{} ", s)).block();
  }
}
