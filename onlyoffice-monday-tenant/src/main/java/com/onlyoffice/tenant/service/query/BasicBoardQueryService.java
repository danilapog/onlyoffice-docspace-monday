package com.onlyoffice.tenant.service.query;

import com.onlyoffice.common.tenant.transfer.request.query.FindEntity;
import com.onlyoffice.common.tenant.transfer.response.BoardInformation;
import com.onlyoffice.tenant.exception.TenantNotFoundException;
import com.onlyoffice.tenant.persistence.repository.BoardRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class BasicBoardQueryService implements BoardQueryService {
  private final BoardRepository boardRepository;

  public BoardInformation find(@Valid FindEntity query) {
    try {
      MDC.put("board_id", String.valueOf(query.getId()));
      log.info("Trying to find board by id");

      var board =
          boardRepository
              .findById(query.getId())
              .orElseThrow(
                  () ->
                      new TenantNotFoundException(
                          String.format("Could not find board with id %d", query.getId())));

      return BoardInformation.builder()
          .id(board.getId())
          .roomId(board.getRoomId())
          .tenantId(board.getTenant().getId())
          .accessKey(board.getAccessKey())
          .build();
    } finally {
      MDC.clear();
    }
  }
}
