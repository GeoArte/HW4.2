package com.skypro.springintegrationtesting.service;

import com.skypro.springintegrationtesting.dto.UserDTO;
import com.skypro.springintegrationtesting.exception.UserNotFoundException;
import com.skypro.springintegrationtesting.model.User;
import com.skypro.springintegrationtesting.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;


  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<UserDTO> getAllUsers() {
    return userRepository.findAll().stream().map(UserDTO::toDTO).collect(Collectors.toList());
  }

  public UserDTO addUser(UserDTO user) {
    User userEntity = new User();
    userEntity.setName(user.getName());
    return UserDTO.toDTO(userRepository.save(userEntity));
  }

  public UserDTO editUser(long id, UserDTO userDTO) {
    User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    user.setName(userDTO.getName());
    return UserDTO.toDTO(userRepository.save(user));
  }

  public void deleteUser(long id) {
    User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    userRepository.delete(user);
  }
}
