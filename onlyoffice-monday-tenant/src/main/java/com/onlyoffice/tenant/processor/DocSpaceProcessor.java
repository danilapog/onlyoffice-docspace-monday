package com.onlyoffice.tenant.processor;

import com.onlyoffice.common.CommandMessage;
import com.onlyoffice.common.tenant.transfer.request.command.InviteRoomUsers;
import com.onlyoffice.common.tenant.transfer.request.command.RefreshAccessKey;
import java.util.function.Consumer;

public interface DocSpaceProcessor {
  Consumer<CommandMessage<RefreshAccessKey>> refreshKeyConsumer();

  Consumer<CommandMessage<InviteRoomUsers>> inviteUsersConsumer();
}
