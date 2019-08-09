import java.util.Random;

public class StorageClerks implements Runnable{
	public long time = System.currentTimeMillis();

	String name;
	Random random = new Random();
	int temp= 0,count = 0;
	public StorageClerks(int id){
		setName("StorageClerks-"+id);

	}

	private void setName(String id) {
		this.name = id;

	}

	@Override
	public void run() {
		int heavy = random.nextInt(9)+1;
		born();
		waitingForCustomer();

		if(heavy > 8){
			while(!Main.waitingCalled.isEmpty()){
				try{
					needTwo();
				}catch(Exception e){
					//waitingForCustomer();
					waitingForStorage();
					//while(Main.availableSClerk.isEmpty()){};
				}
			}
		}else{

			while(Main.hasItem != Main.nCustomers){
				try{
					needOne();
					//count++;
				}catch(Exception e){
					waitingForStorage();
					//waitingForCustomer();
					//	System.out.println("eske");
					//Thread.yield();
				}	
			}
		}

		breakOut();
		sleep();
		closing();


	}//run


	private void closing() {
		msg("store closing,leaving");
		
	}

	private void waitingForStorage() {
		
		while(Main.availableSClerk.isEmpty()){};
	}

	private void breakOut() {
		Main.error = true;

	}

	private void sleep() {
	
		while(Main.assistedsCus.size() != Main.nCustomers) {};

		msg("finished going to long slept");
	//	Main.sClerkLeave.addElement(this);
	
	//	try {
		//	Thread.sleep(99999999);
	//	} catch (InterruptedException e) {
			
			msg("sleep interrupted,store closing");
			
	//	}

	}

	//	}//while
	//	}

	private void waitingForCustomer() {
		msg("waiting for customer");
		while(Main.waitingCalled.isEmpty()){
			Thread.yield();
			if(Main.error == true) break;
		};
	}

	private void needOne() {
		Customers customer = null;
		try{
			customer = Main.waitingCalled.remove(0);		
		}catch(Exception e){
			
		}

		temp = random.nextInt(500);
		msg("helping " + customer.getName());
		StorageClerks clerk = Main.availableSClerk.remove(0);
		try {
			Thread.sleep(temp);
			msg("finished helping " + customer.getName());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Main.ticCalled[(customer.getId()) - 1] = true;
		Main.hasItem++;
		//Main.hasItem[(customer.getId()) -1] = true;
		Main.availableSClerk.add(clerk);

	}

	private void needTwo() {
		msg("item is heavy need two people");
		Main.availableSClerk.remove(this);
		StorageClerks sClerk1 = Main.availableSClerk.remove(0);

		sClerk1.msg("helping " + getName());
		Customers customer = null;
		try{
			customer = Main.waitingCalled.remove(0);
		}catch(Exception e){
		//	waitingForCustomer();
		}
		int temp = random.nextInt(500);
		msg("bringing item to "+ customer.getName());
		sClerk1.msg("bringing item to "+ customer.getName());
		try{
			Thread.sleep(temp);
		}catch(Exception e){
			e.printStackTrace();
		}

		Main.ticCalled[(customer.getId()) - 1] = true;
		Main.hasItem++;
		Main.availableSClerk.addElement(this);
		Main.availableSClerk.addElement(sClerk1);
	}

	private void born() {
		msg("arrives");
		Main.availableSClerk.addElement(this);
	}

	public void msg(String m) {
		System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
	}

	private String getName() {

		return name;
	}
}
