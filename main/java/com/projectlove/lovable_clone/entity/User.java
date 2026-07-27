package com.projectlove.lovable_clone.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    Long id;

    String  email;

    String password_hash;
    String name;
    String avatarUrl;

    Instant created_at;
    Instant updated_at;
    Instant deleted_at;


}
