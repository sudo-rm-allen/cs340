import java.util.Random;

public class Customers implements Runnable  {
	long time = System.currentTimeMillis();
	Random random = new Random();
	String name;
	int temp = 0 , id = 0;
	public Customers(int id){
		setName("Customers-"+id);
		this.id = id;
	}

	@Override
	public void run() {
		temp = random.nextInt(9) + 1;
		
		//creates a customer thread
		born();
		
		//found an item the customers likes waiting to get a slip
		//from floor clerk
		waitingInLine();
		
		//paying for the item simulated by sleep
		paying();
		
		//70% of the time item is heavy have to go to storage clerk
		if(temp > 3){
			pickUpFromStorage();
		}
		
		//just get the item
		else{
			getItem();
		}
		
		//wait until last customer is done buying
		browsingTillClose();
		
		//if the customer is the last person 
		//signal all the others its time to leave
		if(Main.totalDone == Main.nCustomers){
			lastPerson();
		}
		
		//customers leaving home
		closing();
		
	}

	private void closing() {
		try {
			Main.closingC.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		msg("going home");
	}

	private void lastPerson() {
		msg("i am the last customer its time to go home");
		msg("telling everyone");
		
		for(int i = 0;i<Main.nCustomers;i++){
			Main.closingC.release();
		}
		
		for(int i = 0;i<Main.nFloorClerks;i++){
			Main.closingF.release();
		}
		
		for(int i = 0;i<Main.nStorageClerks;i++){
			Main.closingS.release();
		}
		
	}

	private void browsingTillClose() {
		msg("browsing until closed");
		
		try {
			Main.hasHeavy.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Main.totalDone ++;
		Main.hasHeavy.release();
		
	}

	private void getItem() {
	
		msg("item was not heavy paid and got item");
	}

	private void pickUpFromStorage() {
		temp = random.nextInt(200);
		msg("the item is heavy, walking to storage to pick it up");
		try {
			Thread.sleep(temp);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		msg("arrived at storage");
		
		Main.heavyCus.addElement(this);
		Main.inLine2.release();
		
	}

	private void paying() {
		temp = random.nextInt(500);
		try {
			Thread.sleep(temp);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		msg("got assisted,paying for item");
		temp = random.nextInt(2000);
		try {
			Thread.sleep(temp);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void waitingInLine() {
		Main.cus.addElement(this);
		msg("found an item going to floor clerk, waiting in line");


		Main.inLine.release();
		try {
			Main.fWaitingCus.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	private void born() {
		temp = random.nextInt(2000);
		try {
			Thread.sleep(temp);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		msg("walks in and is browsing store");
		
		temp = random.nextInt(2000);
		try {
			Thread.sleep(temp);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	
	private void setName(String id) {
		this.name = id;
	}

	public void msg(String m) {
		System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
	}

	public String getName(){
		return name;
	}

	
}
