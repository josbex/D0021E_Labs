package lab5;

public enum LinkStatus {
	idle,
	busy;

	public boolean isIdle() {
		return this == idle;
	}

	public boolean isBusy() {
		return this == busy;
	}
}
