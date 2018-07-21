package fr.iut.zen.clickygame;

public class Spell extends CardAbstract implements CardInterface {
	
	public Spell(String name, Effect effect, String faction, String rarity, int mana) {
		super(name, effect, faction, rarity, mana);

	}
	
	public int getStrength() {
		return 0;
	}
	public int getMovement() {
		return 0;
	}
	public String getName() {
		return super.getName();
	}
	public String getEffect() {
		return super.getEffect();
	}	
}