import * as request from "superagent";

class Client {
    protected baseUrl: string;

    constructor(baseUrl: string) {
        this.baseUrl = baseUrl;
    }

    public post = (url: string, callback: (res: string) => void, input: string = "") => {
        request
            .post(url)
            .set('Content-Type', 'application/json')
            .send(input)
            .then(res => {
                    console.log(res.text);
                    if (res.text.includes("Done")) {
                        callback("Operation Successful")
                    } else {
                        callback(JSON.stringify(res.body, null, 2))
                    }
                }, err => {
                    if (err.response) {
                        callback(err.response.text)
                    } else {
                        callback("Oops something went wrong !!")
                    }
                }
            );
    }
}

export default Client;