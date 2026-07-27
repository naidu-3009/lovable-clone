package com.projectlove.lovable_clone.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Project {
    Long id;
    String name;
    User owner;
    Boolean isPublic;

    Instant createdAt;
    Instant updatedAt;
    Instant deletedAt;


}
