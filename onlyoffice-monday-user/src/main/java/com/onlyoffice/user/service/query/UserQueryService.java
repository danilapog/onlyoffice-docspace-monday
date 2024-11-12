package com.onlyoffice.user.service.query;

import com.onlyoffice.common.user.transfer.request.query.FindDocSpaceUsers;
import com.onlyoffice.common.user.transfer.request.query.FindUser;
import com.onlyoffice.common.user.transfer.response.DocSpaceUsers;
import com.onlyoffice.common.user.transfer.response.UserCredentials;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

public interface UserQueryService {
  UserCredentials findUser(@Valid FindUser query);

  UserCredentials findUser(@Valid FindUser query, @Positive int timeout);

  DocSpaceUsers findDocSpaceUsers(@Valid FindDocSpaceUsers query);

  DocSpaceUsers findDocSpaceUsers(@Valid FindDocSpaceUsers query, @Positive int timeout);
}
