package com.onlyoffice.gateway.controller.view;

import com.onlyoffice.common.service.encryption.EncryptionService;
import com.onlyoffice.common.tenant.transfer.response.BoardInformation;
import com.onlyoffice.common.tenant.transfer.response.TenantCredentials;
import com.onlyoffice.common.user.transfer.response.UserCredentials;
import com.onlyoffice.gateway.client.TenantServiceClient;
import com.onlyoffice.gateway.client.UserServiceClient;
import com.onlyoffice.gateway.configuration.i18n.MessageSourceService;
import com.onlyoffice.gateway.controller.view.model.ErrorPageModel;
import com.onlyoffice.gateway.controller.view.model.LoginModel;
import com.onlyoffice.gateway.controller.view.model.PageRendererWrapper;
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

  @Override
  public void afterPropertiesSet() throws Exception {
    counter =
        Counter.builder("docs.rendered.request")
            .description("Number of times docSpace has been rendered")
            .register(meterRegistry);
  }

  @GetMapping
  public ModelAndView renderBoard(
      @RequestParam("boardId") int boardId,
      @AuthenticationPrincipal MondayAuthenticationPrincipal user) {
    var userFuture =
        CompletableFuture.supplyAsync(
                () -> userService.findUser(user.getAccountId(), user.getUserId()), executor)
            .thenApply(
                res -> {
                  if (res.getBody() != null)
                    res.getBody().setHash(encryptionService.decrypt(res.getBody().getHash()));
                  return res;
                });
    var tenantFuture =
        CompletableFuture.supplyAsync(
            () -> tenantService.findTenant(user.getAccountId()), executor);
    var boardFuture =
        CompletableFuture.supplyAsync(() -> tenantService.findBoard(boardId), executor);

    try {
      CompletableFuture.allOf(userFuture, tenantFuture, boardFuture);
      var userResponse = userFuture.join();
      var tenantResponse = tenantFuture.join();
      var boardResponse = boardFuture.join();

      var userCredentials = userResponse.getBody();
      var tenantCredentials = tenantResponse.getBody();
      if (!tenantResponse.getStatusCode().is2xxSuccessful() || tenantCredentials == null) {
        log.debug("Rendering tenant not found page");
        return handleTenantNotFound(user);
      }

      if (!boardResponse.getStatusCode().is2xxSuccessful() || boardResponse.getBody() == null) {
        log.debug("Rendering no room page");
        return user.isAdmin()
            ? renderCreateRoomView(userCredentials, tenantCredentials)
            : renderErrorView(tenantCredentials, userCredentials);
      }

      counter.increment();
      return renderDocSpaceBoardView(tenantCredentials, userCredentials, boardResponse.getBody());
    } catch (RuntimeException e) {
      log.error("Could not render board page", e);
      return handleTenantNotFound(user);
    }
  }

  private ModelAndView handleTenantNotFound(MondayAuthenticationPrincipal user) {
    return user.isAdmin()
        ? renderAdminConfigureView(user)
        : renderSimpleErrorView(
            "pages.errors.configuration.header",
            "pages.errors.configuration.subtext",
            TemplateLocation.NOT_CONFIGURED_ERROR.getPath());
  }

  private ModelAndView renderCreateRoomView(
      UserCredentials userCredentials, TenantCredentials tenantCredentials) {
    return renderView(
        "pages/board/create",
        BoardCreateRoomModel.builder()
            .login(buildLoginModel(tenantCredentials, userCredentials))
            .creationInformation(
                BoardCreateRoomModel.BoardCreateRoomInformationModel.builder()
                    .welcomeText(messageService.getMessage("pages.creation.welcomeText"))
                    .createText(messageService.getMessage("pages.creation.createText"))
                    .buttonText(messageService.getMessage("pages.creation.buttonText"))
                    .build())
            .timeoutError(messageService.getMessage("pages.creation.timeoutError"))
            .operationError(messageService.getMessage("pages.creation.operationError"))
            .build());
  }

  private ModelAndView renderDocSpaceBoardView(
      TenantCredentials tenantCredentials,
      UserCredentials userCredentials,
      BoardInformation boardInformation) {
    return renderView(
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
                    .build())
            .build());
  }

  private ModelAndView renderAdminConfigureView(MondayAuthenticationPrincipal user) {
    return renderView(
        TemplateLocation.BOARD_ADMIN_CONFIGURE.getPath(),
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
            .build());
  }

  private ModelAndView renderErrorView(
      TenantCredentials tenantCredentials, UserCredentials userCredentials) {
    return renderView(
        TemplateLocation.NO_ROOM_ERROR.getPath(),
        ErrorPageModel.builder()
            .login(buildLoginModel(tenantCredentials, userCredentials))
            .error(buildErrorTextModel("pages.errors.room.header", "pages.errors.room.subtext"))
            .build());
  }

  private ModelAndView renderSimpleErrorView(String headerKey, String subtextKey, String path) {
    return renderView(
        path, ErrorPageModel.builder().error(buildErrorTextModel(headerKey, subtextKey)).build());
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

  private ErrorPageModel.ErrorText buildErrorTextModel(String headerKey, String subtextKey) {
    return ErrorPageModel.ErrorText.builder()
        .header(messageService.getMessage(headerKey))
        .subtext(messageService.getMessage(subtextKey))
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
        .mondayAddress(String.format("https://%s", user.getSlug()))
        .app(messageService.getMessage("pages.settings.configure.information.app"))
        .appAddress(selfOrigin)
        .build();
  }

  private <T> ModelAndView renderView(String location, T data) {
    return new ModelAndView(
        "pages/root",
        "page",
        PageRendererWrapper.<T>builder().location(location).data(data).build());
  }

  public void destroy() {
    executor.shutdown();
  }
}
