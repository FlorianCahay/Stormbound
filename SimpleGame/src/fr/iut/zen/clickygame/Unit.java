package fr.iut.zen.clickygame;

public class Unit extends CardAbstract implements CardInterface {
	private final int strength;
	private final int movement;

	public Unit(String name, Effect effect, String faction, String rarity, int mana, int strength, int movement) {
		super(name, effect, faction, rarity, mana);
		this.strength = strength;
		this.movement = movement;
	}
	
	public Unit(int strength, int movement, int mana) {
		super("Tarzan", Effect.zero, "Neutral", "Rare", mana);
		this.strength = strength;
		this.movement = movement;
	}
	
	@Override
	public String toString() {
		return super.toString()+"\nStrength: "+strength+"\nMovement: "+movement+"\n";
	}
	public int getStrength() {
		return strength;
	}
	public int getMovement() {
		return movement;
	}
	@Override
	public String getName() {
		return super.getName();
	}
}