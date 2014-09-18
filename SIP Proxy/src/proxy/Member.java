
package proxy;

public class Member
{
	public String	name;
	public String	password;
	public String	domena;

	public Member(String name, String pass, String domena) {
		this.name = name;
		this.password = pass;
		this.domena = domena;
	}
	public String getKeyID() {
		// "sip:201@local"
		return "sip:" + name + "@" + "domena";
	}
}
