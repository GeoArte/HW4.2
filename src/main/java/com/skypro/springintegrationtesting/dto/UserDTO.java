package com.skypro.springintegrationtesting.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.skypro.springintegrationtesting.model.User;

public class UserDTO {

  @JsonProperty(access = Access.READ_ONLY)
  private final Long id;
  private final String name;

  public UserDTO(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public static UserDTO toDTO(User user) {
    return new UserDTO(user.getId(), user.getName());
  }
}
