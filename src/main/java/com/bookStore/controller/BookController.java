package com.bookStore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.bookStore.model.Book;
import com.bookStore.model.MyBookList;
import com.bookStore.service.BookService;
import com.bookStore.service.MyBookListService;

import java.util.*;

@Controller
public class BookController {

	@Autowired
	private BookService service;

	@Autowired
	private MyBookListService myBookService;

	// Handling the root URL mapping
	@GetMapping("/")
	public String home() {
		return "home"; // Returns the view name "home"
	}

	// Handling the "/bookRegister" URL mapping
	@GetMapping("/bookRegister")
	public String bookRegister() {
		return "bookRegister"; // Returns the view name "bookRegister"
	}

	// Handling the "/available_books" URL mapping to display all books
	@GetMapping("/available_books")
	public ModelAndView getAllBook() {
		List<Book> list = service.getAllBook(); // Retrieve a list of all books from the database
		return new ModelAndView("bookList", "book", list); // Returns "bookList" view with the book list as a model attribute
	}

	// Handling the "/save" URL mapping to add a new book
	@PostMapping("/save")
	public String addBook(@ModelAttribute Book b) {
		service.save(b); // Saves the new book details to the database
		return "redirect:/available_books"; // Redirects to the URL mapped for displaying all books
	}

	// Handling the "/my_books" URL mapping to display a user's book list
	@GetMapping("/my_books")
	public String getMyBooks(Model model) {
		List<MyBookList> list = myBookService.getAllMyBooks(); // Retrieves the user's book list
		model.addAttribute("book", list); // Adds the book list to the model
		return "myBooks"; // Returns the view name "myBooks"
	}

	// Handling the "/mylist/{id}" URL mapping to add a book to a user's list
	@RequestMapping("/mylist/{id}")
	public String getMyList(@PathVariable("id") int id) {
		Book b = service.getBookById(id); // Retrieves the book details by ID
		MyBookList mb = new MyBookList(b.getId(), b.getName(), b.getAuthor(), b.getPrice()); // Creates a user-specific book list entry
		myBookService.saveMyBooks(mb); // Saves the book to the user's list
		return "redirect:/my_books"; // Redirects to the user's book list
	}

	// Handling the "/editBook/{id}" URL mapping to edit a book
	@RequestMapping("/editBook/{id}")
	public String editBook(@PathVariable("id") int id, Model model) {
		Book b = service.getBookById(id); // Retrieves the book by ID for editing
		model.addAttribute("book", b); // Adds the book to the model for editing
		return "bookEdit"; // Returns the view name "bookEdit" for editing
	}

	// Handling the "/deleteBook/{id}" URL mapping to delete a book
	@RequestMapping("/deleteBook/{id}")
	public String deleteBook(@PathVariable("id") int id) {
		service.deleteById(id); // Deletes the book by ID from the database
		return "redirect:/available_books"; // Redirects to the URL mapped for displaying all books
	}

	@RequestMapping("/deleteMyList/{id}")
	public String deleteMyList(@PathVariable("id") int id) {
		myBookService.deleteById(id);
		return "redirect:/my_books";
	}

}
