package com.javaprojects.workshopmongo.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaprojects.workshopmongo.domain.Post;
import com.javaprojects.workshopmongo.repository.PostRepository;
import com.javaprojects.workshopmongo.services.exception.ObjectNotFoundException;

@Service
public class PostService {

	@Autowired
	private PostRepository repo;
	
	public Post findById(String id) {
		Optional<Post> post = repo.findById(id);
		
		if (post.isEmpty()) {
			throw new ObjectNotFoundException("Objeto não encontrado");
		}
		
		return post.get();
		
	}
	
	public List<Post> findByTitle(String text) {
		return repo.searchTitle(text);
	}
	
	public List<Post> fullSearch(String text, Date minDate, Date maxDate) {
		 maxDate = new Date(maxDate.getTime() +	24 * 60 * 60 * 1000);
		 
		 return repo.fullSearch(text, minDate, maxDate);
	}
}
