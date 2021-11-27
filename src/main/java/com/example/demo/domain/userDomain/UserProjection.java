package com.example.demo.domain.userDomain;

import java.util.UUID;

public interface UserProjection {
    public UUID getId();
    public String getFirstName();
    public String getLastName();
    public String getEmail();
}