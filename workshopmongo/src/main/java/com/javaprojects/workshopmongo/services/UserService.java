package com.javaprojects.workshopmongo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaprojects.workshopmongo.domain.User;
import com.javaprojects.workshopmongo.dto.UserDTO;
import com.javaprojects.workshopmongo.repository.UserRepository;
import com.javaprojects.workshopmongo.services.exception.ObjectNotFoundException;

@Service
public class UserService {
	
	@Autowired
	private UserRepository repo;
	
	public List<User> findAll() {
		return repo.findAll();
	}
	
	public User findById(String id) {
	    Optional<User> user = repo.findById(id);

	    if (user.isEmpty()) {
	        throw new ObjectNotFoundException("Objeto não encontrado");
	    }

	    return user.get();
	}
	
	public User insert(User obj) {
		return repo.insert(obj);
	}
	
	public void delete(String id) {
		findById(id);
		repo.deleteById(id);
	}
	
	public User fromDTO(UserDTO objDto) {
		return new User(objDto.getId(), objDto.getName(), objDto.getEmail());

	}
}
