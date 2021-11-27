package com.example.demo.application.imageApplication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateImageDTO {
    @NotEmpty
    public byte[] content;
}
