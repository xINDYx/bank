package ru.yandex.blocker_service.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransferToOtherUserTransaction extends Transaction {

    private Long receiverAccountId;
}
