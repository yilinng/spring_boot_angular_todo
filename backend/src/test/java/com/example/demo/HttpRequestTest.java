package com.example.demo;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Value;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {

  @Value(value = "${local.server.port}")
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void greetingShouldReturnDefaultMessage() throws Exception {
    assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/todos",
        String.class)).contains(
            "[{\"id\":\"65001b412b002e5e1d0e9fa5\",\"title\":\"Java provides a method for comparing two Array List. The ArrayList.equals() is the method used for comparing two Array List. It compares the Array lists as, both Array lists should have the same size, and all corresponding pairs of elements in the two Array lists are equal.\",\"content\":\"test content....12\",\"createDate\":\"2023-09-12T16:03:13.876\",\"updateDate\":\"2023-09-13T12:59:18.271\",\"userId\":\"64ffcb6e862033602656ca89\"},{\"id\":\"650141c26985ba456ac0108a\",\"title\":\"test title\",\"content\":\"test content\",\"createDate\":\"2023-09-13T12:59:46.747\",\"updateDate\":null,\"userId\":\"64ffcb6e862033602656ca89\"}]");
  }
}
