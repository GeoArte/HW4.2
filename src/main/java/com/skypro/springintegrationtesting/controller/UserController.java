package com.skypro.springintegrationtesting.controller;

import com.skypro.springintegrationtesting.dto.UserDTO;
import com.skypro.springintegrationtesting.exception.UserNotFoundException;
import com.skypro.springintegrationtesting.service.UserService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<?> handleError(UserNotFoundException e) {
    return ResponseEntity.notFound().build();
  }

  @GetMapping
  public List<UserDTO> users() {
    return userService.getAllUsers();
  }

  @PostMapping
  public UserDTO addUser(@RequestBody UserDTO user) {
    return userService.addUser(user);
  }

  @PutMapping("/{id}")
  public UserDTO changeUser(@PathVariable("id") long id, @RequestBody UserDTO userDTO) {
    return userService.editUser(id, userDTO);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}
