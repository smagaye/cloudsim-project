package sn.ucad.helper;

public class Const {
	
	// Hosts with its id and list of PEs and add them to the list of
	// machines
	public final static int ID_HOST_START = 0;
	public final static int RAM_HOST = 4096; // host memory (MB)
	public final static long STORAGE = 512000; // host storage
	public final static int BW_HOST = 10000;
	public final static int HOSTS_NUMBER = 20;
	public final static int MIPS_HOST = 1000;
	public final static String ARCH = "x86"; // system architecture
	public final static String OS = "Linux"; // operating system
	public final static String VMM_HOST = "Xen";
	public final static double TIME_ZONE = 10.0; // time zone this resource located
	public final static double COST = 3.0; // the cost of using processing in this resource
	public final static double COST_PER_MEMORY = 0.05; // the cost of using memory in this resource
	public final static double COST_PER_STORAGE = 0.001; // the cost of using storage in this resource
	public final static double COST_PER_BW_HOST = 0.0; // the cost of using bw in this resource
	
	// Vms properties
	public final static int ID_VM_START = 0;
	public final static int MIPS_VM = 250;
	public final static long SIZE = 10000; // image size (MB)
	public final static int RAM_VM = 512; // vm memory (MB)
	public final static long BW_VM = 1000;
	public final static int PES_NUMBER = 1; // number of cpus
	public final static String VMM_VM = "Xen"; // VMM name
	
	
	// Cloudlet properties
	public final static int ID_CLOUDLET_START = 0;
	public final static int CLOUDLET_NUMBER = 1000;
	public final static long FILE_SIZE = 300;
	public final static long OUTPUT_SIZE = 300;
	public final static long QUERY_SIZE_FIXED = 30000;
	public static final int MIN_QUERY_SIZE = 1;
	public static final int MAX_QUERY_SIZE = 35000;
	


	
}
