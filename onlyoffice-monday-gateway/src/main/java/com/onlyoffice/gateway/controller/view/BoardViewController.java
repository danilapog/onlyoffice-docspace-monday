package com.onlyoffice.gateway.controller.view;

import com.onlyoffice.common.service.encryption.EncryptionService;
import com.onlyoffice.common.tenant.transfer.response.BoardInformation;
import com.onlyoffice.common.tenant.transfer.response.TenantCredentials;
import com.onlyoffice.common.user.transfer.response.UserCredentials;
import com.onlyoffice.gateway.client.TenantServiceClient;
import com.onlyoffice.gateway.client.UserServiceClient;
import com.onlyoffice.gateway.configuration.i18n.MessageSourceService;
import com.onlyoffice.gateway.controller.view.model.*;
import com.onlyoffice.gateway.controller.view.model.board.BoardAdminConfigureModel;
import com.onlyoffice.gateway.controller.view.model.board.BoardCreateRoomModel;
import com.onlyoffice.gateway.controller.view.model.board.DocSpaceBoardModel;
import com.onlyoffice.gateway.controller.view.model.settings.SettingsConfigureInformationModel;
import com.onlyoffice.gateway.controller.view.model.settings.SettingsLoginFormModel;
import com.onlyoffice.gateway.security.MondayAuthenticationPrincipal;
import io.micrometer.context.ContextExecutorService;
import io.micrometer.context.ContextSnapshotFactory;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/views")
@RequiredArgsConstructor
public class BoardViewController implements InitializingBean, DisposableBean {
  @Value("${server.origin}")
  private String selfOrigin;

  private final ExecutorService executor =
      ContextExecutorService.wrap(
          Executors.newVirtualThreadPerTaskExecutor(), ContextSnapshotFactory.builder().build());
  private final MeterRegistry meterRegistry;
  private final MessageSourceService messageService;
  private final EncryptionService encryptionService;
  private final TenantServiceClient tenantService;
  private final UserServiceClient userService;
  private Counter counter;

  public void afterPropertiesSet() {
    counter =
        Counter.builder("docs.rendered.request")
            .description("Number of times docSpace has been rendered")
            .register(meterRegistry);
  }

  @GetMapping
  public ModelAndView renderBoard(
      @RequestParam("boardId") long boardId,
      @AuthenticationPrincipal MondayAuthenticationPrincipal user) {
    return getBoardView(boardId, user, false);
  }

  @GetMapping("/refresh")
  @ResponseBody
  public ModelAndView refreshBoard(
      @RequestParam("boardId") long boardId,
      @AuthenticationPrincipal MondayAuthenticationPrincipal user) {
    return getBoardView(boardId, user, true);
  }

  private ModelAndView getBoardView(
      long boardId, MondayAuthenticationPrincipal user, boolean partial) {
    try {
      var userCredentials = getUserCredentials(user);
      var tenantCredentials = getTenantCredentials(user);
      var boardInformation = getBoardInformation(boardId);

      if (tenantCredentials == null) {
        return handleTenantNotFound(user, partial);
      }

      if (boardInformation == null) {
        return user.isAdmin()
            ? renderCreateRoomView(userCredentials, tenantCredentials, partial)
            : renderErrorView(tenantCredentials, userCredentials, partial);
      }

      counter.increment();
      return renderDocSpaceBoardView(tenantCredentials, userCredentials, boardInformation, partial);
    } catch (CompletionException e) {
      return handleServerError(user, partial);
    } catch (RuntimeException e) {
      log.error("Could not render board page", e);
      return handleTenantNotFound(user, partial);
    }
  }

  private UserCredentials getUserCredentials(MondayAuthenticationPrincipal user) {
    return CompletableFuture.supplyAsync(
            () -> userService.findUser(user.getAccountId(), user.getUserId()), executor)
        .thenApply(
            response -> {
              if (response.getBody() != null) {
                response.getBody().setHash(encryptionService.decrypt(response.getBody().getHash()));
              }
              return response.getBody();
            })
        .join();
  }

  private TenantCredentials getTenantCredentials(MondayAuthenticationPrincipal user) {
    return CompletableFuture.supplyAsync(
            () -> tenantService.findTenant(user.getAccountId()), executor)
        .join()
        .getBody();
  }

  private BoardInformation getBoardInformation(long boardId) {
    return CompletableFuture.supplyAsync(() -> tenantService.findBoard(boardId), executor)
        .join()
        .getBody();
  }

  private ModelAndView handleTenantNotFound(MondayAuthenticationPrincipal user, boolean partial) {
    return user.isAdmin()
        ? renderAdminConfigureView(user, partial)
        : renderSimpleErrorView(
            messageService.getMessage("pages.errors.configuration.header"),
            messageService.getMessage("pages.errors.configuration.subtext"),
            TemplateLocation.NOT_CONFIGURED_ERROR.getPath(),
            partial);
  }

  private ModelAndView handleServerError(MondayAuthenticationPrincipal user, boolean partial) {
    return renderSimpleErrorView(
        messageService.getMessage("pages.errors.server.header"),
        messageService.getMessage("pages.errors.server.subtext"),
        TemplateLocation.SERVER_ERROR.getPath(),
        partial);
  }

  private ModelAndView renderCreateRoomView(
      UserCredentials userCredentials, TenantCredentials tenantCredentials, boolean partial) {
    return renderView(
        partial,
        "pages/board/create",
        BoardCreateRoomModel.builder()
            .login(buildLoginModel(tenantCredentials, userCredentials))
            .creationInformation(
                BoardCreateRoomModel.BoardCreateRoomInformationModel.builder()
                    .welcomeText(messageService.getMessage("pages.creation.welcomeText"))
                    .createText(messageService.getMessage("pages.creation.createText"))
                    .buttonText(messageService.getMessage("pages.creation.buttonText"))
                    .build())
            .roomsQuotaError(messageService.getMessage("pages.creation.roomsQuotaError"))
            .timeoutError(messageService.getMessage("pages.creation.timeoutError"))
            .operationError(messageService.getMessage("pages.creation.operationError"))
            .build());
  }

