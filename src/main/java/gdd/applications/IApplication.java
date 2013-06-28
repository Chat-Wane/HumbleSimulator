package gdd.applications;

import java.util.Observer;

public interface IApplication extends Observer {

	public void send(Object arg);

	public void receive(Object arg);

}
