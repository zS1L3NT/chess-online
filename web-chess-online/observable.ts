export default class Observable<T> {
	private value: T
	private resolve: ((value: T) => void) | null = null

	constructor(value: T) {
		this.value = value
	}

	public set(value: T) {
		this.value = value
		if (this.resolve) {
			this.resolve(value)
			this.resolve= null
		}
	}

	public get() {
		return this.value
	}

	public waitForChange() {
		return new Promise<T>(resolve => {
			this.resolve = resolve
		})
	}
}