package gdd.events;

public class ESend extends MyEvent {

	public final String id; // id of the message sent
	public Object payload;
	
	public ESend(Integer uid, String id, Object payload) {
		super(uid);
		this.id = id;
		this.payload = payload;
	}

	@Override
	public boolean equals(Object obj) {

		return obj instanceof ESend && id.equals(((ESend) obj).id)
				&& (uid.equals(((ESend) obj).uid));
	}
}
