package com.onlyoffice.tenant.controller.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlyoffice.common.tenant.transfer.request.command.RegisterRoom;
import com.onlyoffice.common.tenant.transfer.request.command.RemoveRoom;
import com.onlyoffice.common.user.transfer.response.DocSpaceUsers;
import com.onlyoffice.tenant.client.UserServiceClient;
import com.onlyoffice.tenant.controller.GlobalControllerAdvice;
import com.onlyoffice.tenant.service.command.BoardCommandService;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class BoardCommandControllerTest {
  private MockMvc mvc;
  @Mock private UserServiceClient userService;
  @Mock private BoardCommandService commandService;
  @InjectMocks private BoardCommandController controller;

  private JacksonTester<RegisterRoom> jsonRegisterRoom;
  private JacksonTester<RemoveRoom> jsonRemoveRoom;

  @BeforeEach
  public void setup() {
    var objectMapper = new ObjectMapper();
    JacksonTester.initFields(this, objectMapper);
    mvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(new GlobalControllerAdvice())
            .build();
  }

  @Test
  void shouldRegisterRoom_WhenValidRequest_ThenReturnCreatedStatus() throws Exception {
    var command =
        RegisterRoom.builder()
            .tenantId(1)
            .boardId(2)
            .roomId(3)
            .mondayUsers(Set.of("4", "5"))
            .build();

    var usersResponse =
        ResponseEntity.ok(DocSpaceUsers.builder().ids(Set.of("user1", "user2")).build());

    Mockito.when(userService.findDocSpaceUsers(Mockito.eq(1), Mockito.eq(Set.of("4", "5"))))
        .thenReturn(usersResponse);

    var response =
        mvc.perform(
                post("/tenants/boards/room")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRegisterRoom.write(command).getJson()))
            .andReturn()
            .getResponse();

    Mockito.verify(userService).findDocSpaceUsers(Mockito.eq(1), Mockito.eq(Set.of("4", "5")));
    Mockito.verify(commandService)
        .register(
            Mockito.argThat(
                c ->
                    c.getTenantId() == command.getTenantId()
                        && c.getBoardId() == command.getBoardId()
                        && c.getRoomId() == command.getRoomId()
                        && c.getMondayUsers().equals(command.getMondayUsers())),
            Mockito.eq(Set.of("user1", "user2")));
    assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
  }

  @Test
  void shouldNotRegisterRoom_WhenInvalidRequest_ThenReturnBadRequest() throws Exception {
    var command = RegisterRoom.builder().boardId(2).roomId(3).mondayUsers(Set.of("4", "5")).build();

    var response =
        mvc.perform(
                post("/tenants/boards/room")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRegisterRoom.write(command).getJson()))
            .andReturn()
            .getResponse();

    Mockito.verifyNoInteractions(userService);
    Mockito.verifyNoInteractions(commandService);
    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  void shouldDeleteRoom_WhenValidRequest_ThenReturnOkStatus() throws Exception {
    var command = RemoveRoom.builder().tenantId(1).boardId(2).build();

    var response =
        mvc.perform(
                delete("/tenants/boards/room")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRemoveRoom.write(command).getJson()))
            .andReturn()
            .getResponse();

    Mockito.verify(commandService)
        .remove(
            Mockito.argThat(
                c ->
                    c.getTenantId() == command.getTenantId()
                        && c.getBoardId() == command.getBoardId()));

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
  }

  @Test
  void shouldNotDeleteRoom_WhenInvalidRequest_ThenReturnBadRequest() throws Exception {
    var command = RemoveRoom.builder().boardId(2).build();

    var response =
        mvc.perform(
                delete("/tenants/boards/room")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRemoveRoom.write(command).getJson()))
            .andReturn()
            .getResponse();

    Mockito.verifyNoInteractions(commandService);
    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }
}
