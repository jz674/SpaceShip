/**Jeffrey Zhang and Anirudh Maddula
 * jz674 and aam252
 * 20 hours
 * it was an honor!
 */

package student;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import models.Edge;
import models.Node;
import models.NodeStatus;
import models.RescueStage;
import models.ReturnStage;
import models.Spaceship;
import student.Paths;

/** An instance implements the methods needed to complete the mission */
public class MySpaceship extends Spaceship {

	private static final int NodeStatus = 0;


	/**
	 * Explore the galaxy, trying to find the missing spaceship that has crashed
	 * on Planet X in as little time as possible. Once you find the missing
	 * spaceship, you must return from the function in order to symbolize that
	 * you've rescued it. If you continue to move after finding the spaceship
	 * rather than returning, it will not count. If you return from this
	 * function while not on Planet X, it will count as a failure.
	 * 
	 * At every step, you only know your current planet's ID and the ID of all
	 * neighboring planets, as well as the ping from the missing spaceship.
	 * 
	 * In order to get information about the current state, use functions
	 * currentLocation(), neighbors(), and getPing() in RescueStage. You know
	 * you are standing on Planet X when foundSpaceship() is true.
	 * 
	 * Use function moveTo(long id) in RescueStage to move to a neighboring
	 * planet by its ID. Doing this will change state to reflect your new
	 * position.
	 */
	@Override
	public void rescue(RescueStage state) {
		// TODO : Find the missing spaceship
		ArrayList<Long> visited = new ArrayList<Long>();
		rescueHelper(state, visited);
		//return;
	}

	/**
	 * Recursive method that return when the spaceship (in RescueStage state) has found Planet X.
	 * It uses a heap to sort neighboring planet id's with the priority of their pings, then iterates through
	 * the heap to do a DFS. It will move backwards if all neighbors have already been visited.
	 */
	public void rescueHelper(RescueStage state, ArrayList<Long> visited){

		if(state.foundSpaceship()){
			return;
		}
		visited.add(state.currentLocation());

		Heap<Long> PingTracker = new Heap<Long>();
		for(NodeStatus ns: state.neighbors()) {
			PingTracker.add(ns.getId(), -ns.getPingToTarget());
		}

		int a = state.neighbors().size();
	
		for(int i =0; i<a; i++){

			//checks the top of the heap
			if((PingTracker.size() != 0) && !visited.contains(PingTracker.peek())){
				if(state.foundSpaceship()){
					return;
				}
				state.moveTo(PingTracker.poll());
				rescueHelper(state, visited);
			}
			else {
				PingTracker.poll();
			}

		}
		//if every single ns is in tracker, then you're at a deadend. move back one and continue iterating through the 
		//rest of the set
		if(state.foundSpaceship()){
			return;
		}
		visited.remove(state.currentLocation());
		state.moveTo(visited.get(visited.size()-1));
		//return;

	}	
	

	/**
	 * Get back to Earth, avoiding hostile troops and searching for speed
	 * upgrades on the way. Traveling through 3 or more planets that are hostile
	 * will prevent you from ever returning to Earth.
	 *
	 * You now have access to the entire underlying graph, which can be accessed
	 * through ReturnStage. currentNode() and getEarth() will return Node objects
	 * of interest, and allNodes() will return a collection of all nodes in the
	 * graph.
	 *
	 * You may use state.grabSpeedUpgrade() to get a speed upgrade if there is
	 * one, and can check whether a planet is hostile using the isHostile
	 * function in the Node class.
	 *
	 * You must return from this function while on Earth. Returning from the
	 * wrong location will be considered a failed run.
	 *
	 * You will always be able to return to Earth without passing through three
	 * hostile planets. However, returning to Earth faster will result in a
	 * better score, so you should look for ways to optimize your return.
	 */
	@Override
	public void returnToEarth(ReturnStage state) {
		// TODO: Return to Earth
		returnToEarthDraft(state);
	}
	

	public void returnToEarthDraft(ReturnStage state) {
		ArrayList<Node> noHos = new ArrayList<Node>();
		noHos.addAll(Paths.shortestPath(state.currentNode(), state.getEarth()));
		//the shortest path will include hostile, but the weights for them are insane.
		if (!(noHos.isEmpty())) {
			noHos.remove(0);
			for (Node ns : noHos) {
				state.moveTo(ns);
				if (ns.hasSpeedUpgrade()) {
					state.grabSpeedUpgrade();
				}
			}
			return;
		}
	}
}






