package ru.yandex.front_ui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;
import ru.yandex.front_ui.dto.transfer.CreateTransactionRequestDTO;
import ru.yandex.front_ui.dto.transfer.UserPickRequest;
import ru.yandex.front_ui.service.AccountService;
import ru.yandex.front_ui.service.TransferService;
import ru.yandex.front_ui.service.UserService;
import ru.yandex.front_ui.utils.SecurityUtils;

@Controller
@RequiredArgsConstructor
@RequestMapping("/transfer")
public class TransferController {
    private final TransferService transferService;
    private final AccountService accountService;
    private final UserService userService;

    @GetMapping
    private Mono<String> transferPage(Model model) {
        model.addAttribute("accounts", accountService.readAccountsOfUser());
        model.addAttribute("current", SecurityUtils.getUserId());
        model.addAttribute("users", userService.readUsersWithAccounts());
        return Mono.just("transfer");
    }

    @PostMapping
    private Mono<String> transferPageWithSelectedUser(@ModelAttribute UserPickRequest request, Model model) {
        model.addAttribute("accounts", accountService.readAccountsOfUser());
        model.addAttribute("current", SecurityUtils.getUserId());
        model.addAttribute("users", userService.readUsersWithAccounts());
        model.addAttribute("receiver_id", request.getUser());
        model.addAttribute("receiver_accounts", accountService.readAccountsOfUserById(request.getUser()));
        return Mono.just("transfer");
    }

    @PostMapping("/self-transfer")
    private Mono<String> selfTransfer(@ModelAttribute CreateTransactionRequestDTO request, Model model) {
        return transferService.selfTransfer(request)
                .flatMap(__ -> Mono.just("redirect:/transfer"));
    }

    @PostMapping("/transfer-to-other-user")
    private Mono<String> transferToOtherUser(@ModelAttribute CreateTransactionRequestDTO request, Model model) {
        return transferService.transferToOtherUser(request)
                .flatMap(__ -> Mono.just("redirect:/transfer"));
    }
}
