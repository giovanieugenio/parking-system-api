package com.park.api.web.dto.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import com.park.api.entities.User;
import com.park.api.web.dto.UserCreateDTO;
import com.park.api.web.dto.UserResponseDTO;

public class UserMapper {

	public static User toUser(UserCreateDTO userDTO) {
		return new ModelMapper().map(userDTO, User.class);
	}
	
	public static UserResponseDTO toDTO(User user) {
		String role = user.getRole().name().substring("ROLE_".length());
		PropertyMap<User, UserResponseDTO> props = new PropertyMap<User, UserResponseDTO>() {
			@Override
			protected void configure() {
				map().setRole(role);
			}
		};
		ModelMapper mapper = new ModelMapper();
		mapper.addMappings(props);
		return mapper.map(user, UserResponseDTO.class);
	}
}