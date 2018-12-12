package isebase.cognito.tourpilot_apk.DataInterfaces.Job;

import java.util.Comparator;

/**
 * Comparer for IJob objects.  Use eJobComparerType to define comparer type </p> 
 * ONLY_TIME - ignore statuses (done, sent). Compare only by time </p>
 * NORMAL_DONE_SENT - normal items -> done items -> sent items. Compare by time for each status. 
 * */
public class JobComparer implements Comparator<IJob> {

	public enum eJobComparerType {ONLY_TIME, NORMAL_DONE_SENT};
	
	private eJobComparerType jobComparerType;
		
	public JobComparer(eJobComparerType jobComparerType){
		this.jobComparerType = jobComparerType;
	}
	
	public JobComparer(){	
		this(eJobComparerType.NORMAL_DONE_SENT);
	}
	
	@Override
	public int compare(IJob lhs, IJob rhs) {
		switch(jobComparerType){
			case NORMAL_DONE_SENT:
				int retVal = 0;		
				retVal = compareWasSent(lhs, rhs);		
				if(retVal == 0){
					retVal = compareIsDone(lhs, rhs);
					if(retVal == 0)
//						retVal = compareTime(lhs, rhs);
						retVal = compareDate(lhs, rhs);
				}		
				return retVal;
			case ONLY_TIME:
//				return compareTime(lhs, rhs);
				return compareDate(lhs, rhs);
			default: return 0;
		}
	}
	
//	private int compareTime(IJob lhs, IJob rhs){
//		return lhs.time().compareTo(rhs.time());
//	}
	
	private int compareDate(IJob lhs, IJob rhs){
		return lhs.startTime().compareTo(rhs.startTime());		
	}
	
	private int compareIsDone(IJob lhs, IJob rhs){
		int retVal = 0;
		if(lhs.isDone() == rhs.isDone())
			retVal = 0;
		if(lhs.isDone() && !rhs.isDone())
			retVal = 1;
		if(!lhs.isDone() && rhs.isDone())
			retVal = -1;
		return retVal;
	}
	
	private int compareWasSent(IJob lhs, IJob rhs){
		int retVal = 0;
		if(lhs.wasSent() == rhs.wasSent())
			retVal = 0;
		if(lhs.wasSent() && !rhs.wasSent())
			retVal = 1;
		if(!lhs.wasSent() && rhs.wasSent())
			retVal = -1;
		return retVal;
	}
}
