package it.uniroma3.siw.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.ReviewRepository;
import jakarta.transaction.Transactional;

@Service
public class ReviewService {
	@Autowired 
	private ReviewRepository reviewRepository;
	
	public boolean existsReviewByBookAndUser(Book b, User u) {
		return reviewRepository.existsByLibroAndUtente(b,u);
	}
	@Transactional
	public void saveReview(Review r) {
		reviewRepository.save(r);
	}
	
	public void deleteReview(Review review) {
		reviewRepository.delete(review);
	}
	
	public Optional<Review> getReviewById(Long id) {
		return reviewRepository.findById(id);
	}
	public Iterable<Review> getAllReviews() {
		return reviewRepository.findAll();
	}
}
