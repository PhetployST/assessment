package com.kbtg.bootcamp.posttest.lottery.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LotteryRequestDto {

    @NotNull(message = "Lottery ticket is required")
    @Size(min = 6, max = 6, message = "Lottery ticket should be 6 characters")
    @Pattern(regexp = "\\d+", message = "Lottery ticket should be numbers")
    private String ticket;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be a positive number")
    @Min(value = 80, message = "Price should be between 80 and 100")
    @Max(value = 100, message = "Price should be between 80 and 100")
    private double price;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be a positive number")
    @Min(value = 1, message = "increase one lottery at a time")
    @Max(value = 1, message = "increase one lottery at a time")
    private double amount;

}
