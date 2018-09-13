import Client from "./Client";

class ResultEventClient extends Client{
    protected resourcePath: string;
    protected eventSource: EventSource;

    constructor(resourcePath: string) {
       super(resourcePath);
        this.eventSource = new EventSource(`${this.resourcePath}results`);
    }

    public onMessage(callback: (evt: MessageEvent) => any) {
        this.eventSource.onmessage = callback;
    }

    public close() {
        this.eventSource.close();
    }
}

export default ResultEventClient