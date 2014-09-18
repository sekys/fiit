package Base;
public class Uloha
{
	private Facts	m_facts;
	private Rules	m_rules;

	public Uloha() {
		m_facts = new Facts();
		m_rules = new Rules();
	}

	public void testRodina() {
		m_facts.load("rodina_f.txt");
		m_rules.load("rodina_p.txt");
	}
	public void testFiaty() {
		m_facts.load("fiaty_f.txt");
		m_rules.load("fiaty_p" + ".txt");
	}
	public void dump() {
		m_facts.dump();
		m_rules.dump();
	}
	

	public Facts getFacts() {
		return m_facts;
	}

	public void setFacts(Facts facts) {
		m_facts = facts;
	}

	public Rules getRules() {
		return m_rules;
	}

	public void setRules(Rules rules) {
		m_rules = rules;
	}

}
