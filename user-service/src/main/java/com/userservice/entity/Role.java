package com.userservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("roles")
public class Role {

    private String id;

    private ERole name;

    public Role(ERole name) {
        this.name = name;
    }
}
