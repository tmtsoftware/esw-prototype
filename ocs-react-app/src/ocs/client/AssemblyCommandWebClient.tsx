import Client from "./Client";

class AssemblyCommandWebClient extends Client {
    protected resourcePath: string;

    constructor(resourcePath: string) {
        super(resourcePath)
    }

    public submit(input: string, callback: (res: string) => void) {
        this.post(`${this.resourcePath}submit`, callback, input)
    }
}

export default AssemblyCommandWebClient