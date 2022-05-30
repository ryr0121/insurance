package Practice.InsuranceCompany.Design.src.customer;


import java.util.Optional;

/**
 * @author macbook
 * @version 1.0
 * @created 21-5-2022 ���� 11:03:48
 */
public interface CustomerList {

	/**
	 * 
	 * @param customer
	 */
	public boolean add(Customer customer);

	/**
	 * 
	 * @param customerID
	 */
	public boolean delete(String customerID);

	/**
     * @param customerID
     */
	public Optional<Customer> getByCustomerId(String customerID);

	/**
	 *
	 * @param customerName
	 */
	public Customer getByCustomerName(String customerName);

	/**
	 * 
	 * @param customerType
	 */
	public Customer get(CustomerType customerType);

}