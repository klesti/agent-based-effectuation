package zombies.relogo

import static repast.simphony.relogo.Utility.*;
import static repast.simphony.relogo.UtilityG.*;
import repast.simphony.relogo.BasePatch;
import repast.simphony.relogo.BaseTurtle;
import repast.simphony.relogo.Plural;
import repast.simphony.relogo.Stop;
import repast.simphony.relogo.Utility;
import repast.simphony.relogo.UtilityG;

class Human extends BaseTurtle {
	
	def infected = false
	def infectionTime = 0
	
	def step() {
		def winner = minOneOf(neighbors()) {
			count(zombiesOn(it))			
		}
		face(winner)
		forward(1.5)
		if (infected) {
			infectionTime++
			if (infectionTime >= gestationPeriod) {
				hatchZombies(1) {
					size = 2
				}
				die()
			}
		}
	}

}
