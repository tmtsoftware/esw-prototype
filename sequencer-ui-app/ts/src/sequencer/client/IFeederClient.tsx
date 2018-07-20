import * as request from "superagent";

class Client {
    public feedApi = (input: string, callback: (res: string) => void) => {
        request
            .post('http://localhost:8000/feeder/feed')
            .set('Content-Type', 'application/json')
            .send(input)
            .then(res => callback(JSON.stringify(res.body, null, 2)), err => {
                    if (err.response) {
                        callback(err.response.text)
                    } else {
                        callback("Oops something went wrong !!, please verify input.")
                    }
                }
            );
    }
}

export default Client