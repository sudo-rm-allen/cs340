import java.util.Random;

public class FloorClerks implements Runnable  {
	long time = System.currentTimeMillis();
	String name;
	Random random = new Random();
	int temp = 0, count = 0;
	

	FloorClerks(int id){
		setName("FloorClerks-"+id);
	}


	@Override
	public void run() {
		//creates a new thread
		born();
		
		//waits for the first customer to get a slip
		waitingForCustomer();
		
		//helps the customers until they dont need help anymore
		while(Main.countHelp != Main.nCustomers){
			helping();
		}
		//waits to get signaled by last customer to close
		waitingToClose();
		
		//closing floor clerk going home
		closing();
	}

	private void closing() {
		msg("closing time going home");
	}


	private void waitingToClose() {
		
		msg("finished helping customers waiting for closing");
		try {
			Main.closingF.acquire();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		
		
	}


	private void helping() {
		Customers cus = null;
		try{
			cus = Main.cus.remove(0);		
		}catch(Exception e){
			msg("waiting cus");
		}

		try {
			Main.mutex_fHelp.acquire();

		} catch (InterruptedException e1) {

		}
		Main.countHelp++;
		Main.mutex_fHelp.release();
		try{
			msg("helping customer " + cus.getName());
		}catch(Exception e){
			try {
				Main.inLine.acquire();
			} catch (InterruptedException e1) {
				e.printStackTrace();
			}
		}
		temp = random.nextInt(2000);
		try {
			Thread.sleep(temp);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

		try{
			msg("finished helping" + cus.getName());	
		}catch(Exception e){
			try {
				Main.inLine.acquire();
			} catch (InterruptedException e1) {
				e.printStackTrace();
			}
		}

		Main.fWaitingCus.release();


	}

	private void waitingForCustomer() {
		msg("waiting for customer");
		try {
			Main.inLine.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		msg("customer is here");
	}
	
	public void born(){
		msg("arrives");
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
