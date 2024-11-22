package com.onlyoffice.tenant.service.query;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.onlyoffice.common.tenant.transfer.request.query.FindEntity;
import com.onlyoffice.common.tenant.transfer.response.BoardInformation;
import com.onlyoffice.tenant.exception.TenantNotFoundException;
import com.onlyoffice.tenant.persistence.entity.Board;
import com.onlyoffice.tenant.persistence.entity.Tenant;
import com.onlyoffice.tenant.persistence.repository.BoardRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

@ExtendWith(MockitoExtension.class)
public class BasicBoardQueryServiceTest {
  @Mock private BoardRepository boardRepository;
  @InjectMocks private BasicBoardQueryService service;

  @BeforeEach
  public void setup() {
    MDC.clear();
  }

  @Nested
  class FindMethodTests {
    @Test
    void shouldReturnBoardInformation_WhenBoardExists() {
      var query = FindEntity.builder().id(1).build();
      var tenant = Tenant.builder().id(1).build();
      var board = Board.builder().id(1).roomId(1).accessKey("accessKey").tenant(tenant).build();

      when(boardRepository.findById(query.getId())).thenReturn(Optional.of(board));

      BoardInformation result = service.find(query);

      assertThat(result).isNotNull();
      assertThat(result.getId()).isEqualTo(board.getId());
      assertThat(result.getRoomId()).isEqualTo(board.getRoomId());
      assertThat(result.getAccessKey()).isEqualTo(board.getAccessKey());
      assertThat(result.getTenantId()).isEqualTo(tenant.getId());

      verify(boardRepository).findById(query.getId());
      verifyNoMoreInteractions(boardRepository);
    }

    @Test
    void shouldThrowTenantNotFoundException_WhenBoardDoesNotExist() {
      var query = FindEntity.builder().id(1).build();

      when(boardRepository.findById(query.getId())).thenReturn(Optional.empty());

      assertThatThrownBy(() -> service.find(query))
          .isInstanceOf(TenantNotFoundException.class)
          .hasMessageContaining("Could not find board with id 1");

      verify(boardRepository).findById(query.getId());
      verifyNoMoreInteractions(boardRepository);
    }
  }
}
