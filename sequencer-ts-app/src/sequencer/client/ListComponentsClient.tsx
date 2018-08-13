import * as request from "superagent";

class ListComponentsClient {
    protected baseUrl: string;

    constructor(baseUrl: string) {
        this.baseUrl = baseUrl;
    }

    public listSequencers(callback: (sequencers: string[]) => void): void {
        request
            .get(this.baseUrl + "/locations/sequencers")
            .set('Content-Type', 'application/json')
            .send()
            .then(res => {
                    console.log(res.text);
                    if (res.body) {
                        callback(res.body)
                    }
                }, err => console.log(err)

            );
    }

    public listAssemblies(callback: (assemblies: string[]) => void): void {
        request
            .get(this.baseUrl + "/locations/assemblies")
            .set('Content-Type', 'application/json')
            .send()
            .then(res => {
                    console.log(res.text);
                    if (res.body) {
                        callback(res.body)
                    }
                },
                err => console.log(err)
            );
    }
}

export {ListComponentsClient}