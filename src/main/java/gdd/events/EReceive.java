package gdd.events;

public class EReceive extends MyEvent {

	public final String id; // id of the msg received
	public Object payload; // content of the message received

	public EReceive(Integer uid, String id, Object payload) {
		super(uid);
		this.id = id;
		this.payload = payload;
	}
}