  private ModelAndView renderDocSpaceBoardView(
      TenantCredentials tenantCredentials,
      UserCredentials userCredentials,
      BoardInformation boardInformation,
      boolean partial) {
    return renderView(
        partial,
        "pages/board/docspace",
        DocSpaceBoardModel.builder()
            .login(buildLoginModel(tenantCredentials, userCredentials))
            .docSpaceManager(
                DocSpaceBoardModel.DocSpaceBoardManagerModel.builder()
                    .accessKey(boardInformation.getAccessKey())
                    .roomId(boardInformation.getRoomId())
                    .notificationText(messageService.getMessage("pages.docSpace.notificationText"))
                    .welcomeText(messageService.getMessage("pages.docSpace.welcomeText"))
                    .notPublicText(messageService.getMessage("pages.docSpace.notPublicText"))
                    .unlinkRoomText(messageService.getMessage("pages.docSpace.unlinkRoomText"))
                    .build())
            .build());
  }

  private ModelAndView renderAdminConfigureView(
      MondayAuthenticationPrincipal user, boolean partial) {
    var model =
        BoardAdminConfigureModel.builder()
            .settingsForm(buildSettingsLoginFormModel())
            .login(
                LoginModel.builder()
                    .accessText(
                        messageService.getMessage("pages.settings.configure.login.accessText"))
                    .addressText(user.getSlug())
                    .error(messageService.getMessage("pages.settings.configure.login.error"))
                    .success(messageService.getMessage("pages.settings.configure.login.success"))
                    .build())
            .information(buildSettingsConfigureInformationModel(user))
            .build();

    return renderView(partial, TemplateLocation.BOARD_ADMIN_CONFIGURE.getPath(), model);
  }

  private ModelAndView renderErrorView(
      TenantCredentials tenantCredentials, UserCredentials userCredentials, boolean partial) {
    return renderView(
        partial,
        TemplateLocation.NO_ROOM_ERROR.getPath(),
        ErrorPageModel.builder()
            .refresh("/views/refresh")
            .login(buildLoginModel(tenantCredentials, userCredentials))
            .error(
                ErrorPageModel.ErrorText.builder()
                    .header(messageService.getMessage("pages.errors.room.header"))
                    .subtext(messageService.getMessage("pages.errors.room.subtext"))
                    .build())
            .build());
  }

  private ModelAndView renderSimpleErrorView(
      String header, String subtext, String path, boolean partial) {
    return renderView(
        partial,
        path,
        ErrorPageModel.builder()
            .refresh("/views/refresh")
            .error(ErrorPageModel.ErrorText.builder().header(header).subtext(subtext).build())
            .build());
  }

  private <T> ModelAndView renderView(boolean partial, String location, T data) {
    return partial ? renderPartialView(location, data) : renderFullView(location, data);
  }

  private <T> ModelAndView renderFullView(String location, T data) {
    return new ModelAndView(
        "pages/root",
        "page",
        PageRendererWrapper.<T>builder().location(location).data(data).build());
  }

  private <T> ModelAndView renderPartialView(String location, T data) {
    return new ModelAndView(
        location, "page", PageRendererWrapper.builder().location(location).data(data).build());
  }

  private LoginModel buildLoginModel(
      TenantCredentials tenantCredentials, UserCredentials userCredentials) {
    return LoginModel.builder()
        .url(tenantCredentials != null ? tenantCredentials.getDocSpaceUrl() : "")
        .email(userCredentials != null ? userCredentials.getEmail() : "")
        .hash(userCredentials != null ? userCredentials.getHash() : "")
        .success(messageService.getMessage("pages.settings.configure.login.success"))
        .error(messageService.getMessage("pages.settings.configure.login.error"))
        .build();
  }

  private SettingsLoginFormModel buildSettingsLoginFormModel() {
    return SettingsLoginFormModel.builder()
        .loginText(messageService.getMessage("pages.settings.configure.field.login"))
        .changeText(messageService.getMessage("pages.settings.configure.field.change"))
        .fields(
            SettingsLoginFormModel.SettingsLoginFormFields.builder()
                .docSpaceLabel(
                    messageService.getMessage("pages.settings.configure.field.docSpaceLabel"))
                .docSpacePlaceholder(
                    messageService.getMessage("pages.settings.configure.field.docSpacePlaceholder"))
                .emailLabel(messageService.getMessage("pages.settings.configure.field.emailLabel"))
                .emailPlaceholder(
                    messageService.getMessage("pages.settings.configure.field.emailPlaceholder"))
                .passwordLabel(
                    messageService.getMessage("pages.settings.configure.field.passwordLabel"))
                .passwordPlaceholder(
                    messageService.getMessage("pages.settings.configure.field.passwordPlaceholder"))
                .build())
        .build();
  }

  private SettingsConfigureInformationModel buildSettingsConfigureInformationModel(
      MondayAuthenticationPrincipal user) {
    return SettingsConfigureInformationModel.builder()
        .csp(messageService.getMessage("pages.settings.configure.information.csp"))
        .credentials(messageService.getMessage("pages.settings.configure.information.credentials"))
        .monday(messageService.getMessage("pages.settings.configure.information.monday"))
        .mondayAddress(String.format("https://%s.monday.com", user.getSlug()))
        .app(messageService.getMessage("pages.settings.configure.information.app"))
        .appAddress(selfOrigin)
        .build();
  }

  public void destroy() {
    executor.shutdown();
  }
}
