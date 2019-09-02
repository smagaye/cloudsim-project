package sn.ucad.manager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import sn.ucad.helper.Const;
import sn.ucad.helper.RandomValues;

public class CloudSimManager {

	/** The cloudlet list. */
	private static List<Cloudlet> cloudletList;

	/** The vmlist. */
	private static List<Vm> vmlist;

	
	private static Datacenter createDatacenter(String name) {
		List<Host> hostList = new ArrayList<Host>();
		int mips_host = Const.MIPS_HOST;
		int hostId = Const.ID_HOST_START;
		int ram = Const.RAM_HOST; // host memory (MB)
		long storage = Const.STORAGE; // host storage
		int bw = Const.BW_HOST;
		
		for (int i = 0; i < Const.HOSTS_NUMBER; i++) {
			// . Create PEs and add these into a list
			List<Pe> peList2 = new ArrayList<Pe>();
			peList2.add(new Pe(0, new PeProvisionerSimple(mips_host)));
			hostList.add(new Host(hostId, new RamProvisionerSimple(ram), new BwProvisionerSimple(bw), storage, peList2,
					new VmSchedulerTimeShared(peList2))

			); 
			hostId++;
		}
		// create another machine in the Data center


		LinkedList<Storage> storageList = new LinkedList<Storage>(); // we are not adding SAN devices by now

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(Const.ARCH, Const.OS, Const.VMM_HOST, hostList, Const.TIME_ZONE,
				Const.COST, Const.COST_PER_MEMORY, Const.COST_PER_STORAGE, Const.COST_PER_BW_HOST);

		// 6. Finally, we need to create a PowerDatacenter object.
		Datacenter datacenter = null;
		try {
			datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datacenter;
	}

	// We strongly encourage users to develop their own broker policies, to submit
	// vms and cloudlets according
	// to the specific rules of the simulated scenario
	private static DatacenterBroker createBroker() {

		DatacenterBroker broker = null;
		try {
			broker = new DatacenterBroker("Broker");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}

	public static double simulationSimple(String name, int nbrVM, long length_cloudlet) {
		Log.printLine("Starting " + name);

		try {
			// First step: Initialize the CloudSim package. It should be called
			int num_user = 1; // number of cloud users
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = false; // mean trace events

			// Initialize the CloudSim library
			CloudSim.init(num_user, calendar, trace_flag);

			// Second step: Create Datacenters
			// Datacenters are the resource providers in CloudSim. We need at list one of
			// them to run a CloudSim simulation
			@SuppressWarnings("unused")
			Datacenter datacenter0 = createDatacenter("Datacenter_0");

			// Third step: Create Broker
			DatacenterBroker broker = createBroker();
			int brokerId = broker.getId();

			// Fourth step: Create one virtual machine
			vmlist = new ArrayList<Vm>();
			int vmid =  Const.ID_VM_START;
			int mips_vm = Const.MIPS_VM;
			// create 5 first VMs
			for (int i = 0; i < nbrVM; i++) {
				if (i == 5)
					mips_vm = 750;
				Vm vm1 = new Vm(vmid, brokerId, mips_vm, Const.PES_NUMBER, Const.RAM_VM, Const.BW_VM, Const.SIZE, Const.VMM_VM, new CloudletSchedulerTimeShared());
				vmlist.add(vm1);
				vmid++;
			}

			// submit vm list to the broker
			broker.submitVmList(vmlist);

			// Fifth step: Create two Cloudlets
			cloudletList = new ArrayList<Cloudlet>();

			int id = Const.ID_CLOUDLET_START;
			long length = Const.QUERY_SIZE_FIXED;
			
			UtilizationModel utilizationModel = new UtilizationModelFull();
			if (length_cloudlet > 0) {
				length = length_cloudlet;
				for (int i = 0; i < Const.CLOUDLET_NUMBER; i++) {
					Cloudlet cloudlet2 = new Cloudlet(id , length, Const.PES_NUMBER, Const.FILE_SIZE,  Const.OUTPUT_SIZE, utilizationModel,
							utilizationModel, utilizationModel);
					cloudlet2.setUserId(brokerId);
					// add the cloudlets to the list
					cloudletList.add(cloudlet2);
					id++;
				}

			}

			// submit cloudlet list to the broker
			broker.submitCloudletList(cloudletList);

			// Sixth step: Starts the simulation

			double time = CloudSim.startSimulation();
			// Final step: Print results when simulation is over
			List<Cloudlet> newList = broker.getCloudletReceivedList();
			CloudSim.stopSimulation();

//			printCloudletList(newList);

			double fin = (double) newList.get(newList.size() - 1).getFinishTime();
			System.out.println(name + " \t Terminé \t Durée : " + time+"\t Fin : " + fin);

			return time;
		} catch (Exception e) {
			e.printStackTrace();
			Log.printLine("The simulation has been terminated due to an unexpected error");
		}
		return 0;
	}

	public static double simulationAleatoire(String name, int nbrVM) {
		Log.printLine("Starting " + name);

		try {
			// First step: Initialize the CloudSim package. It should be called
			int num_user = 1; // number of cloud users
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = false; // mean trace events

			// Initialize the CloudSim library
			CloudSim.init(num_user, calendar, trace_flag);

			// Second step: Create Datacenters
			// Datacenters are the resource providers in CloudSim. We need at list one of
			// them to run a CloudSim simulation
			@SuppressWarnings("unused")
			Datacenter datacenter0 = createDatacenter("Datacenter_0");

			// Third step: Create Broker
			DatacenterBroker broker = createBroker();
			int brokerId = broker.getId();

			// Fourth step: Create one virtual machine
			vmlist = new ArrayList<Vm>();
			int vmid =  Const.ID_VM_START;
			int mips_vm = Const.MIPS_VM;
			// create 5 first VMs
			for (int i = 0; i < nbrVM; i++) {
				if (i == 5)
					mips_vm = 750;
				Vm vm1 = new Vm(vmid, brokerId, mips_vm, Const.PES_NUMBER, Const.RAM_VM, Const.BW_VM, Const.SIZE, Const.VMM_VM, new CloudletSchedulerTimeShared());
				vmlist.add(vm1);
				vmid++;
			}

			// submit vm list to the broker
			broker.submitVmList(vmlist);

			// Fifth step: Create two Cloudlets
			cloudletList = new ArrayList<Cloudlet>();

			int id = Const.ID_CLOUDLET_START;
			long length = Const.QUERY_SIZE_FIXED;
			
			UtilizationModel utilizationModel = new UtilizationModelFull();
				for (int i = 0; i < Const.CLOUDLET_NUMBER; i++) {
					length = (long) RandomValues.between(Const.MIN_QUERY_SIZE,Const.MAX_QUERY_SIZE);
					Cloudlet cloudlet2 = new Cloudlet(id, length, Const.PES_NUMBER,  Const.FILE_SIZE,  Const.OUTPUT_SIZE, utilizationModel,
							utilizationModel, utilizationModel);
					cloudlet2.setUserId(brokerId);
					// add the cloudlets to the list
					cloudletList.add(cloudlet2);
					id++;
				}

			// submit cloudlet list to the broker
			broker.submitCloudletList(cloudletList);

			// Sixth step: Starts the simulation

			double time = CloudSim.startSimulation();
			// Final step: Print results when simulation is over
			List<Cloudlet> newList = broker.getCloudletReceivedList();
			CloudSim.stopSimulation();

			printCloudletList(newList);

			double fin = (double) newList.get(newList.size() - 1).getFinishTime();
			System.out.println(name + " \t Terminé \t Durée : " + time+"\t Fin : " + fin);

			return time;
		} catch (Exception e) {
			e.printStackTrace();
			Log.printLine("The simulation has been terminated due to an unexpected error");
		}
		return 0;
	}

	private static void printCloudletList(List<Cloudlet> list) {
		int size = list.size();
		Cloudlet cloudlet;

		String indent = "    ";
		Log.printLine("\n========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent + "Data center ID" + indent + "VM ID" + indent + "Time"
				+ indent + "Start Time" + indent + "Finish Time");

		DecimalFormat dft = new DecimalFormat("###.##");
		for (int i = 0; i < size; i++) {
			cloudlet = list.get(i);
			Log.print(indent + cloudlet.getCloudletId() + indent + indent);

			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
				Log.print("SUCCESS");

				Log.printLine(indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId()
						+ indent + indent + dft.format(cloudlet.getActualCPUTime()) + indent + indent
						+ dft.format(cloudlet.getExecStartTime()) + indent + indent
						+ dft.format(cloudlet.getFinishTime()));
			}
		}

	}

}
