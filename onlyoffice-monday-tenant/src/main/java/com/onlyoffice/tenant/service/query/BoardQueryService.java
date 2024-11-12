package com.onlyoffice.tenant.service.query;

import com.onlyoffice.common.tenant.transfer.request.query.FindEntity;
import com.onlyoffice.common.tenant.transfer.response.BoardInformation;
import jakarta.validation.Valid;

public interface BoardQueryService {
  BoardInformation find(@Valid FindEntity query);
}
