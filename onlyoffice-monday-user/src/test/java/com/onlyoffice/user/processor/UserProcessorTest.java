package com.onlyoffice.user.processor;

import static org.assertj.core.api.Assertions.assertThatNoException;

import com.onlyoffice.common.CommandMessage;
import com.onlyoffice.common.user.transfer.request.command.RegisterUser;
import com.onlyoffice.common.user.transfer.request.command.RemoveTenantUsers;
import com.onlyoffice.user.service.command.UserCommandService;
import java.time.Instant;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserProcessorTest {
  @Mock UserCommandService userCommandService;
  @InjectMocks UserProcessor processor;

  @Nested
  class UserProcessorRegisterTests {
    @Test
    void shouldProcessRegisterUser_WhenValidCommand() {
      assertThatNoException()
          .isThrownBy(
              () ->
                  processor
                      .registerUserConsumer()
                      .accept(
                          CommandMessage.<RegisterUser>builder()
                              .commandAt(Instant.now().toEpochMilli())
                              .payload(
                                  RegisterUser.builder()
                                      .tenantId(1)
                                      .mondayId(1)
                                      .docSpaceId("aaa-aaa-aaa")
                                      .hash("hash")
                                      .email("test@email.com")
                                      .build())
                              .build()));
    }
  }

  @Nested
  class UserProcessorRemoveTests {
    @Test
    void shouldProcessRemoveUsers_WhenValidCommand() {
      assertThatNoException()
          .isThrownBy(
              () ->
                  processor
                      .removeTenantUsersConsumer()
                      .accept(
                          CommandMessage.<RemoveTenantUsers>builder()
                              .commandAt(Instant.now().toEpochMilli())
                              .payload(RemoveTenantUsers.builder().tenantId(1).build())
                              .build()));
    }
  }
}
