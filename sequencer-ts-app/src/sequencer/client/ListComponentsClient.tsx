import Client from "./Client";

class ListComponentsClient extends Client {
    protected resourcePath: string;

    constructor(resourcePath: string) {
        super(resourcePath);
    }

    public listSequencers(callback: (sequencers: string[]) => void): void {
        this.get(`${this.resourcePath}/sequencers`, callback)
    }

    public listAssemblies(callback: (assemblies: string[]) => void): void {
        this.get(`${this.resourcePath}/assemblies`, callback)
    }
}

export {ListComponentsClient}