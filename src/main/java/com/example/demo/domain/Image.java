package com.example.demo.domain;

import com.example.demo.core.EntityBase;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Table("Images")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@RedisHash("Images")
public class Image extends EntityBase {

    @Column
    private byte[] content;

}
