import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LibrarySystemGUI {
	private JFrame frame;
	private JLabel statusLabel;
	private Admin admin;
	private Student student;
	private Library library;
	private DefaultListModel<String> bookListModel;
	private JList<String> bookList;
	private DefaultListModel<String> requestedBooksModel;
	private JList<String> requestedBooksList;
	private JTextField searchField;
	private JTextField usernameField;
	private DefaultListModel<String> searchResultsModel;
	private JList<String> searchResultsList;
	private String username;
	private JPanel mainPanel;
	private JPasswordField passwordField;

	public LibrarySystemGUI(Admin admin, Student student, Library library) {
		this.admin = admin;
		this.student = student;
		this.library = library;
		this.bookListModel = new DefaultListModel<>();
		this.requestedBooksModel = new DefaultListModel<>();
		this.searchResultsModel = new DefaultListModel<>();
		passwordField = new JPasswordField();

		frame = new JFrame("Library Management System");
		frame.setSize(400, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainPanel = new JPanel();
		placeComponents(mainPanel);
		frame.add(mainPanel);

		frame.setVisible(true);

		JLabel welcomeLabel = new JLabel("Welcome to Library Management System");
		welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
		welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
		mainPanel.add(welcomeLabel); // Use mainPanel to add components
		addComponents(mainPanel);

		frame.setVisible(false);
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}

	

	private void addComponents(JPanel panel) {

		JButton loginButton = new JButton("Login");
		loginButton.setBounds(10, 80, 80, 25);
		panel.add(loginButton);
		
		loginButton.setBackground(new Color(30, 144, 255)); // Dodger Blue
		loginButton.setForeground(Color.WHITE);

		// New: Customize the status label appearance
		statusLabel = new JLabel("");
		statusLabel.setHorizontalAlignment(JLabel.CENTER);
		statusLabel.setForeground(Color.RED); // Red text color
		panel.add(statusLabel);

		/*
		 * // New: Add an image (replace "path/to/your/image.jpg" with the actual image
		 * path) ImageIcon libraryIcon = new ImageIcon(
		 * "C:\\Users\\User\\Downloads\\American-University-of-Phnom-Penh-AUPP.jpg");
		 * 
		 * JLabel imageLabel = new JLabel(libraryIcon); panel.add(imageLabel);
		 */
		panel.setBackground(Color.CYAN);

	}

	private void placeComponents(JPanel panel) {
		panel.setLayout(null);

		JLabel userLabel = new JLabel("Username:");
		userLabel.setBounds(10, 20, 80, 25);
		panel.add(userLabel);

		usernameField = new JTextField(20);
		usernameField.setBounds(100, 20, 165, 25);
		panel.add(usernameField); 

		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setBounds(10, 50, 80, 25);
		panel.add(passwordLabel);

		passwordField = new JPasswordField(20);
		passwordField.setBounds(100, 50, 165, 25);
		panel.add(passwordField);

		JButton loginButton = new JButton("Login");
		loginButton.setBounds(10, 80, 80, 25);
		panel.add(loginButton);

		statusLabel = new JLabel("");
		statusLabel.setBounds(10, 110, 300, 25);
		panel.add(statusLabel);

		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				username = usernameField.getText();
				String password = new String(passwordField.getPassword());

				User currentUser = null;
				if (admin.login(username, password, "1")) {
					currentUser = admin;
				} else if (student.login(username, password, "2")) {
					currentUser = student;
				} else {
					statusLabel.setText("Invalid username or password.");
					return;
				}

				if (currentUser instanceof Admin) {
					showAdminUI();
				} else if (currentUser instanceof Student) {
					showStudentUI();
				}
			}
		});
	}

	private void showAdminUI() {

		library.loadBooksFromDatabase();

		JFrame adminFrame = new JFrame("Admin Interface");
		adminFrame.setSize(400, 300);

		JPanel panel = new JPanel();
		placeAdminComponents(panel);
		adminFrame.add(panel);
		panel.setBackground(Color.CYAN);

		java.util.List<Book> loadedBooks = library.getBooks();
		for (Book book : loadedBooks) {
			bookListModel.addElement(book.getTitle());
		}
		bookList.setModel(bookListModel);

		adminFrame.setVisible(true);
		frame.setVisible(false);
	}

	private void placeAdminComponents(JPanel panel) {
		panel.setLayout(null);

		bookList = new JList<>(bookListModel);
		JScrollPane scrollPane = new JScrollPane(bookList);
		scrollPane.setBounds(150, 20, 200, 150);
		panel.add(scrollPane);

		JButton addBookButton = new JButton("Add Book");
		addBookButton.setBounds(10, 180, 120, 25);
		panel.add(addBookButton);

		addBookButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showAddBookDialog();
			}
		});
	}

	private void showAddBookDialog() {
		JFrame addBookFrame = new JFrame("Add Book");
		addBookFrame.setSize(300, 200);

		JPanel panel = new JPanel();
		placeAddBookComponents(panel);
		addBookFrame.add(panel);

		addBookFrame.setVisible(true);
	}

	private void placeAddBookComponents(JPanel panel) {
		panel.setLayout(new GridLayout(4, 2));

		JLabel titleLabel = new JLabel("Book Title:");
		JTextField titleField = new JTextField();
		JLabel authorLabel = new JLabel("Author:");
		JTextField authorField = new JTextField();

	

		JButton addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String title = titleField.getText();
				String author = authorField.getText();

				
				if (title.isEmpty() || author.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please fill in all fields.");
					return;
				}

				// Create a new book
				Book newBook = new Book(library.getNextBookId(), title, author);

				// Add the book to the library
				library.addBook(newBook);
				library.addBookToDatabase(newBook);

				// Update the book list model
				bookListModel.addElement(title);

				// Refresh the book list display
				bookList.setModel(bookListModel);

				JOptionPane.showMessageDialog(null, "success!");
			}
		});

		panel.add(titleLabel);
		panel.add(titleField);
		panel.add(authorLabel);
		panel.add(authorField);
		panel.add(new JLabel());
		panel.add(addButton);
	}

	private void showStudentUI() {
		JFrame studentFrame = new JFrame("Student Interface");
		studentFrame.setSize(400, 300);

		JPanel panel = new JPanel();
		placeStudentComponents(panel);
		studentFrame.add(panel);
		panel.setBackground(Color.YELLOW);

		studentFrame.setVisible(true);
		frame.setVisible(false); 

		List<String> requestedBooks = library.getAllRequestedBooks(username);
		requestedBooksModel.clear();
		requestedBooksModel.addAll(requestedBooks);
		requestedBooksList.setModel(requestedBooksModel);

	}

	private void placeStudentComponents(JPanel panel) {
		panel.setLayout(null);

		// Add a search field and button
		searchField = new JTextField();
		searchField.setBounds(10, 210, 150, 25);
		panel.add(searchField);

		JButton searchButton = new JButton("Search");
		searchButton.setBounds(170, 210, 80, 25);
		panel.add(searchButton);

		requestedBooksList = new JList<>(requestedBooksModel);
		JScrollPane requestedBooksScrollPane = new JScrollPane(requestedBooksList);
		requestedBooksScrollPane.setBounds(10, 240, 350, 80);
		panel.add(requestedBooksScrollPane);

		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String query = searchField.getText();

				
				if (query.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please enter a search query.");
					return;
				}

				// Search for books in the library
				List<Book> searchResults = library.searchBooks(query);

				// Display search results in a dialog
				SearchResultDialog searchResultDialog = new SearchResultDialog(searchResults);
				searchResultDialog.showDialog();
			}
		});
		JOptionPane.showMessageDialog(null, "Book requested successfully!");
	}

	private class SearchResultDialog {
		private JFrame searchFrame;
		private JList<String> searchResultsList;

		public SearchResultDialog(List<Book> searchResults) {
			searchFrame = new JFrame("Search Results");
			searchFrame.setSize(300, 200);

			JPanel panel = new JPanel();
			placeSearchComponents(panel, searchResults);
			searchFrame.add(panel);
		}

		public void showDialog() {
			searchFrame.setVisible(true);
		}

		private void placeSearchComponents(JPanel panel, List<Book> searchResults) {
			panel.setLayout(new BorderLayout());

			JLabel resultLabel = new JLabel("Search Results:");

			DefaultListModel<String> searchResultsModel = new DefaultListModel<>();
			for (Book book : searchResults) {
				searchResultsModel.addElement(book.getTitle());
			}
			searchResultsList = new JList<>(searchResultsModel);
			JScrollPane searchResultsScrollPane = new JScrollPane(searchResultsList);

			JButton requestButton = new JButton("Request Book");
			requestButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String selectedBook = searchResultsList.getSelectedValue();

					// Check if the book is already requested
					if (requestedBooksModel.contains(selectedBook)) {
						JOptionPane.showMessageDialog(null, "Book already requested.");
					} else {
						// Add the book to the requested books list
						requestedBooksModel.addElement(selectedBook);

						// Request the book and store in the database
						library.requestBook(selectedBook, username);

						JOptionPane.showMessageDialog(null, "Book requested successfully!");
					}
				}
			});

			panel.add(resultLabel, BorderLayout.NORTH);
			panel.add(searchResultsScrollPane, BorderLayout.CENTER);
			panel.add(requestButton, BorderLayout.SOUTH);
		}
	}

	public void requestBook(String title) throws SQLException {
		if (title.isEmpty()) {
			throw new IllegalArgumentException("Title cannot be empty.");
		}

		try (Connection connection = Database.getConnection()) {
			boolean autoCommit = connection.getAutoCommit();

			if (autoCommit) {
				connection.setAutoCommit(false);
			}

			try {
				String sql = "INSERT INTO requested_books (book_title) VALUES (?)";
				try (PreparedStatement statement = connection.prepareStatement(sql)) {
					statement.setString(1, title);
					statement.executeUpdate();
				}

				if (!autoCommit) {
					connection.commit();
				}

				// Add the book to the UI immediately after inserting into the database
				requestedBooksModel.addElement(title);
			} catch (SQLException e) {
				connection.rollback();
				e.printStackTrace();
				throw e;
			} finally {
				if (!autoCommit) {
					connection.setAutoCommit(true);
				}
			}
		}

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Admin admin = new Admin("admin", "admin123", "admin");
			Student student = new Student("student", "student123", "student");
			Library library = new Library();

			LibrarySystemGUI librarySystemGUI = new LibrarySystemGUI(admin, student, library);

			JFrame mainFrame = new JFrame("Library System");
			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainFrame.getContentPane().add(librarySystemGUI.getMainPanel());
			mainFrame.pack();
			mainFrame.setLocationRelativeTo(null);
			mainFrame.setVisible(true);
		});
	}
}
