package com.kbtg.bootcamp.posttest.user;

import com.kbtg.bootcamp.posttest.exception.InternalServiceException;
import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.lottery.dto.LotteryListResponseDto;

import com.kbtg.bootcamp.posttest.lottery.dto.LotteryResponseDto;
import com.kbtg.bootcamp.posttest.user.dto.UserTicketResponseDto;
import com.kbtg.bootcamp.posttest.user.dto.UserTicketsRequestDto;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users/{userId}/lotteries/{lotteries}")
    public UserTicketsRequestDto buyLotteries(
            @Validated @Pattern(regexp = "\\d{10}") @PathVariable("userId") String userId,
            @PathVariable("lotteries") String lotteries) {
        try {
            return this.userService.buyLotteryTicket(userId, lotteries);
        } catch (Exception exception) {
            throw new InternalServiceException("Failed to process lottery purchase. Unable to call user service");
        }
    }

    @GetMapping("/users/{userId}/lotteries")
    public UserTicketResponseDto getAllLottery(@PathVariable("userId") String userId) {
        try {
            return this.userService.getUserLottery(userId);
        } catch (Exception exception) {
            throw new InternalServiceException("Failed to process get all lotteries. Unable to call user service");
        }
    }

    @DeleteMapping("/users/{userId}/lotteries/{ticket}")
    public LotteryResponseDto deleteLotteries (@PathVariable("userId") String userId,
                                               @PathVariable("ticket") String ticket) {
        try {
            return this.userService.deleteLottery(userId, ticket);
        }catch (Exception exception) {
            throw new InternalServiceException("Failed to process buy lotteries. Unable to call user service");
        }
    }
}


