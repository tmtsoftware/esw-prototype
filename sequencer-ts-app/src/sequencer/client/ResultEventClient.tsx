class ResultEventClient {
    protected baseUrl: string;
    protected url: string;
    protected eventSource: EventSource;

    constructor(baseUrl: string, url: string) {
        this.baseUrl = baseUrl;
        this.eventSource = new EventSource(`${this.baseUrl}${url}results`);
    }

    public onMessage(callback: (evt: MessageEvent) => any) {
        this.eventSource.onmessage = callback;
    }

    public close() {
        this.eventSource.close();
    }
}

export default ResultEventClient