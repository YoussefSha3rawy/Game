package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import model.infrastructure.ResidentialBuilding;

public class GasLeak extends Disaster {

	public GasLeak(int startCycle, ResidentialBuilding target) {
		super(startCycle, target);
	}

	@Override
	public void strike() throws CitizenAlreadyDeadException, BuildingAlreadyCollapsedException {

		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.hasCollapsed()) {
			throw new BuildingAlreadyCollapsedException(this, "Building has already collapsed");
		} else {
			target.setGasLevel(target.getGasLevel() + 10);
			super.strike();
		}

	}

	@Override
	public void cycleStep() {
		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		target.setGasLevel(target.getGasLevel() + 15);

	}

	public String toString() {
		return "Gas Leak Disaster has struck the Building at: (" + this.getTarget().getLocation().getX() + ","
				+ this.getTarget().getLocation().getY() + ")" + " during cycle " + this.getStartCycle();
	}

}
