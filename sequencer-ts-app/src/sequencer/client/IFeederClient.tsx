import Client from "./Client";

class FeederClient extends Client {
    protected baseUrl: string;

    constructor(baseUrl: string) {
        super(baseUrl)
    }

    public feedApi(url: string, input: string, callback: (res: string) => void) {
        this.post(`${this.baseUrl}${url}feeder/feed`, callback, input)
    }
}

export default FeederClient