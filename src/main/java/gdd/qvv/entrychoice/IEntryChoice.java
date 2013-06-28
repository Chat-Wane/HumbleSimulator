package gdd.qvv.entrychoice;


public interface IEntryChoice {

	/**
	 * Initial entry given to the peer
	 * 
	 * @return An Integer representing the entry of the vector for the peer
	 */
	Integer init();

	/**
	 * Renew entry of the VC
	 * 
	 * @return Integer corresponding to the chosen entry
	 */
	Integer get();

}
