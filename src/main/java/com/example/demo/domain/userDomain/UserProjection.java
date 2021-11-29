package com.example.demo.domain.userDomain;

import java.util.UUID;

public interface UserProjection {
    public UUID getId();
    public String getFirst_name();
    public String getLast_name();
    public String getEmail();
}