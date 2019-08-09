import java.util.Vector;
import java.util.concurrent.Semaphore;

public class Main {
	static int nCustomers,nFloorClerks,nStorageClerks;
	public static int countHelp = 0;
	public static Semaphore mutex_fHelp = new Semaphore(1);	
	public static Semaphore hasHeavy = new Semaphore(1);
	public static Semaphore closingC = new Semaphore(0);
	public static Semaphore closingF = new Semaphore(0);
	public static Semaphore closingS = new Semaphore(0);
	public static Semaphore sClerksAvailable,inLine,inLine2,fWaitingCus,sWaitingCus;
	public static int id = 1,totalDone= 0;
	public static Vector<Customers> cus = new Vector<Customers>();
	public static Vector<Customers> heavyCus = new Vector<Customers>();
	public static Vector<StorageClerks> store = new Vector<StorageClerks>();
	
	
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
		
		fWaitingCus = new Semaphore(nFloorClerks);
		sWaitingCus = new Semaphore(nStorageClerks);
		inLine = new Semaphore(0);
		inLine2 = new Semaphore(0);
		sClerksAvailable = new Semaphore(nStorageClerks);

		//creates threads
		ThreadGroup fgroup = new ThreadGroup("fGroup");
		for(int i = 0; i < nFloorClerks;i++){
			FloorClerks fclerk = new FloorClerks(id++);
			Thread t1 = new Thread(fgroup,fclerk);
			t1.start();
			
		}
		id = 1;
		ThreadGroup cgroup = new ThreadGroup("cGroup");
		for(int i = 0; i < nCustomers;i++){
			Customers cus = new Customers(id++);
			Thread t2 = new Thread(cgroup,cus);
			t2.start();
			
		}
		
		id = 1;
		ThreadGroup sgroup = new ThreadGroup("sGroup");
		
		for(int i = 0; i < nStorageClerks;i++){
			StorageClerks sclerk = new StorageClerks(id++);
			Thread t3 = new Thread(sgroup,sclerk);
			t3.start();
			
		}
	
		
	}

}
