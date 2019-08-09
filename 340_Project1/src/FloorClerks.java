import java.util.Random;

public class FloorClerks implements Runnable {
	public static long time = System.currentTimeMillis();
	String name;
	Main m = new Main();
	Random random = new Random();
	int temp = 0, count = 0;


	public FloorClerks(int id){
		setName("FloorClerks-"+id);

	}

	@Override
	public void run() {
		born();
		waitingForCustomer();

		while(!Main.needASlip.isEmpty() && count != Main.nCustomers){
			try{
				helpCustomer();
				count++;

			}catch(Exception e){
			
				waitingForFloor();
			}
		}

		slept();	

		leave();



	}

	private void leave() {
		msg("store closing,leaving");
	}

	private void waitingForFloor() {

		while(Main.assistedfCus.isEmpty()){};
	}

	private void slept() {
		while(Main.assistedfCus.size() != Main.nCustomers) {Thread.yield();};
		msg("finished helping customers going to long sleep");
		Main.fClerkLeave.addElement(this);

		/*
		 * couldnt get to make all floor clerks to leave
		 */
		//		try {
//			Thread.sleep(99999999);
//		} catch (InterruptedException e) {
			msg("sleep Interrupted");
//		}	


	}

	public void helpCustomer() {
		Customers customer = null;
		try{
			customer = Main.needASlip.remove(0);		
		}catch(Exception e){
			waitingForCustomer();
		}
		temp = random.nextInt(500);
		msg("helping " + customer.getName());

		FloorClerks clerk = Main.availablefClerk.remove(0);
		try {
			Thread.sleep(temp);

			msg("finished helping " + customer.getName());

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Main.gotHelp[(customer.getId()) - 1] = true;

		Main.availablefClerk.addElement(clerk);

	}
	public void born() {
		msg("arrives");
		Main.availablefClerk.addElement(this);
	}
	public void waitingForCustomer(){
		msg("is Now Busy Waiting for customers");
		while(Main.needASlip.isEmpty()){};
	}
	public void setName(String id){
		this.name = id;
	}
	public void msg(String m) {
		System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
	}
	private String getName() {
		return name;
	}
}
