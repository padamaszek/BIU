package my.vaadin.app;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.ui.TreeTable;

/**
 * An in memory dummy "database" for the example purposes. In a typical Java app
 * this class would be replaced by e.g. EJB or a Spring based service class.
 * <p>
 * In demos/tutorials/examples, get a reference to this service class with
 * {@link CustomerService#getInstance()}.
 */
public class CustomerService {

	private static CustomerService instance;
	private static final Logger LOGGER = Logger.getLogger(CustomerService.class.getName());

	private final HashMap<Long, Customer> contacts = new HashMap<>();
	private long nextId = 0;

	private CustomerService() {
	}

	/**
	 * @return a reference to an example facade for Customer objects.
	 */
	public static CustomerService getInstance() {
		if (instance == null) {
			instance = new CustomerService();
			instance.ensureTestData();
		}
		return instance;
	}

	/**
	 * @return all available Customer objects.
	 */
	public synchronized List<Customer> findAll() {
		return findAll(null);
	}

	/**
	 * Finds all Customer's that match given filter.
	 *
	 * @param stringFilter
	 *            filter that returned objects should match or null/empty string
	 *            if all objects should be returned.
	 * @return list a Customer objects
	 */
	public synchronized List<Customer> findAll(String stringFilter) {
		ArrayList<Customer> arrayList = new ArrayList<>();
		for (Customer contact : contacts.values()) {
			try {
				boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
						|| contact.toString().toLowerCase().contains(stringFilter.toLowerCase());
				if (passesFilter) {
					arrayList.add(contact.clone());
				}
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		Collections.sort(arrayList, new Comparator<Customer>() {

			@Override
			public int compare(Customer o1, Customer o2) {
				return (int) (o2.getId() - o1.getId());
			}
		});
		return arrayList;
	}

	/**
	 * Finds all Customer's that match given filter and limits the resultset.
	 *
	 * @param stringFilter
	 *            filter that returned objects should match or null/empty string
	 *            if all objects should be returned.
	 * @param start
	 *            the index of first result
	 * @param maxresults
	 *            maximum result count
	 * @return list a Customer objects
	 */
	public synchronized List<Customer> findAll(String stringFilter, int start, int maxresults) {
		ArrayList<Customer> arrayList = new ArrayList<>();
		for (Customer contact : contacts.values()) {
			try {
				boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
						|| contact.toString().toLowerCase().contains(stringFilter.toLowerCase());
				if (passesFilter) {
					arrayList.add(contact.clone());
				}
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		Collections.sort(arrayList, new Comparator<Customer>() {

			@Override
			public int compare(Customer o1, Customer o2) {
				return (int) (o2.getId() - o1.getId());
			}
		});
		int end = start + maxresults;
		if (end > arrayList.size()) {
			end = arrayList.size();
		}
		return arrayList.subList(start, end);
	}

	/**
	 * @return the amount of all customers in the system
	 */
	public synchronized long count() {
		return contacts.size();
	}

	/**
	 * Deletes a customer from a system
	 *
	 * @param value
	 *            the Customer to be deleted
	 */
	public synchronized void delete(Customer value) {
		contacts.remove(value.getId());
	}

	/**
	 * Persists or updates customer in the system. Also assigns an identifier
	 * for new Customer instances.
	 *
	 * @param entry
	 */
	public synchronized void save(Customer entry) {
		if (entry == null) {
			LOGGER.log(Level.SEVERE,
					"Customer is null. Are you sure you have connected your form to the application as described in tutorial chapter 7?");
			return;
		}
		if (entry.getId() == null) {
			entry.setId(nextId++);
		}
		try {
			entry = (Customer) entry.clone();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		contacts.put(entry.getId(), entry);
	}

	/**
	 * Sample data generation
	 */
	public void ensureTestData() {
		if (findAll().isEmpty()) {
			final String[] cars = new String[] { "Audi A4", "BMW M3", "Chevrolet Corvette", "Fiat Panda",
					"Ford Mustang", "Honda Civic", "Hyundai Genesis", "Kia Ceed", "Mazda 6", "Mercedes Benz",
					"Mitsubishi Lancer", "Nissan Note", "Opel Astra", "Peugeot 206", "Renault Clio", "Seat Ibiza",
					"Skoda Octavia" };
			final String[] engines = new String[] { "Petrol 1.4", "Petrol 1.6", "Petrol 1.8", "Petrol 2.0",
					"Petrol 3.2", "Diesel 1.7", "Diesel 1.9", "Diesel 2.0" };

			Random r = new Random(0);
			for (String name : cars) {
				String[] split = name.split(" ");
				Customer c = new Customer();
				c.setType("car");
				c.setName(split[0]);
				c.setModel(split[1]);
				c.setStatus(CustomerStatus.values()[r.nextInt(CustomerStatus.values().length)]);
				save(c);
			}
			for (String name : engines) {
				String[] split = name.split(" ");
				Customer c = new Customer();
				c.setType("engine");
				c.setName(split[0]);
				c.setModel(split[1]);
				c.setStatus(CustomerStatus.values()[r.nextInt(CustomerStatus.values().length)]);
				save(c);
			}
		}
	}

}
