package com.onlyoffice.gateway.controller.view;

import com.onlyoffice.gateway.client.TenantServiceClient;
import com.onlyoffice.gateway.configuration.i18n.MessageSourceService;
import com.onlyoffice.gateway.controller.view.model.ErrorPageModel;
import com.onlyoffice.gateway.controller.view.model.LoginModel;
import com.onlyoffice.gateway.controller.view.model.PageRendererWrapper;
import com.onlyoffice.gateway.controller.view.model.settings.*;
import com.onlyoffice.gateway.security.MondayAuthenticationPrincipal;
import java.net.URI;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping(
    value = "/views/settings",
    produces = {MediaType.TEXT_HTML_VALUE})
@RequiredArgsConstructor
public class SettingsViewController {
  @Value("${server.origin}")
  private String selfOrigin;

  private final MessageSourceService messageService;
  private final TenantServiceClient tenantService;

  @GetMapping
  public ModelAndView renderSettings(@AuthenticationPrincipal MondayAuthenticationPrincipal user) {
    log.debug("Rendering settings page");
    var tenantResponse = tenantService.findTenant(user.getAccountId());
    var tenantExists =
        tenantResponse.getStatusCode().is2xxSuccessful() && tenantResponse.getBody() != null;

    if (!tenantExists) {
      log.debug("Rendering page when tenant does not exist");
      return user.isAdmin()
          ? renderPage(TemplateLocation.ADMIN_CONFIGURE, buildAdminConfigureModel(user.getSlug()))
          : renderErrorPage(
              messageService.getMessage("pages.errors.configuration.header"),
              messageService.getMessage("pages.errors.configuration.subtext"));
    }

    Function<String, ModelAndView> renderLoginPage =
        user.isAdmin()
            ? url ->
                renderPage(TemplateLocation.ADMIN_LOGIN, buildAdminLoginModel(url, user.getSlug()))
            : url ->
                renderPage(TemplateLocation.USER_LOGIN, buildUserLoginModel(url, user.getSlug()));

    return renderLoginPage.apply(tenantResponse.getBody().getDocSpaceUrl());
  }

  @GetMapping("/change")
  public ModelAndView renderChange(@AuthenticationPrincipal MondayAuthenticationPrincipal user) {
    log.debug("Rendering change page");
    return renderPage(TemplateLocation.ADMIN_CONFIGURE, buildAdminConfigureModel(user.getSlug()));
  }

  private ModelAndView renderPage(TemplateLocation location, Object dataModel) {
    return new ModelAndView(
        "pages/root",
        "page",
        PageRendererWrapper.builder().location(location.getPath()).data(dataModel).build());
  }

  private ModelAndView renderErrorPage(String header, String subtext) {
    log.debug("Rendering not configured page");
    return renderPage(
        TemplateLocation.NOT_CONFIGURED_ERROR,
        ErrorPageModel.builder()
            .error(ErrorPageModel.ErrorText.builder().header(header).subtext(subtext).build())
            .build());
  }

  private SettingsAdminConfigureModel buildAdminConfigureModel(String slug) {
    return SettingsAdminConfigureModel.builder()
        .login(
            LoginModel.builder()
                .accessText(messageService.getMessage("pages.settings.configure.login.accessText"))
                .addressText(slug)
                .error(messageService.getMessage("pages.settings.configure.login.error"))
                .success(messageService.getMessage("pages.settings.configure.login.success"))
                .build())
        .information(buildSettingsConfigureInformationModel(slug))
        .settingsForm(buildSettingsLoginFormModel())
        .build();
  }

  private SettingsAdminLoginModel buildAdminLoginModel(String url, String slug) {
    return SettingsAdminLoginModel.builder()
        .settingsForm(buildSettingsLoginFormModel())
        .login(buildSettingsLoginModel(url, getDocSpaceAddress(URI.create(url))))
        .information(buildSettingsConfigureInformationModel(slug))
        .modal(buildSettingsModalModel())
        .build();
  }

  private SettingsUserLoginModel buildUserLoginModel(String url, String slug) {
    return SettingsUserLoginModel.builder()
        .settingsForm(buildSettingsLoginFormModel())
        .login(buildSettingsLoginModel(url, getDocSpaceAddress(URI.create(url))))
        .information(buildSettingsConfigureInformationModel(slug))
        .modal(buildSettingsModalModel())
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

  private LoginModel buildSettingsLoginModel(String url, String docSpaceAddress) {
    return LoginModel.builder()
        .url(url)
        .accessText(messageService.getMessage("pages.settings.configure.login.accessText"))
        .addressText(docSpaceAddress)
        .error(messageService.getMessage("pages.settings.configure.login.error"))
        .success(messageService.getMessage("pages.settings.configure.login.success"))
        .build();
  }

  private SettingsConfigureInformationModel buildSettingsConfigureInformationModel(String slug) {
    return SettingsConfigureInformationModel.builder()
        .csp(messageService.getMessage("pages.settings.configure.information.csp"))
        .credentials(messageService.getMessage("pages.settings.configure.information.credentials"))
        .monday(messageService.getMessage("pages.settings.configure.information.monday"))
        .mondayAddress(String.format("https://%s.monday.com", slug))
        .app(messageService.getMessage("pages.settings.configure.information.app"))
        .appAddress(selfOrigin)
        .build();
  }

  private SettingsModalModel buildSettingsModalModel() {
    return SettingsModalModel.builder()
        .confirmActionText(messageService.getMessage("pages.settings.modal.confirmActionText"))
        .agreementText(messageService.getMessage("pages.settings.modal.agreementText"))
        .changeText(messageService.getMessage("pages.settings.modal.changeText"))
        .cancelText(messageService.getMessage("pages.settings.modal.cancelText"))
        .build();
  }

  private String getDocSpaceAddress(URI uri) {
    var parts = uri.getHost().split("\\.");
    var subdomain =
        parts.length > 2
            ? String.join(".", java.util.Arrays.copyOfRange(parts, 0, parts.length - 2))
            : "";
    var domain = parts.length > 1 ? parts[parts.length - 2] : "";
    var zone = parts.length > 0 ? parts[parts.length - 1] : "";
    return String.format("%s.%s.%s", subdomain, domain, zone);
  }
}
