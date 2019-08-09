import java.util.Random;

public class Customers implements Runnable {
	public static long time = System.currentTimeMillis();
	Random random = new Random();
	String name;
	int temp = 0 , id = 0;
	Main m = new Main();
	boolean fClerkSignal = false;
	boolean hasTicket = false;
	int ticket = 0;
	public Customers(int id){
		setName("Customers-"+id);
		this.id = id;
	}
	private void setName(String id) {
		this.name = id;
	}
	public String getName(){
		return name;
	}
	@Override
	public void run() {
		int itemWieght = random.nextInt(9) + 1;
		born();
		waitsInLine();
		goesToCashier();
		if(itemWieght > 6){
			pickUpItem60();
			arriveAtStorage();
		}else{
			pickUpItem();
		}
		waitToLeave();
		leaving();
		if(getId() == 1){
			msg("interrupting clerks");
		//	Main.t3.interrupt();
		//	Main.fGroup.interrupt();
			//System.out.println(Main.fGroup.getName());
		//	Main.sGroup.interrupt();

		}else{
			left();
		}

	}
	private void left() {

		msg("left");
	}
	private void leaving() {

		msg("about to leave");
		Main.joinED(this);

	}
	private void waitToLeave() {

		msg("finished shopping waiting for turn to leave");

		while(getId() != Main.custLeave){};

	}
	
	private void arriveAtStorage() {
		msg("arrived at storage");
		increment();
		this.ticket = Main.ticket;
		msg("my ticket is " + getTicket());
		msg("waiting to be called");	
		Main.waitingCalled.addElement(this);
		while(!Main.ticCalled[getId() - 1]){Thread.yield(); };
		Main.assistedsCus.addElement(this);
		msg("got my item");
	}
	public int getTicket(){
		return ticket;
	}
	private void pickUpItem() {
		Main.hasItem++;
		Main.assistedsCus.addElement(this);
		//	Main.hasItem[getId()-1] = true;
		msg("waiting for group");
		//	System.out.println(Main.hasItem);
	}


	private void pickUpItem60() {
		temp = random.nextInt(500);
		msg("taking a break,eating");
		Thread.yield();
		Thread.yield();
		try {
			Thread.sleep(temp);
			msg("finished eatting going to storage");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private synchronized void increment(){
		++Main.ticket;

	}

	public void goesToCashier() {
		while(!Main.gotHelp[id-1]){Thread.yield();};
		Main.assistedfCus.addElement(this);
		msg("rushing to cashier");
		temp = random.nextInt(4) + 6;
		Main.t2.setPriority(temp);
		temp = random.nextInt(500);
		try {
			msg("paying for item");
			Thread.sleep(temp);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Main.t2.setPriority(Thread.NORM_PRIORITY);
	}


	private void waitsInLine() {

		Main.needASlip.addElement(this);
		while(Main.availablefClerk.isEmpty()){};

	}

	private void born(){
		temp = random.nextInt(500);
		Main.cusInStore.addElement(this);
		try {
			Thread.sleep(temp);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		msg("walks in and is browsing store");
		try {
			Thread.sleep(temp);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		msg("found an item going to floor clerk, waiting in line");
	}
	public void msg(String m) {
		System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
	}
	public int getId(){
		return id;
	}
	public void sethasTicket(boolean b) {
		this.hasTicket = b;
	}
}