package gdd.events;

public class EDeliver extends MyEvent {

	public final String id; // id of the msg delivered

	public EDeliver(Integer uid, String id) {
		super(uid);
		this.id = id;
	}
}
