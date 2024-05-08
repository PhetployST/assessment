package com.kbtg.bootcamp.posttest.lottery;

import com.kbtg.bootcamp.posttest.exception.InternalServiceException;
import com.kbtg.bootcamp.posttest.lottery.dto.LotteryListResponseDto;
import com.kbtg.bootcamp.posttest.lottery.dto.LotteryRequestDto;
import com.kbtg.bootcamp.posttest.lottery.dto.LotteryResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import javax.validation.ValidationException;

@RestController
public class LotteryController {

    private final LotteryService lotteryService;

    public LotteryController(LotteryService adminService) {
        this.lotteryService = adminService;
    }

    @GetMapping("/lotteries")
    public ResponseEntity<LotteryListResponseDto> getLotteryList() {
        try {
            LotteryListResponseDto response = lotteryService.getAllLotteries();
            return ResponseEntity.ok(response);
        } catch (Exception exception) {
            throw new InternalServiceException("Error occurred while retrieving lottery list: Internal service error");
        }
    }

    @PostMapping("/admin/lotteries")
    public ResponseEntity<LotteryResponseDto> createLottery(@Validated @RequestBody LotteryRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errorMessage.append(fieldError.getDefaultMessage()).append("; ");
            }
            throw new ValidationException(errorMessage.toString());
        }

        try {
            LotteryResponseDto response = lotteryService.createLottery(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception exception) {
            throw new InternalServiceException("An unexpected error occurred while processing your request");
        }
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleValidationException(ValidationException ex) {
        String[] errorMessages = ex.getMessage().split("; ");
        StringBuilder errorMessage = new StringBuilder();
        for (String errorMessageLine : errorMessages) {
            errorMessage.append(errorMessageLine).append("\n");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage.toString());
    }
}
