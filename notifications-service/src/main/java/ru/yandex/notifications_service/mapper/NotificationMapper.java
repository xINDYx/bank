package ru.yandex.notifications_service.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.yandex.notifications_service.dto.CreateEmailNotificationRequestDTO;
import ru.yandex.notifications_service.dto.EmailNotificationResponseDTO;
import ru.yandex.notifications_service.model.EmailNotification;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class NotificationMapper {
    public abstract EmailNotificationResponseDTO map(EmailNotification notification);

    public abstract EmailNotification map(CreateEmailNotificationRequestDTO notification);

    @AfterMapping
    protected void setSentToFalse(@MappingTarget EmailNotification emailNotification) {
        if (emailNotification.getSent() == null) {
            emailNotification.setSent(false);
        }
    }

}
