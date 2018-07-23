import Client from "./Client";

class FeederClient extends Client{
    protected baseUrl: string;

    constructor(baseUrl: string) {
        super(baseUrl)
    }

    public feedApi = (input: string, callback: (res: string) => void) => this.post(`${this.baseUrl}/feeder/feed`, input, callback)
}

export default FeederClient