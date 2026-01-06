package com.interview.j2ee.mapper;

import com.interview.j2ee.dto.UserDTO;
import com.interview.j2ee.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    List<UserDTO> toDTOList(List<User> users);
}
