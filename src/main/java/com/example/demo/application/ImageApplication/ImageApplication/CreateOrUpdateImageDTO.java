package com.example.demo.application.ImageApplication.ImageApplication;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateOrUpdateImageDTO {
    @NotEmpty
    public byte[] content;
}
