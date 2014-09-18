package Action;

import java.util.List;

public interface Action
{
	public void make(String[] x);
	public boolean can(List<String> x);
}
