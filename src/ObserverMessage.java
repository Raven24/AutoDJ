
public class ObserverMessage {
	public static final int PLAY = 1;
	public static final int PAUSE = 2;
	
	public static final int RESCAN_LIBRARY = 100;
	
	private int message;
	
	public ObserverMessage (int message) {
		this.message=message;
	}
	
	public int getMessage () {
		return this.message;
	}
}
