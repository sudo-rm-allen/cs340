import java.util.Arrays;
import java.util.Vector;

public class Main {

	static int nCustomers,nFloorClerks,nStorageClerks;
	static Vector<Customers> assistedfCus = new Vector<Customers>();
	static Vector<Customers> assistedsCus = new Vector<Customers>();
	static Vector<Customers> cusInStore = new Vector<Customers>(); 
	static Vector<Customers> needASlip = new Vector<Customers>();
	static Vector<Customers> waitingCalled = new Vector<Customers>();
	static Vector<FloorClerks> availablefClerk = new Vector<FloorClerks>();
	static Vector<StorageClerks> availableSClerk = new Vector<StorageClerks>(); 
	static Vector<FloorClerks> fClerkLeave = new Vector<FloorClerks>();
	static Vector<StorageClerks> sClerkLeave = new Vector<StorageClerks>();
	static Boolean[] gotHelp,ticCalled;
	static int id = 1;
	static boolean error = false,sClerkSleep = false;
	static volatile int ticket = 0,hasItem = 0,custLeave,sleepingSclerk = 0;
	static ThreadGroup sGroup,fGroup;
	static Thread t1,t2,t3;

	
	public static void main(String[] args) {
		try{
			nCustomers = Integer.parseInt(args[0]);
		}catch(Exception e){
			System.err.println("Unable to read:" + args[0]);
			e.printStackTrace();
		}try{
			nFloorClerks = Integer.parseInt(args[1]);
		}catch(Exception e){
			System.err.println("Unable to read:" + args[1]);
			e.printStackTrace();
		}try{
			nStorageClerks = Integer.parseInt(args[2]);
		}catch(Exception e){
			System.err.println("Unable to read:" + args[2]);
			e.printStackTrace();
		}

		gotHelp = new Boolean[nCustomers];
		ticCalled = new Boolean[nCustomers];
		Arrays.fill(gotHelp,Boolean.FALSE);
		Arrays.fill(ticCalled,Boolean.FALSE);
		
		//Nth customer for when they leave
		custLeave = nCustomers;
		
		
		//sGroup = new ThreadGroup("fGroup");
		
		/*
		 * for floor clerk
		 */
		for(int j = 0; j < nFloorClerks; j ++){

			FloorClerks fclerks = new FloorClerks(id++);
			t1 = new Thread(fGroup,fclerks);
			
			t1.start();
			
		}

		id = 1;
	
		//ThreadGroup sGroup = null;
		/*
		 * for customer group
		 */
		for(int i = 0; i < nCustomers; i++){
			Customers customer = new Customers(id++);
			
			t2 = new Thread(customer);
			t2.start();
			
		}

		id = 1;
		//sGroup = new ThreadGroup("sGroup");
		
		/*
		 * for storage clerks thread
		 */
		for(int k = 0; k < nStorageClerks;k++){
			StorageClerks sClerk = new StorageClerks(id++);
			t3 = new Thread(sGroup,sClerk);
			t3.start();
			
		}

		
	}

	/**
	 * makes a n-customer thread waits for this to finish
	 * 
	 * @param cus gets a customer from customer
	 */
	static public void joinED(Customers cus){

			Thread t4 = new Thread(cus);
			try {
				t4.join(); 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			custLeave--;

	}
}
