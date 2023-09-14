package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDetailsDto {
  @JsonProperty("responseCode")
  private String responseCode;

  @JsonProperty("responseStatus")
  private String responseStatus;

  @JsonProperty("responseMessage")
  private String responseMessage;
}
