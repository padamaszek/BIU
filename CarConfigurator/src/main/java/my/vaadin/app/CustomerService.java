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

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.TreeTable;

/**
 * An in memory dummy "database" for the example purposes. In a typical Java app
 * this class would be replaced by e.g. EJB or a Spring based service class.
 * <p>
 * In demos/tutorials/examples, get a reference to this service class with
 * {@link CustomerService#getInstance()}.
 */
public class CustomerService implements Cloneable {

	private static CustomerService instance;
	private static final Logger LOGGER = Logger.getLogger(CustomerService.class.getName());

	private ArrayList<Customer> contacts = new ArrayList<>();
	static ArrayList<Customer> car = new ArrayList<>();
	private CustomerStatus custStat;
	private String name;
	private long nextId = 0;
	int i = 0;

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
		for (Customer contact : contacts) {
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

	public synchronized List<Customer> findAllLogged() {
		ArrayList<Customer> arrayList = new ArrayList<>();
		for (Customer contact : contacts) {
			arrayList.add(contact);
		}
		return arrayList;
	}

	public synchronized List<Customer> findAllItems(String stringFilter) {
		ArrayList<Customer> arrayList = new ArrayList<>();
		for (Customer contact : contacts) {
			if (contact.getStatus() == CustomerStatus.Car)
				arrayList.add(contact);
		}
		MyUI.label.setValue("Car Configurator");
		return arrayList;
	}

	public synchronized List<Customer> getOrder() {
		ArrayList<Customer> arrayList = new ArrayList<>();
		for (Customer contact : car) {
			System.out.println(contact.getName());
			arrayList.add(contact);
		}
		return arrayList;
	}

	// --------------------------------------------
	public synchronized List<Customer> getBack() {
		ArrayList<Customer> arrayList = new ArrayList<>();
		if (custStat != null) {
			switch (custStat) {
			case Engine:
				for (Customer contact : contacts) {
					if (contact.getStatus() == CustomerStatus.Engine) {
						arrayList.add(contact);
					}
				}
				custStat = CustomerStatus.Version;
				car.remove(car.size() - 1);
				MyUI.label.setValue("Select Engine:");
				break;

			case Version:
				for (Customer contact : contacts) {
					if (contact.getStatus() == CustomerStatus.Version && contact.getName().equals(name)) {
						arrayList.add(contact);
					}
				}
				custStat = CustomerStatus.Model;
				car.remove(car.size() - 1);
				MyUI.label.setValue("Select Version:");
				break;

			case Model:
				for (Customer contact : contacts) {
					if (contact.getStatus() == CustomerStatus.Model && contact.getName().equals(name)) {
						arrayList.add(contact);
					}
				}
				custStat = CustomerStatus.Car;
				car.remove(car.size() - 1);
				MyUI.label.setValue("Select Model:");
				break;
			case Car:
				for (Customer contact : contacts) {
					if (contact.getStatus() == CustomerStatus.Car) {
						arrayList.add(contact);
					}
				}
				i = 0;
				custStat = null;
				car.remove(car.size() - 1);
				MyUI.label.setValue("Select Car:");
				break;
			}
		} else {
			for (Customer contact : contacts) {
				if (contact.getStatus() == CustomerStatus.Car) {
					arrayList.add(contact);
				}
			}
			MyUI.label.setValue("Select Car:");
		}
		return arrayList;
	}

	public synchronized List<Customer> getCar(Customer stringFilter) {
		ArrayList<Customer> arrayList = new ArrayList<>();
		if (i == 0) {
			name = stringFilter.getName();
			i++;
		}
		custStat = stringFilter.getStatus();

		car.add(stringFilter);
		if (stringFilter.getStatus() == CustomerStatus.Engine) {
			for (Customer contact : car) {
				arrayList.add(contact);
			}
			MyUI.label.setValue("Your Order:");
		}

		for (Customer contact : contacts) {
			switch (stringFilter.getStatus()) {
			case Car:
				if (contact.getName().equals(stringFilter.getName()) && contact.getStatus() == CustomerStatus.Model) {
					arrayList.add(contact);
				}
				MyUI.label.setValue("Select Model:");
				break;
			case Model:
				if (contact.getName().equals(stringFilter.getName()) && contact.getStatus() == CustomerStatus.Version) {
					arrayList.add(contact);

				}
				MyUI.label.setValue("Select Version:");
				break;

			case Version:
				if (contact.getStatus() == CustomerStatus.Engine) {
					arrayList.add(contact);
				}
				MyUI.label.setValue("Select Engine:");
				break;
			}
		}

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
		for (Customer contact : contacts) {
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
		contacts.remove(value);
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
		contacts.add(entry);
	}

	/**
	 * Sample data generation
	 */
	public void ensureTestData() {
		if (findAll().isEmpty()) {

			final String[] models = new String[] { "Audi A3", "Audi A4", "Audi A6", "BMW 1", "BMW 5", "BMW 3",
					"Ford Fiesta", "Ford Focus", "Ford Mustang", "Honda Civic", "Honda Accord", "Mercedes C",
					"Mercedes E", "Mercedes SLK", "Seat Ibiza", "Seat Leon", "Seat Toledo", "Skoda Octavia",
					"Skoda Superb", "Skoda Fabia" };
			final String[] cars = new String[] { "Audi", "BMW", "Ford", "Honda", "Mercedes", "Seat", "Skoda" };

			final String[] engines = new String[] { "Petrol 1.4", "Petrol 1.6", "Petrol 1.8", "Petrol 2.0",
					"Petrol 3.2", "Diesel 1.7", "Diesel 1.9", "Diesel 2.0" };
			final String[] version = new String[] { "Audi Sportback", "Audi Cabriolet", "Audi Limousine", "BMW Touring",
					"BMW Limousine", "BMW GranTurismo", "Ford ST", "Ford Hatchback", "Ford RS", "Honda Hatchback",
					"Honda Kombi", "Mercedes ShootingBreak", "Mercedes AMG", "Mercedes Coupe", "Seat Cupra",
					"Seat Hatchback5d", "Seat Hatchback3d", "Skoda Liftback", "Skoda Scout", "Skoda Sedan" };

			Random r = new Random(0);
			for (String name : cars) {
				String[] split = name.split(" ");
				Customer c = new Customer();
				c.setName(split[0]);
				c.setModel("");
				c.setStatus(CustomerStatus.Car);
				
				c.setYear(r.nextInt(2016 - 1900 + 1) + 1900);
				Calendar cal = Calendar.getInstance();
				int daysOld = 0 - r.nextInt(365 * 15 + 365 * 60);
				cal.add(Calendar.DAY_OF_MONTH, daysOld);
				c.setBirthDate(cal.getTime());
				save(c);
			}
			for (String name : models) {
				String[] split = name.split(" ");
				Customer c = new Customer();
				c.setName(split[0]);
				c.setModel(split[1]);
				c.setStatus(CustomerStatus.Model);
				c.setYear(2016);
				Calendar cal = Calendar.getInstance();
				int daysOld = 0 - r.nextInt(365 * 15 + 365 * 60);
				cal.add(Calendar.DAY_OF_MONTH, daysOld);
				c.setBirthDate(cal.getTime());
				save(c);
			}
			for (String name : engines) {
				String[] split = name.split(" ");
				Customer c = new Customer();
				c.setName(split[0]);
				c.setModel(split[1]);
				c.setStatus(CustomerStatus.Engine);
				Calendar cal = Calendar.getInstance();
				int daysOld = 0 - r.nextInt(365 * 15 + 365 * 60);
				cal.add(Calendar.DAY_OF_MONTH, daysOld);
				c.setBirthDate(cal.getTime());
				save(c);
			}
			for (String name : version) {
				String[] split = name.split(" ");
				Customer c = new Customer();
				c.setName(split[0]);
				c.setModel(split[1]);
				c.setStatus(CustomerStatus.Version);
				Calendar cal = Calendar.getInstance();
				int daysOld = 0 - r.nextInt(365 * 15 + 365 * 60);
				cal.add(Calendar.DAY_OF_MONTH, daysOld);
				c.setBirthDate(cal.getTime());

				save(c);
			}
		}
	}

}
