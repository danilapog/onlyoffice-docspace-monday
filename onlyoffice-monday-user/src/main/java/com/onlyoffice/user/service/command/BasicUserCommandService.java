package com.onlyoffice.user.service.command;

import com.onlyoffice.common.CommandMessage;
import com.onlyoffice.common.user.transfer.request.command.RegisterUser;
import com.onlyoffice.common.user.transfer.request.command.RemoveTenantUsers;
import com.onlyoffice.user.persistence.entity.User;
import com.onlyoffice.user.persistence.entity.UserId;
import com.onlyoffice.user.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserCommandService implements UserCommandService {
  private final UserRepository userRepository;

  @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
  public void register(RegisterUser payload) {
    try {
      MDC.put("tenant_id", String.valueOf(payload.getTenantId()));
      MDC.put("monday_id", String.valueOf(payload.getMondayId()));
      MDC.put("docSpace_id", String.valueOf(payload.getDocSpaceId()));
      log.info("Registering a new user set of credentials");

      userRepository
          .findById(
              UserId.builder()
                  .mondayId(payload.getMondayId())
                  .tenantId(payload.getTenantId())
                  .build())
          .ifPresentOrElse(
              u -> {
                u.setDocSpaceId(payload.getDocSpaceId());
                u.setEmail(payload.getEmail());
                u.setHash(payload.getHash());
                userRepository.save(u);
              },
              () ->
                  userRepository.save(
                      User.builder()
                          .mondayId(payload.getMondayId())
                          .tenantId(payload.getTenantId())
                          .docSpaceId(payload.getDocSpaceId())
                          .email(payload.getEmail())
                          .hash(payload.getHash())
                          .build()));
    } finally {
      MDC.clear();
    }
  }

  @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
  public void register(CommandMessage<RegisterUser> command) {
    try {
      var payload = command.getPayload();
      MDC.put("tenant_id", String.valueOf(payload.getTenantId()));
      MDC.put("monday_id", String.valueOf(payload.getMondayId()));
      MDC.put("docSpace_id", String.valueOf(payload.getDocSpaceId()));
      log.info("Registering a new user set of credentials asynchronously");

      userRepository
          .findById(
              UserId.builder()
                  .mondayId(payload.getMondayId())
                  .tenantId(payload.getTenantId())
                  .build())
          .ifPresentOrElse(
              u -> {
                if (u.getUpdatedAt().compareTo(command.getCommandAt()) < 0) {
                  u.setDocSpaceId(payload.getDocSpaceId());
                  u.setEmail(payload.getEmail());
                  u.setHash(payload.getHash());
                  userRepository.save(u);
                }
              },
              () ->
                  userRepository.save(
                      User.builder()
                          .mondayId(payload.getMondayId())
                          .tenantId(payload.getTenantId())
                          .docSpaceId(payload.getDocSpaceId())
                          .email(payload.getEmail())
                          .hash(payload.getHash())
                          .build()));
    } finally {
      MDC.clear();
    }
  }

  @Transactional
  public void removeAll(CommandMessage<RemoveTenantUsers> command) {
    try {
      var payload = command.getPayload();
      MDC.put("tenant_id", String.valueOf(payload.getTenantId()));
      log.info("Removing all tenant users");

      userRepository.deleteAllByTenantIdAndUpdatedAtLessThanEqual(
          payload.getTenantId(), command.getCommandAt());
    } finally {
      MDC.clear();
    }
  }
}
