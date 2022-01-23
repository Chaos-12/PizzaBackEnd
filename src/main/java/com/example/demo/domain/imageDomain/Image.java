package com.example.demo.domain.imageDomain;

import javax.validation.constraints.NotEmpty;

import com.example.demo.core.EntityBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class Image extends EntityBase {
    @NotEmpty
    private byte[] content;
}
