import * as request from "superagent";

class Client {
    protected baseUrl: string;
    constructor(baseUrl: string) {
        this.baseUrl = baseUrl;
    }

    public post = (url: string, input: string = "", callback: (res: string) => void) => {
        request
            .post(url)
            .set('Content-Type', 'application/json')
            .send(input)
            .then(res => callback(JSON.stringify(res.body, null, 2)), err => {
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