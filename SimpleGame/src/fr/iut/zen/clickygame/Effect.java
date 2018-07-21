package fr.iut.zen.clickygame;

public enum Effect {
	nothing("Pas implémenté"),
	zero("No effect"),
	one("Deal 1 damage to all enemy units and structures"),
	two("Deal 3 damage to a target enemy unit or structure"),
	three("On play, deal 2 damage to a random surrounding enemy"),
	four("Before attacking a unit or structure, deal 3 additional damage to it"),
	five("On play, give 2 strength to another random friendly unit"),
	six("At the start of your turn, give 2 strength to all friendly units in front"),
	seven("On play, deal 4 damage to all bordering enemy structures");
	
	private String equivalent;
	
	private Effect(String equivalent) {
		this.equivalent = equivalent;
	}
	
	public String getEquivalent() {
		return equivalent;
	}
}
