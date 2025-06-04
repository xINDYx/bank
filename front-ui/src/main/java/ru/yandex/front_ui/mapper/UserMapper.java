package ru.yandex.front_ui.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.yandex.bank.clients.accounts.dto.accounts.UsersAccountsResponse;
import ru.yandex.bank.clients.accounts.dto.user.ChangePasswordRequest;
import ru.yandex.bank.clients.accounts.dto.user.RegisterUserRequest;
import ru.yandex.bank.clients.accounts.dto.user.UpdateUserRequest;
import ru.yandex.bank.clients.accounts.dto.user.UserResponse;
import ru.yandex.front_ui.dto.user.*;
import ru.yandex.front_ui.model.User;

@Mapper(
        uses = {AccountMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {

    public abstract User mapToUser(UserResponse user);

    public abstract UpdateUserRequest map(UpdateUserRequestDTO request);

    public abstract UserAccountResponseDTO map(UsersAccountsResponse request);

    public abstract UserResponseDTO map(UserResponse user);

    public abstract User map(UserResponseDTO user);

    public abstract RegisterUserRequest map(RegisterUserRequestDTO userRequestDTO);

    public abstract ChangePasswordRequest map(ChangePasswordRequestDTO userRequestDTO);
}
