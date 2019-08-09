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
		//first creates a thread
		born();
		
		//waits until a customer has an heavy item
		waitingForCustomer();
		
		//helps the customers until no more customer needs hel[
		while(Main.totalDone != Main.nCustomers){
			helping();
		}
		
		//waits to get signaled from last customer
		waitingForClose();

		//closing
		closing();

	}//run



	private void closing() {
		msg("closing time going home");
	}

	private void waitingForClose() {
		msg("waiting for store to close");
		try {
			Main.closingS.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	private void helping() {
		msg("item is heavy need 3 people");
		
		StorageClerks s1= null,s2 = null,s3=null;
		Customers c1 = null;
		try{
			c1 = Main.heavyCus.remove(0);
		}
		catch (Exception e){
			//msg("waiting for customer");
		}
		
		try{
			s1 = Main.store.remove(0);
			s2 = Main.store.remove(0);
			s3 = Main.store.remove(0);
		}catch(Exception e){
			//msg("waiting for storage clerk" );
		}
		
		Main.inLine2.release();
		Main.inLine2.release();
		Main.inLine2.release();
		try{
			s1.msg("getting help from " + s2.getName() + " and " + s3.getName() );
		}catch(Exception e){
			
		}

		try{
			msg("carrying furniture to " + c1.getName());
		}catch(Exception e){
			//	msg("waiting for customer");
		}
		temp = random.nextInt(2000);
		try {
			Thread.sleep(temp);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

		try{
			msg("gave furniture to " + c1.getName());
		}catch(Exception e){

		}

		Main.store.add(s1);
		Main.store.add(s2);
		Main.store.add(s3);
	
	}

	private void waitingForCustomer() {
		msg("waiting for customer");
		try {
			Main.inLine2.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		msg("customer is here");
	}

	private void born() {
		msg("arrives");
		Main.store.addElement(this);
	}

	public void msg(String m) {
		System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
	}

	private String getName() {

		return name;
	}

}
