import java.util.*;
import java.io.IOException;

public class server {
	public static PriorityQueue <Event> FutureEventslist ; 
	public static  Queue <Event> delayedList ;
	public static double Clock, MeanInterArrivalTime, MeanServiceTime, LastEventTime,NumberOfDepartures,
    TotalBusy, TotalDelayeTime,TotalCompletion;
	public static double TotalServiceTime = 0;
	public static double TotalInterArrivalTime = 0;
	public static double TotalQueueLength;
	public final static int arrival = 0;
	public final static int departure = 1;
	static Random rand ;
	static boolean busy;
	static int qLenght,busyPeriod=0;
	  
	  
	
	 
	  public static void main(String argv[]) {
		// initializing
		  MeanInterArrivalTime = 6; MeanServiceTime = 5;
		  TotalCompletion = 1000;
          int count=0;
		  rand = new Random();           //  UNIFORM RANDOM 
		  FutureEventslist = new PriorityQueue<Event>();
		  delayedList= new LinkedList<Event>();
		  Clock = 0.0;
		  LastEventTime = 0.0;
		  TotalBusy = 0 ;
		  TotalDelayeTime = 0;
		  NumberOfDepartures = 0;
		  Event evt = new Event(arrival, realTime( rand , MeanInterArrivalTime));// initialized first arrival scheduling 
		  FutureEventslist.add( evt );// add the first event in  future event list 
		  System.out.println( "                                                    ABDELHAKIM MEKKAOUI               ");
		  
		  
		  // Loop until first all have departed
		  while(NumberOfDepartures < TotalCompletion ) {// repeat for 1000 completion
			   count++;
			   
		    Event evt1 = (Event)FutureEventslist.poll();  // get imminent of the first event in the future event list
		           
		    if(count<=20) {// printing the first 20 events
		        	  if(evt1.get_type()==arrival)
		        		  System.out.println("arrival event");
		        	  else System.out.println("end of service event");
		        	  System.out.println("the fel size is  "+FutureEventslist.size()+" the del size is "+qLenght);
		        	  
		        	  
		          }
		    TotalQueueLength+=qLenght;  // ACCUMULATE THE  NUMBER OF QUEUE SIZE       
		    if( evt1.get_type() == arrival ) Arrival(evt1);// check the type the event pulled future event 
		    else  EndOfservice(evt1);
		    }
		  System.out.println( "\tMEAN INTERARRIVAL TIME                         "
		  	+ (TotalInterArrivalTime / TotalCompletion) );
		  System.out.println( "\tMEAN SERVICE TIME                              "
		  	+ (TotalServiceTime / TotalCompletion) );
		  System.out.println( "\tAVERAGE QUEUE (DEL) LENGTH                               "
				  	+ (TotalQueueLength / TotalCompletion) );// 
		  
		  System.out.println( "\tAVERAGE BUSY PERIOD                               "
				  	+(TotalDelayeTime/TotalCompletion)  );
		  System.out.println( "\tAVERAGE WAIT TIME IN THE SYSTEM                               "
				  	+((TotalDelayeTime+TotalServiceTime)/TotalCompletion)  );
	  }
	  public static void Arrival(Event evt) {
		  

		  Clock = evt.get_time(); // update the clock to the current event that have been pulled from future event list
		  
		  // schedule the next arrival
		  double next_interarrival_time = realTime(rand, MeanInterArrivalTime);// scheduling next arrival event 
		  TotalInterArrivalTime += next_interarrival_time;
		  Event next_arrival = new Event(arrival, Clock+next_interarrival_time);// create the next arrival event 
		  FutureEventslist.add( next_arrival );// add the next arrival event into the future event list 
		 
		  
		  //check if the server is busy
		  if (!busy) {// server is idle 
			  LastEventTime = Clock;// time that the server start to be busy
			  busy=true;// server is busy from now on 
			  busyPeriod++;
			  ScheduleDeparture();// schedule an end of service event to this arrival event 
			  
		  }
		  else {// server is  busy 
			  delayedList.add(evt);// add the arrive event  to delay list 
			  //busyPeriod++;// increment the busy periods of the server
			  qLenght++;// Increment the length of the delay list 
		      
		  }
		 }
	  public static void EndOfservice(Event e) {
		  Clock = e.get_time();// update the clock to the current event that have been pulled from future event list

		  // check if the queue is empty or not
		  if (qLenght!=0) {
			  qLenght--;
			  busy=true;
			  
			  Event depart_Event=delayedList.remove();// remove the first event in the delay list 
	          double delayTime= (Clock-depart_Event.get_time());// compute delay time 
	          TotalDelayeTime+=delayTime;// compute the total delay time till now 
			  ScheduleDeparture();// schedule end of service event of this arrival event 
			  
		  }
		  
		  else {
			  TotalBusy += (Clock - LastEventTime );// compute  the recent period time the server was busy 
			  busy=false ; // server idle
			  
		  }
		 //gather statistics
		   
		   NumberOfDepartures++;
		   
		  }
	  
	  
	private static void ScheduleDeparture() {// schedule end of service method
		double ServiceTime;
		   ServiceTime = realTime(rand, MeanServiceTime);
		  TotalServiceTime += ServiceTime;
		  Event depart = new Event(departure,Clock+ServiceTime);// Create endofservice event 
		  FutureEventslist.add(depart);// add end of service  event to the future event list 
		
	}
	
	
	public static double realTime(Random rng, double mean) { //method  to  obtain the time from the exponential density 
		 return -mean*Math.log( rng.nextDouble() );
		}
	
	
	
};
