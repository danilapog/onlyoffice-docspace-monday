package com.onlyoffice.common.docspace.transfer.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericResponse<D> {
  private int status;
  private D response;
}
