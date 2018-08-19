import Client from "./Client";

class FeederClient extends Client {
    protected resourcePath: string;

    constructor(resourcePath: string) {
        super(resourcePath)
    }

    public feedApi(input: string, callback: (res: string) => void) {
        this.post(`${this.resourcePath}feeder/feed`, callback, input)
    }
}

export default FeederClient