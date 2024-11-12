package com.onlyoffice.tenant.service.remote;

import com.onlyoffice.common.CommandMessage;
import com.onlyoffice.common.tenant.transfer.event.AccessKeyRefreshed;
import com.onlyoffice.common.tenant.transfer.request.command.InviteRoomUsers;
import com.onlyoffice.common.tenant.transfer.request.command.RefreshAccessKey;
import jakarta.validation.Valid;

public interface DocSpaceRemoteRoomService {
  AccessKeyRefreshed refreshAccessKey(@Valid CommandMessage<RefreshAccessKey> command);

  void inviteUsers(@Valid CommandMessage<InviteRoomUsers> command);
}
