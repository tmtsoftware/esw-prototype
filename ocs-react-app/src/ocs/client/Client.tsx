import * as request from "superagent";

class Client {
    protected resourcePath: string;
    private gatewayHost: string = "http://localhost:9090";

    constructor(resourcePath: string) {
        this.resourcePath = `${this.gatewayHost}${resourcePath}`;
    }

    public post(url: string, callback: (res: string) => void, input: string = "") {
        request
            .post(url)
            .set('Content-Type', 'application/json')
            .send(input)
            .then(res => {
                    console.log(res.text);
                        callback("Operation Successful")
                }, err => {
                    if (err.response) {
                        callback(err.response.text)
                    } else {
                        callback("Oops something went wrong !!")
                    }
                }
            );
    }

    public get(url: string, callback: (responses: string) => void) {
        request
            .get(url)
            .set('Content-Type', 'application/json')
            .send()
            .then(res => {
                    console.log(res.text);
                    if (res.body) {
                        callback(res.text)
                    }
                }, err => console.log(err)

            );
    }
}

export default Client;