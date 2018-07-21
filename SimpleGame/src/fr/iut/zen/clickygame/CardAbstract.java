package fr.iut.zen.clickygame;

import java.util.Objects;

public abstract class CardAbstract {
	private final String name;
	private final Effect effect;
	private final String faction;
	private final String rarity;
	private final int mana;
	
	public CardAbstract(String name, Effect effect, String faction, String rarity, int mana) {
		this.name = name;
		this.effect = effect;
		this.faction = faction;
		this.rarity = rarity;
		this.mana = mana;
	}
	
	@Override
	public String toString() {
		return "\n " + name + "\n--------\n" + "Effect : " + effect.getEquivalent() + "\nFaction : " + faction + "\nRarity : "
				+ rarity + "\nMana cost : " + mana;
	}
	@Override
	public int hashCode() {
		return Objects.hash(name, effect, faction, rarity, mana);
	}
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof CardAbstract)) {
			return false;
		}
		CardAbstract c = (CardAbstract) o;
		return c.name.equals(name) && c.effect.equals(effect) && c.faction.equals(faction) && c.rarity.equals(rarity) && c.mana == mana;
	}
	public String getFaction() {
		return faction;
	}
	public int getMana() {
		return mana;
	}
	public String getName() {
		return name;
	}
	public String getEffect() {
		for (Effect eff : Effect.values()) {
			if (effect.equals(eff)) {
				return eff.getEquivalent();
			}
		}
		return Effect.zero.getEquivalent();
	}
	// Créer une carte depuis une ligne d'un fichier en fonction du type et de l'effet
	public static CardInterface makeCardFromLine(String line) {
		CardInterface card = null;
		String[] parameters = line.split("/");
		if (parameters[2].equals("Unit")) {
			for (Effect eff : Effect.values()) {
				if (parameters[1].equals(eff.getEquivalent())) {
					card = new Unit(parameters[0], eff, "Neutral", parameters[3], Integer.parseInt(parameters[4]), Integer.parseInt(parameters[5]), Integer.parseInt(parameters[6]));
					break;
				}
				else {
					card = new Unit(parameters[0], Effect.nothing, "Neutral", parameters[3], Integer.parseInt(parameters[4]), Integer.parseInt(parameters[5]), Integer.parseInt(parameters[6]));
				}
			}
		}
		else if (parameters[2].equals("Spell")) {
			for (Effect eff : Effect.values()) {
				if (parameters[1].equals(eff.getEquivalent())) {
					card = new Spell(parameters[0], eff, "Neutral", parameters[3], Integer.parseInt(parameters[4]));
					break;
				}
				else {
					card = new Spell(parameters[0], Effect.nothing, "Neutral", parameters[3], Integer.parseInt(parameters[4]));
				}
			}
		}
		else if (parameters[2].equals("Structure")) {
			for (Effect eff : Effect.values()) {
				if (parameters[1].equals(eff.getEquivalent())) {
					card = new Structure(parameters[0], eff, "Neutral", parameters[3], Integer.parseInt(parameters[4]), Integer.parseInt(parameters[5]));
					break;
				}
				else {
					card = new Structure(parameters[0], Effect.nothing, "Neutral", parameters[3], Integer.parseInt(parameters[4]), Integer.parseInt(parameters[5]));
				}
			}
		}
		return card;
	}
}