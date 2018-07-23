import * as request from "superagent";

class Client {
    private baseUrl: string;

    constructor(baseUrl: string) {
        this.baseUrl = baseUrl
    }

    public feedApi = (input: string, callback: (res: string) => void) => {
        request
            .post(`${this.baseUrl}/feeder/feed`)
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

export default Client