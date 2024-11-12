package com.onlyoffice.gateway.controller.view.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageRendererWrapper<T> {
  private String location;
  private T data;
}
