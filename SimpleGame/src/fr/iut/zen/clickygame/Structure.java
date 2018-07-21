package fr.iut.zen.clickygame;

import java.util.Objects;

public class Structure extends CardAbstract implements CardInterface {
	private final int strength;
	
	public Structure(String name, Effect effect, String faction, String rarity, int mana, int strength) {
		super(name, effect, faction, rarity, mana);
		this.strength = strength;
	}
	
	@Override
	public String toString() {
		return super.toString()+"\nStrength: "+strength+"\n";
	}
	public int getStrength() {
		return strength;
	}
	public int getMovement() {
		return 0;
	}
	
	@Override
	public String getName() {
		return super.getName();
	}
	
	@Override
	public int hashCode() {
		return super.hashCode() + Objects.hash(strength);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Structure)) {
			return false;
		}
		Structure c = (Structure) o;
		return super.equals(o) && c.strength == strength;
	}
}