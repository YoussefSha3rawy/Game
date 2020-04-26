package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import model.infrastructure.ResidentialBuilding;

public class Collapse extends Disaster {

	public Collapse(int startCycle, ResidentialBuilding target) {
		super(startCycle, target);

	}

	public void strike() throws CitizenAlreadyDeadException, BuildingAlreadyCollapsedException {
		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.hasCollapsed()) {
			throw new BuildingAlreadyCollapsedException(this, "Building has already collapsed");
		} else {
			target.setFoundationDamage(target.getFoundationDamage() + 10);
			super.strike();
		}
	}

	public void cycleStep() {
		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		target.setFoundationDamage(target.getFoundationDamage() + 10);
	}

	public String toString() {
		return "Collapse Disaster has struck the Building at: (" + this.getTarget().getLocation().getX() + ","
				+ this.getTarget().getLocation().getY() + ")" + " during cycle " + this.getStartCycle();
	}

}
