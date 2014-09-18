package proxy;

import javax.sip.header.CallIdHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.ToHeader;
import javax.sip.message.Message;

public class Hovor {
	public FromHeader from;
	public ToHeader to;
	public CallIdHeader callidheader;
	
	public Hovor(Message msg) {
		// uloz hodnoty
		from = (FromHeader) msg.getHeader(FromHeader.NAME);
		to = (ToHeader) msg.getHeader(ToHeader.NAME);
		callidheader = ((CallIdHeader) msg.getHeader(CallIdHeader.NAME));
	}
	
	public boolean equals(Object obj) {
		return ((Hovor) obj).callidheader == callidheader;
	}
	
	public String toString() {
		return from.getName() + " " + to.getName() + " " + callidheader.getCallId();	
	}		
}
