package zombies.relogo

import static repast.simphony.relogo.Utility.*;
import static repast.simphony.relogo.UtilityG.*;
import repast.simphony.relogo.BaseObserver;
import repast.simphony.relogo.Stop;
import repast.simphony.relogo.Utility;
import repast.simphony.relogo.UtilityG;

class UserObserver extends BaseObserver{
	
	def setup() {
		clearAll()
		setDefaultShape(Human, "person")
		createHumans(numHumans)  {
			setxy(randomXcor(), randomYcor()) 
		}
		
		setDefaultShape(Zombie, "zombie")
		createZombies(numZombies) {
			setxy(randomXcor(), randomYcor())
			size = 2
		}		
	}
	
	def go() {
		ask (zombies()) {
			step()
		}
		
		ask (humans()) {
			step()
		}
	}
	
	def remainingHumans() {
		count(humans())
	}

	/**
	 * Add observer methods here. For example:

		def setup(){
			clearAll()
			createTurtles(10){
				forward(random(10))
			}
		}
		
	 *
	 * or
	 * 	
	
		def go(){
			ask(turtles()){
				left(random(90))
				right(random(90))
				forward(random(10))
			}
		}

	 */

}