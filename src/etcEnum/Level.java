package Practice.InsuranceCompany.Design.src.etcEnum;


/**
 * @author SeoyeonPark
 * @version 1.0
 * @created 21-5-2022 ���� 11:03:49
 */
public enum Level {
	high(2),
	middle(1),
	low(0);

	private int severity;
	Level(int severity){ this.severity = severity; }
	public int getLevelNum(){ return this.severity; }
}