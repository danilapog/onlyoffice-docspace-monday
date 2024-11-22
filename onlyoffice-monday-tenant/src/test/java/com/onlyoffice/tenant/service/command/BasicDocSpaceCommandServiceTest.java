package com.onlyoffice.tenant.service.command;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.onlyoffice.common.tenant.transfer.request.command.RegisterDocSpace;
import com.onlyoffice.tenant.exception.TenantNotFoundException;
import com.onlyoffice.tenant.persistence.entity.Board;
import com.onlyoffice.tenant.persistence.entity.Docspace;
import com.onlyoffice.tenant.persistence.entity.Tenant;
import com.onlyoffice.tenant.persistence.repository.TenantRepository;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

@ExtendWith(MockitoExtension.class)
public class BasicDocSpaceCommandServiceTest {
  @Mock private TenantRepository tenantRepository;
  @InjectMocks private BasicDocSpaceCommandService service;

  @BeforeEach
  public void setup() {
    MDC.clear();
  }

  @Nested
  class RegisterMethodTests {
    @Test
    void register_ShouldThrowTenantNotFoundException_WhenTenantDoesNotExist() {
      var command =
          RegisterDocSpace.builder()
              .tenantId(1)
              .url("https://docspace.example.com")
              .adminLogin("admin")
              .adminHash("hash")
              .build();

      when(tenantRepository.findById(command.getTenantId())).thenReturn(Optional.empty());

      assertThatThrownBy(() -> service.register(command))
          .isInstanceOf(TenantNotFoundException.class)
          .hasMessageContaining("Could not find tenant with id 1");
      verify(tenantRepository).findById(command.getTenantId());
      verifyNoMoreInteractions(tenantRepository);
    }

    @Test
    void register_ShouldSetNewDocspaceAndClearBoards_WhenUrlsNotEqual() {
      var boardOne = Board.builder().id(1).roomId(101).build();
      var boardTwo = Board.builder().id(2).roomId(102).build();
      var existingDocspace =
          Docspace.builder()
              .url("https://old-docspace.example.com")
              .adminLogin("oldAdmin")
              .adminHash("oldHash")
              .build();
      var tenant =
          Tenant.builder()
              .id(1)
              .docspace(existingDocspace)
              .boards(Set.of(boardOne, boardTwo))
              .build();
      var command =
          RegisterDocSpace.builder()
              .tenantId(1)
              .url("https://new-docspace.example.com")
              .adminLogin("newAdmin")
              .adminHash("newHash")
              .build();

      when(tenantRepository.findById(command.getTenantId())).thenReturn(Optional.of(tenant));

      service.register(command);

      assertThat(tenant.getDocspace()).isNotNull();
      assertThat(tenant.getDocspace().getUrl()).isEqualTo(command.getUrl());
      assertThat(tenant.getDocspace().getAdminLogin()).isEqualTo(command.getAdminLogin());
      assertThat(tenant.getDocspace().getAdminHash()).isEqualTo(command.getAdminHash());
      assertThat(tenant.getBoards()).isEmpty();

      verify(tenantRepository).findById(command.getTenantId());
      verifyNoMoreInteractions(tenantRepository);
    }

    @Test
    void register_ShouldUpdateExistingDocSpace_WhenUrlsEqual() {
      var boardOne = Board.builder().id(1).roomId(101).build();
      var boardTwo = Board.builder().id(2).roomId(102).build();
      var existingDocspace =
          Docspace.builder()
              .url("https://docspace.example.com")
              .adminLogin("oldAdmin")
              .adminHash("oldHash")
              .build();
      var tenant =
          Tenant.builder()
              .id(1)
              .docspace(existingDocspace)
              .boards(Set.of(boardOne, boardTwo))
              .build();
      var command =
          RegisterDocSpace.builder()
              .tenantId(1)
              .url("https://docspace.example.com")
              .adminLogin("newAdmin")
              .adminHash("newHash")
              .build();

      when(tenantRepository.findById(command.getTenantId())).thenReturn(Optional.of(tenant));

      service.register(command);

      assertThat(tenant.getDocspace()).isNotNull();
      assertThat(tenant.getDocspace().getUrl()).isEqualTo(command.getUrl());
      assertThat(tenant.getDocspace().getAdminLogin()).isEqualTo(command.getAdminLogin());
      assertThat(tenant.getDocspace().getAdminHash()).isEqualTo(command.getAdminHash());
      assertThat(tenant.getBoards()).containsExactlyInAnyOrder(boardOne, boardTwo);
      verify(tenantRepository).findById(command.getTenantId());
      verifyNoMoreInteractions(tenantRepository);
    }
  }
}
